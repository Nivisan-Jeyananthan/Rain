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
import javax.sound.sampled.SourceDataLine;
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
	private Thread micThread;
	private TargetDataLine micLine;
	private DeviceInfo currentMicrophone;
	private SourceDataLine speakerLine;
	private DeviceInfo currentSpeaker;

	// TODO: remove
	private AudioWav waveAudio;

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

		// TODO: remove
		waveAudio = new AudioWav(new File("audio.wav"));

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

		if (message.startsWith("/v/")) {

			String[] messageData = message.split("/v/");
			if (messageData.length > 1) {
				byte[] voiceData = StringCipher.decodeString(messageData[1]);
				playVoice(voiceData);
			}
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
			micRunning.set(true);
			micThread = new Thread(new MicRecorder());
			micThread.start();
		} else if (micRunning.get()) {
			refreshMicLine();
		}
	}

	public void closeAudio() {
		micRunning.set(false);
		closeMicLine();
		if (micThread != null) {
			micThread = null;
		}
		// TODO: remove
		waveAudio.close();
	}

	private void playVoice(byte[] voiceData) {
		if (voiceData == null || voiceData.length == 0)
			return;

		DeviceInfo selectedSpeaker = DeviceSettings.getSpeaker();
		if (!sameDevice(currentSpeaker, selectedSpeaker)) {
			resetSpeakerLine(selectedSpeaker);
		}

		if (speakerLine != null) {
			Audio.writeAudio(speakerLine, voiceData);
		}
	}

	private boolean sameDevice(DeviceInfo a, DeviceInfo b) {
		if (a == b)
			return true;
		if (a == null || b == null)
			return false;
		return a.equals(b);
	}

	private void resetSpeakerLine(DeviceInfo selectedSpeaker) {
		if (speakerLine != null) {
			speakerLine.drain();
			speakerLine.stop();
			speakerLine.close();
			speakerLine = null;
		}
		currentSpeaker = selectedSpeaker;
		if (currentSpeaker != null) {
			speakerLine = Audio.getSourceDataLine(currentSpeaker);
			if (speakerLine != null && !speakerLine.isOpen()) {
				try {
					speakerLine.open(currentSpeaker.format);
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
			if (speakerLine != null && !speakerLine.isRunning()) {
				speakerLine.start();
			}
		}
	}

	private synchronized void refreshMicLine() {
		DeviceInfo selectedMic = DeviceSettings.getMicrophone();
		if (!sameDevice(currentMicrophone, selectedMic)) {
			closeMicLine();
			currentMicrophone = selectedMic;
			if (currentMicrophone != null) {
				micLine = Audio.getTargetDataLine(currentMicrophone);
				if (micLine != null && !micLine.isOpen()) {
					try {
						micLine.open(currentMicrophone.format);
						micLine.start();
					} catch (LineUnavailableException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private synchronized TargetDataLine ensureMicLine() {
		DeviceInfo selectedMic = DeviceSettings.getMicrophone();
		if (!sameDevice(currentMicrophone, selectedMic)) {
			refreshMicLine();
		}
		if (micLine == null && currentMicrophone != null) {
			micLine = Audio.getTargetDataLine(currentMicrophone);
			if (micLine != null && !micLine.isOpen()) {
				try {
					micLine.open(currentMicrophone.format);
					micLine.start();
				} catch (LineUnavailableException e) {
					e.printStackTrace();
				}
			}
		}
		return micLine;
	}

	private synchronized void closeMicLine() {
		if (micLine != null) {
			micLine.stop();
			micLine.close();
			micLine = null;
		}
	}

	boolean getMicRunning() {
		System.out.println("Running mic: " + micRunning);
		return micRunning.get();
	}

	private class MicRecorder implements Runnable {
		@Override
		public void run() {
			while (micRunning.get()) {
				TargetDataLine activeLine = ensureMicLine();
				if (activeLine == null || !activeLine.isOpen()) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
					continue;
				}

				byte[] buffer = new byte[Audio.bufferSize];
				int bytesRead = activeLine.read(buffer, 0, buffer.length);
				if (bytesRead > 0) {
					byte[] voiceData = new byte[bytesRead];
					System.arraycopy(buffer, 0, voiceData, 0, bytesRead);
					sendEncrypted(("/v/" + StringCipher.encodeString(voiceData) + "/v/"));
				}
			}
			closeMicLine();
		}
	}
}
