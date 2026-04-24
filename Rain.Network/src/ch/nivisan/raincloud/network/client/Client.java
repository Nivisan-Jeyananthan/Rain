package ch.nivisan.raincloud.network.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyPair;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import ch.nivisan.raincloud.network.utilities.Audio;
import ch.nivisan.raincloud.network.utilities.AudioResampler;
import ch.nivisan.raincloud.network.utilities.MessageType;
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

	private AtomicBoolean micRunning = new AtomicBoolean(false);
	private boolean connected = false;
	private boolean handshakeComplete = false;
	private Thread micThread;
	private TargetDataLine micLine;
	private DeviceInfo currentMicrophone;
	private SourceDataLine speakerLine;
	private DeviceInfo currentSpeaker;

	private final ExecutorService executor = Executors.newCachedThreadPool();

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
		sendBytes((MessageType.CONNECT + name + "/" + clientPubKey + MessageType.CONNECT).getBytes());

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
		String message = receivePacket();
		if (message.isEmpty()) {
			return "";
		}

		if (message.startsWith(MessageType.ENCRYPTED)) {
			message = decryptMessage(message);
			if (message == null || message.isEmpty()) {
				return "";
			}
		}

		if (message.startsWith(MessageType.CONNECT)) {
			handleConnectResponse(message);
			return message;
		}

		if (message.startsWith(MessageType.KEY_SYNC)) {
			handleKeySync(message);
			return "";
		}

		if (message.startsWith(MessageType.KEEP_ALIVE)) {
			handleKeepAlive();
			return "";
		}

		if (message.startsWith(MessageType.VOICE)) {
			handleVoicePacket(message);
		}

		return message;
	}

	private String receivePacket() {
		byte[] data = new byte[NetUtils.MAX_PACKET_SIZE];
		DatagramPacket packet = new DatagramPacket(data, data.length);

		try {
			if (!socket.isClosed()) {
				socket.receive(packet);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}

		return new String(packet.getData(), 0, packet.getLength());
	}

	private String decryptMessage(String message) {
		int endIndex = message.indexOf(MessageType.ENCRYPTED, 3);
		if (endIndex <= MessageType.ENCRYPTED.length()) {
			return "";
		}

		String encoded = message.split(MessageType.ENCRYPTED)[1];
		try {
			byte[] cipherText = StringCipher.decodeString(encoded);
			return StringCipher.decrypt(cipherText, sessionKey, sessionIv);
		} catch (Exception e) {
			return "";
		}
	}

	private void handleConnectResponse(String message) {
		String[] parts = message.split(MessageType.CONNECT);
		if (parts.length > 1) {
			Id = Integer.parseInt(parts[1]);
			connected = true;
		}
	}

	private void handleKeySync(String message) {
		int endIndex = message.indexOf(MessageType.KEY_SYNC, 3);
		if (endIndex <= MessageType.KEY_SYNC.length()) {
			return;
		}

		String payload = message.substring(MessageType.KEY_SYNC.length(), endIndex);
		try {
			byte[] encrypted = StringCipher.decodeString(payload);
			String decrypted = StringCipher.decryptRSA(encrypted, keyPair.getPrivate());
			if (decrypted == null || !decrypted.startsWith("SYMMETRIC:")) {
				return;
			}

			String[] token = decrypted.split(":", 4);
			if (token.length != 4) {
				return;
			}

			Id = Integer.parseInt(token[1]);
			sessionKey = StringCipher.decodeSecretKeyFromBase64(token[2]);
			sessionIv = new IvParameterSpec(StringCipher.decodeString(token[3]));
			handshakeComplete = true;
			connected = true;
		} catch (Exception e) {
			// ignore invalid handshake packet
		}
	}

	private void handleKeepAlive() {
		if (Id >= 0) {
			sendBytes((MessageType.KEEP_ALIVE + Id + MessageType.KEEP_ALIVE).getBytes());
		}
	}

	private void handleVoicePacket(String message) {
		String[] parts = message.split(MessageType.VOICE);
		if (parts.length <= 1) {
			return;
		}

		byte[] voiceData = StringCipher.decodeString(parts[1]);
		DeviceInfo selectedSpeaker = DeviceSettings.getSpeaker();
		AudioFormat targetFormat = selectedSpeaker != null ? selectedSpeaker.format : Audio.defaultFormat;
		voiceData = Audio.resampleToFormat(voiceData, targetFormat);
		playVoice(voiceData);
	}

	void sendText(String message) {
		message = message.replaceAll("/\\w/", "");
		sendEncrypted(MessageType.MESSAGE + message + MessageType.MESSAGE);
	}

	void requestUsernames() {
		sendEncrypted(MessageType.USERS);
	}

	private void sendEncrypted(String payload) {
		if (handshakeComplete && sessionKey != null && sessionIv != null) {
			byte[] encrypted = StringCipher.encrypt(payload, sessionKey, sessionIv);
			if (encrypted != null) {
				String encoded = StringCipher.encodeString(encrypted);
				sendBytes((MessageType.ENCRYPTED + encoded + MessageType.ENCRYPTED).getBytes());
				return;
			}
		}
		sendBytes(payload.getBytes());
	}

	private void sendVoice(byte[] voiceData) {
		if (voiceData == null || voiceData.length == 0)
			return;

		String encoded = StringCipher.encodeString(voiceData);
		sendEncrypted(MessageType.VOICE + encoded + MessageType.VOICE);
	}

	private void sendBytes(final byte[] data) {
		executor.submit(() -> {
			try {
				if (!socket.isClosed()) {
					socket.send(new DatagramPacket(data, data.length, ip, port));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
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
			sendEncrypted(MessageType.DISCONNECT + Id + MessageType.DISCONNECT);
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
			executor.shutdown();
		}).start();
	}

	public void sendAudio() {
		if (micRunning.compareAndSet(false, true)) {
			startMicRecorder();
			return;
		}
		refreshMicLine();
	}

	private void startMicRecorder() {
		micThread = new Thread(new MicRecorder(), "MicRecorder");
		micThread.start();
	}

	public void closeAudio() {
		micRunning.set(false);
		closeMicLine();
		micThread = null;
	}

	private void playVoice(byte[] voiceData) {
		if (voiceData == null || voiceData.length == 0)
			return;

		DeviceInfo selectedSpeaker = DeviceSettings.getSpeaker();
		if (!sameDevice(currentSpeaker, selectedSpeaker)) {
			resetSpeakerLine(selectedSpeaker);
		}

		if (speakerLine == null) {
			speakerLine = createSpeakerLine(selectedSpeaker);
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
		closeSpeakerLine();
		currentSpeaker = selectedSpeaker;
		speakerLine = createSpeakerLine(currentSpeaker);
	}

	private SourceDataLine createSpeakerLine(DeviceInfo speaker) {
		SourceDataLine line = Audio.getSourceDataLine(speaker);
		if (line == null) {
			return null;
		}

		try {
			if (!line.isOpen()) {
				line.open(speaker.format);
			}
			if (!line.isRunning()) {
				line.start();
			}
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return null;
		}

		return line;
	}

	private void closeSpeakerLine() {
		if (speakerLine != null) {
			speakerLine.drain();
			speakerLine.stop();
			speakerLine.close();
			speakerLine = null;
		}
	}

	private synchronized void refreshMicLine() {
		DeviceInfo selectedMic = DeviceSettings.getMicrophone();
		if (!sameDevice(currentMicrophone, selectedMic)) {
			closeMicLine();
			currentMicrophone = selectedMic;
			micLine = createMicLine(currentMicrophone);
		}
	}

	private synchronized TargetDataLine ensureMicLine() {
		DeviceInfo selectedMic = DeviceSettings.getMicrophone();
		if (!sameDevice(currentMicrophone, selectedMic)) {
			refreshMicLine();
		}

		if (micLine == null && currentMicrophone != null) {
			micLine = createMicLine(currentMicrophone);
		}

		return micLine;
	}

	private TargetDataLine createMicLine(DeviceInfo microphone) {
		if (microphone == null) {
			return null;
		}

		TargetDataLine line = Audio.getTargetDataLine(microphone);
		if (line == null) {
			return null;
		}

		try {
			if (!line.isOpen()) {
				// Try to open with microphone's format, fall back to best detected format if
				// not available
				AudioFormat formatToUse = microphone.format;
				try {
					line.open(formatToUse);
				} catch (LineUnavailableException e1) {
					// Fallback: try stereo versions of common formats
					try {
						line.open(Audio.defaultFormatStereo);
					} catch (LineUnavailableException e2) {
						try {
							line.open(Audio.compatFormatStereo);
						} catch (LineUnavailableException e3) {
							try {
								line.open(Audio.fallbackFormatStereo);
							} catch (LineUnavailableException e4) {
								// Final fallback to mono default format
								line.open(Audio.defaultFormat);
							}
						}
					}
				}
			}
			if (!line.isRunning()) {
				line.start();
			}
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			return null;
		}

		return line;
	}

	private synchronized void closeMicLine() {
		if (micLine != null) {
			micLine.stop();
			micLine.close();
			micLine = null;
		}
	}

	boolean getMicRunning() {
		return micRunning.get();
	}

	private class MicRecorder implements Runnable {
		private final byte[] buffer = new byte[Audio.bufferSize];
		private AudioResampler resampler = null;
		private AudioFormat lastFormat = null;

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

				// Check if we need to update the resampler based on current microphone format
				AudioFormat currentFormat = currentMicrophone != null ? currentMicrophone.format : Audio.defaultFormat;
				if (!currentFormat.equals(lastFormat)) {
					lastFormat = currentFormat;
					if (!currentFormat.equals(Audio.defaultFormat)) {
						resampler = new AudioResampler(currentFormat, Audio.defaultFormat);
					} else {
						resampler = null;
					}
				}

				int bytesRead = activeLine.read(buffer, 0, buffer.length);
				if (bytesRead > 0) {
					byte[] voiceData = new byte[bytesRead];
					System.arraycopy(buffer, 0, voiceData, 0, bytesRead);

					// Resample capture to network format. The device-specific input format is
					// handled by the AudioResampler based on the microphone's actual format.
					if (resampler != null && resampler.needsResampling()) {
						voiceData = resampler.resample(voiceData);
					}

					sendVoice(voiceData);
				}
			}
			closeMicLine();
		}
	}
}
