package ch.nivisan.raincloud.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
	private boolean connected;

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

		sendBytes(("/c/" + name + "/e/").getBytes());
		return recieveBytes() != "";
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
		String message = new String(packet.getData());
		if (message.startsWith("/c/")) {
			final String tempString = message.split("/c/|/e/")[1];
			this.Id = Integer.parseInt(tempString);
			connected = true;
			running = true;
		} else if (message.startsWith("/i/")) {
			final String serverData = "/i/" + Id + "/e/";
			sendBytes(serverData.getBytes());
		}

		return message;
	}

	public void sendText(String message) {
		message = message.replaceAll("/\\w/", "");
		message = "/m/" + message + "/e/";
		sendBytes(message.getBytes());
	}

	public void requestUsernames() {
		sendBytes("/u/".getBytes());
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
						socket.close();
					} catch (Exception e) {
						System.out.println("Not closed socket system");
					}

				}
			}
		}.start();

	}

}
