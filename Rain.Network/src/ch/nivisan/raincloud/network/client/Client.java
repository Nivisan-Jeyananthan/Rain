package ch.nivisan.raincloud.network.client;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import ch.nivisan.raincloud.network.utilities.Audio;
import ch.nivisan.raincloud.network.utilities.NetUtils;
import ch.nivisan.raincloud.network.utilities.StringCipher;

class Client {
	private DatagramSocket socket;
	private InetAddress ip;
	final int port;

	final String name;
	public final String address;
	private KeyPair keyPair;
	private SecretKey sessionKey;
	private IvParameterSpec sessionIv;
	private int Id = -1;

	private boolean running = false;
	private AtomicBoolean micRunning = new AtomicBoolean(false);
	private boolean connected = false;
	private boolean handshakeComplete = false;
	private MicRecorder micThread;

	Client(final String name, final String address, final int port) {
		this.name = name;
		this.address = address;
		this.port = port;

		try {
			socket = new DatagramSocket();
			socket.setSoTimeout(3000);
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
			ip = null;
			socket = null;
		}
	}

	boolean connected() {
		return connected;
	}

	boolean connect() {
		if (ip == null || socket == null)
			return false;

		handshakeComplete = false;
		keyPair = StringCipher.generateRSAKey();
		String clientPubKey = StringCipher.encodeString(keyPair.getPublic().getEncoded());
		sendBytes(("/c/" + name + "/" + clientPubKey + "/e/").getBytes());

		long deadline = System.currentTimeMillis() + 8000;
		while (System.currentTimeMillis() < deadline) {
			receiveMessage();
			if (handshakeComplete && connected)
				return true;
		}
		return connected;
	}

	String getBytes() {
		try {
			socket.setSoTimeout(0);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return receiveMessage();
	}

	private String receiveMessage() {
		byte[] data = new byte[NetUtils.MAX_PACKET_SIZE];
		DatagramPacket packet = new DatagramPacket(data, data.length);

		try {
			if (!socket.isClosed())
				socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		String message = new String(packet.getData(), 0, packet.getLength());

		if (message.startsWith("/e/")) {
			int endIndex = message.lastIndexOf("/e/");
			if (endIndex <= 3)
				return "";
			String encoded = message.substring(3, endIndex);
			try {
				byte[] cipherText = Base64.getDecoder().decode(encoded);
				String plain = StringCipher.decrypt(cipherText, sessionKey, sessionIv);
				message = plain == null ? "" : plain;
			} catch (Exception e) {
				return "";
			}
		}

		if (message.startsWith("/c/")) {
			String[] parts = message.split("/c/|/e/");
			if (parts.length > 1) {
				Id = Integer.parseInt(parts[1]);
				connected = true;
				running = true;
			}
			return message;
		}

		if (message.startsWith("/ks/")) {
			int endIndex = message.indexOf("/e/");
			if (endIndex <= 4)
				return "";
			String payload = message.substring(4, endIndex);
			try {
				byte[] encrypted = Base64.getUrlDecoder().decode(payload);
				String decrypted = StringCipher.decryptRSA(encrypted, keyPair.getPrivate());
				if (decrypted != null && decrypted.startsWith("SYMMETRIC:")) {
					String[] token = decrypted.split(":", 4);
					if (token.length == 4) {
						Id = Integer.parseInt(token[1]);
						sessionKey = StringCipher.decodeSecretKeyFromBase64(token[2]);
						sessionIv = new IvParameterSpec(StringCipher.decodeString(token[3]));
						handshakeComplete = true;
						connected = true;
						running = true;
					}
				}
			} catch (Exception e) {
				return "";
			}
			return "";
		}

		if (message.startsWith("/i/")) {
			if (Id >= 0) {
				sendBytes(("/i/" + Id + "/e/").getBytes());
			}
		}

		// TODO: handle voice input/ output
		if (message.startsWith("/v/")) {
			File wavFile = new File("aufnahme.wav");
		// try (AudioInputStream ais = new AudioInputStream(line)) {
		// 	AudioSystem.write(ais, AudioFileFormat.Type.WAVE, wavFile);
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		}

		return message;
	}

	void sendText(String message) {
		message = message.replaceAll("/\\w/", "");
		sendEncrypted("/m/" + message + "/e/");
	}

	void requestUsernames() {
		sendEncrypted("/u/");
	}

	private void sendEncrypted(String payload) {
		if (handshakeComplete && sessionKey != null && sessionIv != null) {
			byte[] encrypted = StringCipher.encrypt(payload, sessionKey, sessionIv);
			if (encrypted != null) {
				String encoded = Base64.getEncoder().encodeToString(encrypted);
				sendBytes(("/e/" + encoded + "/e/").getBytes());
				return;
			}
		}
		sendBytes(payload.getBytes());
	}

	private void sendBytes(final byte[] data) {
		final Thread sendThread = new Thread("Send") {
			public void run() {
				try {
					if (!socket.isClosed()) {
						DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
						socket.send(packet);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		sendThread.start();
	}

	public void setId(int Id) {
		this.Id = Id;
	}

	public int getId() {
		return Id;
	}

	public void quit(boolean kicked) {
		if (!connected)
			return;

		if (!kicked) {
			sendEncrypted("/d/" + Id + "/e/");
		}

		new Thread(() -> {
			synchronized (socket) {
				try {
					socket.disconnect();
					socket.close();
				} catch (Exception e) {
					System.out.println("Not closed socket system");
				}
			}
		}).start();
	}

	public void sendAudio() {
		if (!micRunning.get() && micThread == null) {
			micThread = new MicRecorder();
			micThread.start();
		} else {
			micThread.stopMic();
			micThread = null;
		}
	}

	boolean getMicRunning() {
		System.out.println("Running mic: " + micRunning);
		return micRunning.get();
	}

	class MicRecorder extends Thread {
		TargetDataLine dataLine;

		@Override
		public void run() {
			micRunning.set(true);

			while (micRunning.get()) {
				dataLine = Audio.getTargetDataLine();
				if (dataLine == null)
					return;

				try {
					dataLine.open(Audio.defaultFormat);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
				dataLine.start();
				byte[] buffer = new byte[Audio.bufferSize];
				dataLine.read(buffer, 0, buffer.length);

				sendEncrypted(("/v/" + StringCipher.encodeString(buffer)));
				System.out.println("Send audio bytes");
			}
		}

		void stopMic() {
			micRunning.set(false);
			if (dataLine != null) {
				dataLine.drain();
				dataLine.stop();
				dataLine.close();
			}
		}

	}
}
