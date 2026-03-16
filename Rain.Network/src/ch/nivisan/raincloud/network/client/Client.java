package ch.nivisan.raincloud.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import ch.nivisan.raincloud.network.utilities.StringCipher;

public class Client {
	private static final int MAX_PACKET_SIZE = 1024;

	private DatagramSocket socket;
	private InetAddress ip;
	public final int port;
	public final String name;
	public final String address;
	private KeyPair keyPair;
	private SecretKey sessionKey;
	private IvParameterSpec sessionIv;
	private int Id = -1;

	private Thread sendThread;
	private boolean running;
	private boolean connected;
	private boolean handshakeComplete;

	public Client(final String name, final String address, final int port) {
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

	public boolean connected() {
		return connected;
	}

	public boolean connect() {
		if (ip == null || socket == null)
			return false;

		handshakeComplete = false;
		keyPair = StringCipher.generateRSAKey();
		String clientPubKey = Base64.getUrlEncoder().withoutPadding().encodeToString(keyPair.getPublic().getEncoded());
		sendBytes(("/c/" + name + "/" + clientPubKey + "/e/").getBytes());

		String message = receiveMessage();
		return connected || (message != null && !message.isEmpty());
	}

	public String getBytes() {
		try {
			socket.setSoTimeout(0);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return receiveMessage();
	}

	private String receiveMessage() {
		byte[] data = new byte[MAX_PACKET_SIZE];
		DatagramPacket packet = new DatagramPacket(data, data.length);

		try {
			if (!socket.isClosed())
				socket.receive(packet);
		} catch (SocketException | IOException e) {
			e.printStackTrace();
			return "";
		}

		String message = new String(packet.getData(), 0, packet.getLength());

		if (message.startsWith("/c/") && !handshakeComplete) {
			String[] parts = message.split("/c/|/e/");
			if (parts.length > 1) {
				Id = Integer.parseInt(parts[1]);
				connected = true;
				handshakeComplete = true;
				running = true;
			}
			return message;
		}

		if (message.startsWith("/ks/")) {
			int endIndex = message.indexOf("/e/");
			if (endIndex <= 4) return "";
			String payload = message.substring(4, endIndex);
			try {
				byte[] encrypted = Base64.getUrlDecoder().decode(payload);
				String decrypted = StringCipher.decryptRSA(encrypted, keyPair.getPrivate());
				if (decrypted != null && decrypted.startsWith("SYMMETRIC:")) {
					String[] token = decrypted.split(":", 4);
					if (token.length == 4) {
						Id = Integer.parseInt(token[1]);
						sessionKey = StringCipher.decodeSecretKey(Base64.getUrlDecoder().decode(token[2]));
						sessionIv = new IvParameterSpec(Base64.getUrlDecoder().decode(token[3]));
						handshakeComplete = true;
					}
				}
			} catch (Exception e) {
				return "";
			}
			return "";
		}

		if (message.startsWith("/e/")) {
			int endIndex = message.lastIndexOf("/e/");
			if (endIndex <= 3) return "";
			String encoded = message.substring(3, endIndex);
			try {
				byte[] cipherText = Base64.getDecoder().decode(encoded);
				String plain = StringCipher.decrypt(cipherText, sessionKey, sessionIv);
				return plain == null ? "" : plain;
			} catch (Exception e) {
				return "";
			}
		}

		if (message.startsWith("/i/")) {
			if (Id >= 0) {
				sendBytes(("/i/" + Id + "/e/").getBytes());
			}
		}

		return message;
	}

	public void sendText(String message) {
		message = message.replaceAll("/\\w/", "");
		sendEncrypted("/m/" + message + "/e/");
	}

	public void requestUsernames() {
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
		sendThread = new Thread("Send") {
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
			sendBytes(("/d/" + Id + "/e/").getBytes());
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
}
