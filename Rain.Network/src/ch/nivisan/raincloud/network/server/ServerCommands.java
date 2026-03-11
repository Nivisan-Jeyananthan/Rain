package ch.nivisan.raincloud.network.server;

import java.io.IOException;
import java.net.SocketException;
import java.util.Scanner;

public class ServerCommands {

	private final static String clients = ".clients";
	private final static String raw = ".raw";
	private final static String exitProgramm = ".exit";
	private final static String clearConsole = ".clear";
	private final static String kickClient = ".kick";
	private final static String messagePrefix = "-m";

	public static void read(String text, Server server, Scanner scanner) {
		if (text.equalsIgnoreCase(raw)) {
			server.raw = !server.raw;
		} else if (text.equalsIgnoreCase(clients)) {
			System.out.println("\nClients connected: " + server.clients.size());
			System.out.println("=======================================\n");
			for (ServerClient client : server.clients) {
				System.out.println(
						client.name + "(" + client.getId() + ")" + "@" + client.address.toString() + ":" + client.port);
			}
		} else if (text.equals(clearConsole)) {
			System.out.print("\033\143");
		} else if (text.equalsIgnoreCase(exitProgramm)) {
			exitProgram(server, scanner);
		} else if (text.startsWith(kickClient)) {
			kickClient(text, server);
		} else {
			printHelp();
		}
	}

	private static void printHelp() {
		System.out.println("Available commands are: ");
		System.out.println("------------------------------");
		System.out.println(clearConsole + " -> clears the console\n");
		System.out.println(raw + " -> enables raw mode to see all incoming data\n");
		System.out.println(clients + " -> lists all connected clients\n");
		System.out.println(kickClient + " [id or name]" + " -> kick the connected client with id or name");
		System.out.println("\t e.g id  : .kick 1850");
		System.out.println("\t e.g name: .kick Hugo\n");
		System.out.println(exitProgramm + " -> kicks all connected clients and quits the program\n");
		System.out.println(messagePrefix + " -> sends a message to all connected clients as a server message\n");
		System.out.println("------------------------------");
	}

	private static void kickClient(String text, Server server) {
		String name = text.split(kickClient)[1].trim().toLowerCase();
		int id = -1;

		try {
			id = Integer.parseInt(name);
		} catch (Exception exception) {
		}

		boolean isKicked = false;
		if (id == -1)
			isKicked = server.kickClient(name);

		else
			isKicked = server.kickClient(id);

		if (!isKicked)
			System.out.println("Could not kick the client. \nID: " + id + " or name:" + name + " is wrong");
	}

	private static void exitProgram(Server server, Scanner scanner) {
		System.out.println("Stopping server...");
		server.relayMessage("/m/ Server : Server is shutting down shortly /e/");
		scanner.close();

		new Thread() {
			public void run() {

				while (server.clients.size() != 0) {
					server.kickClient(server.clients.getFirst().Id);
				}

				synchronized (server.socket) {

					server.running = false;
					server.socket.disconnect();
					server.socket.close();

					System.exit(0);
				}
			}
		}.start();
	}

}
