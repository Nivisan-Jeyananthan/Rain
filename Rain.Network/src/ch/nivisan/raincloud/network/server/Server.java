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
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Server implements Runnable {
	protected final List<ServerClient> clients = Collections.synchronizedList(new ArrayList<ServerClient>());

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
			// avoid blocking if stdin is closed (e.g., non-interactive container)
			if (!scanner.hasNextLine()) {
				// no more input available; sleep briefly and continue
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
				continue;
			}
			String text = scanner.nextLine();
			if (text.startsWith("-m")) {
				text = text.split("-m")[1].trim();
				relayMessage("/m/Server:" + text + "/e/");
				continue;
			}

			ServerCommands.read(text, this, scanner);
		}
		scanner.close();
	}

	private void recieveBytes() {
		recieveThread = new Thread("Recieve") {
			public void run() {
				while (running) {
					byte[] data = new byte[1024];
					DatagramPacket packet = new DatagramPacket(data, data.length);

					try {
						if (!socket.isClosed())
							socket.receive(packet);
						processPacket(packet);
					} catch (SocketException e) {
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
			ServerClient serverClient = new ServerClient(name, packet.getAddress(), packet.getPort());
			clients.add(serverClient);
			sendConnectionId(serverClient);
			String message = "/m/ >>" + serverClient.name + " has joined the Chat << /e/";
			relayMessage(message);
			return;
		} else if (value.startsWith("/m/")) {
			relayMessage(value);
		} else if (value.startsWith("/d/")) {
			int index = Integer.parseInt(value.split("/d/|/e/")[1]);
			disconnectClient(getClient(index), ClientDisconnectType.Disconnect);
		} else if (value.startsWith("/i/")) {
			int id = Integer.parseInt(value.split("/i/|/e/")[1]);
			clientResponses.add(id);
		} else if (value.startsWith("/u/")) {
			sendBytes(getOnlineUsers().getBytes(), packet.getAddress(), packet.getPort());
		} else {
			System.out.println(value);
		}
	}

	private ServerClient getClient(int id) {
		ServerClient client = null;
		for (int i = 0; i < clients.size(); i++) {
			client = clients.get(i);
			if (client.Id == id)
				return client;
		}
		return client;
	}

	/**
	 * 
	 * @param id
	 * @param status client closed = true, unnatural causes = false
	 */
	private void disconnectClient(ServerClient client, ClientDisconnectType status) {
		clients.remove(client);

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

	/**
	 * Perform an action on every client matching the predicate. The previous
	 * implementation returned from the loop after the first match, which meant
	 * broadcasts (`condition` always true) only ever reached the first client in
	 * the list. That is why the second client never saw the periodic `/i/`
	 * keep‑alive and timed out.
	 *
	 * @return true if at least one client matched the condition
	 */
	/**
	 * Execute the given action on every client that satisfies the predicate.
	 * This is used for broadcasts (e.g. relaying a message to all clients, or
	 * sending the periodic heartbeat) and therefore must iterate the entire
	 * list.
	 *
	 * @return true if at least one client matched the condition
	 */
	private boolean handleClientAction(Predicate<ServerClient> condition, Consumer<ServerClient> action) {
		boolean any = false;
		for (int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			if (condition.test(client)) {
				action.accept(client);
				any = true;
			}
		}
		return any;
	}

	/**
	 * Execute an action once on the first client that satisfies the predicate.
	 * The loop exits immediately after the first match, which is desirable for
	 * operations such as kicking or disconnecting a specific user.
	 *
	 * @return true if a client was found and action executed
	 */
	private boolean handleFirstClient(Predicate<ServerClient> condition, Consumer<ServerClient> action) {
		for (int i = 0; i < clients.size(); i++) {
			ServerClient client = clients.get(i);
			if (condition.test(client)) {
				action.accept(client);
				return true;
			}
		}
		return false;
	}

	protected boolean kickClient(String name) {
		return handleFirstClient(client -> client.name.toLowerCase().equals(name),
				client -> disconnectClient(client, ClientDisconnectType.Kick));
	}

	protected boolean kickClient(int id) {
		return handleFirstClient(client -> client.Id == id,
				client -> disconnectClient(client, ClientDisconnectType.Kick));
	}

	protected void relayMessage(String message) {
		byte[] messageBytes = message.getBytes();
		handleClientAction(_ -> true, client -> sendBytes(messageBytes, client.address, client.port));
	}

	protected String getOnlineUsers() {
		String usernames = "/u/";
		if (clients.size() == 1) {
			return "/u/" + clients.get(0).name + "/e/";
		}

		for (int i = 0; i < clients.size() - 1; i++) {
			ServerClient client = clients.get(i);
			if (!client.name.isBlank())
				usernames += client.name + "/n/";
		}

		usernames += clients.get(clients.size() - 1).name + "/e/";
		return usernames;
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
						e.printStackTrace();
					}

					for (int i = 0; i < clients.size(); i++) {
						ServerClient client = clients.get(i);
						if (!clientResponses.contains(client.Id)) {
							if (client.attempt >= ServerClient.maxAttempts) {
								disconnectClient(client, ClientDisconnectType.Timeout);
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
					if (!socket.isClosed())
						socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		sendThread.start();
	}

}
