package ch.nivisan.raincloud.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.xml.stream.events.StartDocument;

public class Client {
	private DatagramSocket socket;
	private InetAddress ip;
	public final int port;
	public final String name;
	public final String address;
	private int Id = -1;

	private Thread sendThread;
	private Thread recieveThread;
	private boolean running;

	public Client(final String name, final String address, final int port) {
		this.name = name;
		this.address = address;
		this.port = port;

		establishConnection();
	}

	private void establishConnection() {
		String data = "/c/" + name + "/e/";
		new Thread() {
			public void run() {
				try {
					socket = new DatagramSocket();
					ip = InetAddress.getByName(address);
				} catch (UnknownHostException | SocketException e) {
					e.printStackTrace();
					return;
				}

				sendBytes(data.getBytes());
				recieveBytes();
				running = true;

			}
		}.start();


	}

	public boolean connected() {
		return ip != null;
	}

	public String recieveBytes() {
		byte[] data = new byte[1024];
		DatagramPacket packet = new DatagramPacket(data, data.length);

		try {
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String message = new String(packet.getData());
		if (message.startsWith("/c/")) {
			String tempString = message.split("/c/|/e/")[1];
			this.Id = Integer.parseInt(tempString);
		} else if (message.startsWith("/i/")) {
			String serverData = "/i/" + Id + "/e/";
			sendBytes(serverData.getBytes());
		}

		return message;
	}

	public void sendText(String message) {
		message = message.replaceAll("/\\w/", "");
		message = "/m/" + message + "/e/";
		sendBytes(message.getBytes());
	}

	private void sendBytes(final byte[] data) {
		sendThread = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
				try {
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

	public void quit() {
		if (!connected())
			return;

		String message = "/d/" + Id + "/e/";
		sendBytes(message.getBytes());

		new Thread() {
			public void run() {
				synchronized (socket) {
					socket.close();
				}
			}
		}.start();

	}

}
