package ch.nivisan.raincloud.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Server implements Runnable {
	private List<ServerClient> clients = new ArrayList<ServerClient>();
	private HashSet<Integer> clientResponses = new HashSet<Integer>();

	private final int port;
	private DatagramSocket socket;

	private Thread serverThread;
	private Thread manageThread;
	private Thread recieveThread;
	private Thread sendThread;
	private boolean running;

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
		// manageClients();
		recieveBytes();
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
		if (value.startsWith("/c/")) {
			String name = value.split("/c/|/e/")[1];
			ServerClient serverClient = new ServerClient(name,packet.getAddress(),
					packet.getPort());
			clients.add(serverClient);
			sendMessage(serverClient);
		} else if (value.startsWith("/m/")) {
			relayMessage(value);
		} else if (value.startsWith("/d/")) {
			int index = Integer.parseInt(value.split("/d/|/e/")[1]);
			disconnectClient(index, true);
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
	private void disconnectClient(int id, boolean status) {
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
		if (status) {
			message = "Client " + client.name + "(" + client.Id + ")" + "@" + client.address.toString() + ":"
					+ client.port + " disconnected";
		} else {
			message = "Client " + client.name + "(" + client.Id + ")" + "@" + client.address.toString() + ":"
					+ client.port + " timed out";
		}

		System.out.println(message);

	}

	private void relayMessage(String message) {
		for (int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			sendBytes(message.getBytes(), client.address, client.port);
		}
	}

	private void manageClients() {
		manageThread = new Thread("Manage") {
			public void run() {
				while (running) {
					relayMessage("/i/server/e/");
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					for (int i = 0; i < clients.size(); i++) {
						ServerClient client = clients.get(i);
						if (!clientResponses.contains(clientResponses)) {
							if (client.attempt >= ServerClient.maxAttempts) {
								disconnectClient(client.Id, false);
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

	private void sendMessage(ServerClient client) {
		String message = "/c/" + client.getId() + "/e/";
		sendBytes(message.getBytes(), client.address, port);
		System.out.println("Client created with ID: "+ client.getId());
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
