package ch.nivisan.raincloud.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.IconifyAction;

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

		try {
			socket = new DatagramSocket();
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}

		establishConnection();
		running = true;
	}

	private void establishConnection() {
		String data = "/c/" + name;
		sendBytes(data.getBytes());
		recieveBytes();
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
			Id = Integer.parseInt(message.substring(3, 7));
		}

		return message;
	}

	public void sendBytes(final byte[] data) {
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

	public int getId() {
		return Id;
	}

}
