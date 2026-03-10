package ch.nivisan.raincloud.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {
	protected final List<ServerClient> clients = new ArrayList<ServerClient>();
	private final HashSet<Integer> clientResponses = new HashSet<Integer>();

	private final int port;
	protected DatagramSocket socket;

	private Thread serverThread;
	private Thread manageThread;
	private Thread recieveThread;
	private Thread sendThread;
	protected boolean running;
	protected boolean raw;

	public Server(int port) {
		this.port = port;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
			return;
		}

		serverThread = new Thread(this, "Server");
		serverThread.start();
	}

	@Override
	public void run() {
		running = true;
		System.out.println("Server listening on port: " + port);
		manageClients();
		recieveBytes();

		Scanner scanner = new Scanner(System.in);
		while (running) {
			String text = scanner.nextLine();
			if (!text.startsWith(".")) {
				relayMessage("/m/Server:" + text + "/e/");
				continue;
			}

			ServerCommands.read(text, this, scanner);
		}
	}

	private void recieveBytes() {
		recieveThread = new Thread("Recieve") {
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);

					try {
						socket.receive(packet);
						processPacket(packet);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};

		recieveThread.start();
	}

	private void processPacket(DatagramPacket packet) {
		String value = new String(packet.getData());
		if (raw) {
			System.out.println("Raw: " + value);
		}

		if (value.startsWith("/c/")) {
			String name = value.split("/c/|/e/")[1];
			ServerClient serverClient = new ServerClient(name, packet.getAddress(),
					packet.getPort());
			clients.add(serverClient);
			sendConnectionId(serverClient);
			String message = "/m/ >>" + serverClient.name + " has joined the Chat << /e/";
			relayMessage(message);
			return;
		} 	
		else if (value.startsWith("/m/")) {
			relayMessage(value);
		} else if (value.startsWith("/d/")) {
			int index = Integer.parseInt(value.split("/d/|/e/")[1]);
			disconnectClient(index, ClientDisconnectType.Disconnect);
		} else if (value.startsWith("/i/")) {
			int id = Integer.parseInt(value.split("/i/|/e/")[1]);
			clientResponses.add(id);
		} else {
			System.out.println(value);
		}
	}

	/**
	 * 
	 * @param id
	 * @param status client closed = true, unnatural causes = false
	 */
	private void disconnectClient(int id, ClientDisconnectType status) {
		ServerClient client = null;
		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).getId() == id) {
				client = clients.get(i);
				clients.remove(i);
				break;
			}
		}

		if (client == null)
			return;

		String message = "";
		if (status == ClientDisconnectType.Disconnect) {
			message = "Client " + client.name + "(" + client.Id + ")" + "@" + client.address.toString() + ":"
					+ client.port + " disconnected";
		} else if (status == ClientDisconnectType.Timeout) {
			message = "Client " + client.name + "(" + client.Id + ")" + "@" + client.address.toString() + ":"
					+ client.port + " timed out";
		} else if (status == ClientDisconnectType.Kick) {
			message = "Client " + client.name + "(" + client.Id + ")" + "@" + client.address.toString() + ":"
					+ client.port + " kicked out";
			sendBytes("/d/".getBytes(), client.address, client.port);
			relayMessage("/m/ ---- Server: The user (" + client.name + ") has been kicked from the server ----");
		}

		System.out.println(message);

	}

	protected boolean kickClient(String name) {
		for (int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			if (client.name.equals(name)) {
				disconnectClient(client.Id, ClientDisconnectType.Kick);
				return true;
			}
		}
		return false;
	}

	protected boolean kickClient(int id) {
		for (int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			if (client.getId() == id) {
				disconnectClient(id, ClientDisconnectType.Kick);
				return true;
			}
		}
		return false;
	}

	protected void relayMessage(String message) {
		byte[] messageBytes = message.getBytes();
		for (int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			sendBytes(messageBytes, client.address, client.port);
		}
	}

	private void manageClients() {
		final String messageString = "/i/server/e/";

		manageThread = new Thread("Manage") {
			public void run() {
				while (running) {
					relayMessage(messageString);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					for (int i = 0; i < clients.size(); i++) {
						ServerClient client = clients.get(i);
						if (!clientResponses.contains(client.Id)) {
							if (client.attempt >= ServerClient.maxAttempts) {
								disconnectClient(client.Id, ClientDisconnectType.Timeout);
							} else {
								client.attempt++;
							}
						} else {
							clientResponses.remove(client.Id);
							client.attempt = 0;
						}

					}
				}
			}
		};

		manageThread.start();
	}

	private void sendConnectionId(ServerClient client) {
		String message = "/c/" + client.getId() + "/e/";
		sendBytes(message.getBytes(), client.address, client.port);
		System.out.println("Client created with ID: " + client.getId());
	}

	private void sendBytes(final byte[] data, final InetAddress clientAddress, final int clientPort) {
		sendThread = new Thread("Send") {
			public void run() {
				DatagramPacket packet = new DatagramPacket(data, data.length, clientAddress, clientPort);

				try {
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		sendThread.start();
	}

}
