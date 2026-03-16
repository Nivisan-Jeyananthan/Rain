package ch.nivisan.raincloud.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import ch.nivisan.raincloud.network.utilities.StringCipher;

public class Client {
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
			return;
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

		String response = recieveBytes();
		return connected || (response != null && !response.isEmpty());
	}
	
	public String getBytes() {
		try {
			socket.setSoTimeout(0);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return recieveBytes();
	}

	private String recieveBytes() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);		

		try {
			if (!socket.isClosed())
				socket.receive(packet);
		} catch (SocketException socketException) {
			socketException.printStackTrace();
			return "";
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		String message = new String(packet.getData(), 0, packet.getLength());

		// format is: 
		// /pk/{id}/{publicKey}/e/ for public key exchange
		// /c/{id}/e/ for connection confirmation
		// /e/{encryptedMessage}/e/ for encrypted messages
		// /i/{id}/e/ for id request
		if (!handshakeComplete && message.startsWith("/pk/")) {
			int endIndex = message.indexOf("/e/");
			if (endIndex > 0) {
				String body = message.substring(4, endIndex);
				int separator = body.indexOf("/");
				if (separator > 0) {
					String idPart = body.substring(0, separator);
					String keyPart = body.substring(separator + 1);
					try {
						this.Id = Integer.parseInt(idPart);
						byte[] keyBytes = Base64.getDecoder().decode(keyPart);
						X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
						KeyFactory keyFactory = KeyFactory.getInstance("RSA");
						serverPublicKey = keyFactory.generatePublic(keySpec);
					} catch (Exception e) {
						return "";
					}

					try {
						sessionKey = StringCipher.generateKey();
						sessionIv = StringCipher.generateIv();
						String plainToken = "SYMMETRIC:" + Id + ":" + Base64.getEncoder().encodeToString(sessionKey.getEncoded()) + ":" + Base64.getEncoder().encodeToString(sessionIv.getIV());
						byte[] encrypted = StringCipher.encryptRSA(plainToken, serverPublicKey);
						if (encrypted == null)
							return "";
						String payload = Base64.getEncoder().encodeToString(encrypted);
						sendBytes(("/ks/" + payload + "/e/").getBytes());
					} catch (Exception e) {
						e.printStackTrace();
						return "";
					}
				}
			}
			return "";
		}

		if (message.startsWith("/c/")) {
			String[] parts = message.split("/c/|/e/");
			if (parts.length > 1) {
				this.Id = Integer.parseInt(parts[1]);
				connected = true;
				handshakeComplete = true;
				running = true;
			}
			return message;
		} else if (message.startsWith("/ks/")) {
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
						int id = Integer.parseInt(token[1]);
						this.Id = id;
						byte[] keyBytes = Base64.getUrlDecoder().decode(token[2]);
						byte[] ivBytes = Base64.getUrlDecoder().decode(token[3]);
						sessionKey = StringCipher.decodeSecretKey(keyBytes);
						sessionIv = new IvParameterSpec(ivBytes);
						handshakeComplete = true;
					}
				}
			} catch (Exception e) {
				return "";
			}
			return "";
		} else if (message.startsWith("/e/")) {
			int endIndex = message.lastIndexOf("/e/");
			if (endIndex <= 3)
				return "";
			String encoded = message.substring(3, endIndex);
			byte[] cipherText = Base64.getUrlDecoder().decode(encoded);
			String plain = StringCipher.decrypt(cipherText, sessionKey, sessionIv);
			return plain == null ? "" : plain;
		} else if (message.startsWith("/i/")) {
			if (Id >= 0) {
				final String serverData = "/i/" + Id + "/e/";
				sendBytes(serverData.getBytes());
			}
		}

		return message;
	}

	public void sendText(String message) {
		message = message.replaceAll("/\\w/", "");
		String packet = "/m/" + message + "/e/";
		sendEncrypted(packet);
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
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);

				try {
					if (!socket.isClosed())
						socket.send(packet);
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
		if (!connected())
			return;

		if (!kicked) {
			String message = "/d/" + Id + "/e/";
			sendBytes(message.getBytes());
		}

		new Thread() {
			public void run() {
				synchronized (socket) {
					try {
						socket.disconnect();
						socket.close();
					} catch (Exception e) {
						System.out.println("Not closed socket system");
					}

				}
			}
		}.start();

	}

}
