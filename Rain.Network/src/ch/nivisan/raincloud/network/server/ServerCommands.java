package ch.nivisan.raincloud.network.server;

import java.util.Scanner;

public class ServerCommands {

    private final static String clients = ".clients";
    private final static String raw = ".raw";
    private final static String exitProgramm = ".exit";
    private final static String clearConsole = ".clear";
    private final static String kickClient = ".kick";

    public static void read(String text, Server server, Scanner scanner) {
        if (text.equalsIgnoreCase(raw)) {
            server.raw = !server.raw;
        } else if (text.equalsIgnoreCase(clients)) {
            System.out.println("\nClients connected: " + server.clients.size());
            System.out.println("=======================================\n");
            for (ServerClient client : server.clients) {
                System.out.println(client.name + "(" + client.getId() + ")" + "@" + client.address.toString() + ":"
                        + client.port);
            }
        } else if (text.equals(clearConsole)) {
            System.out.print("\033\143");
        } else if (text.equalsIgnoreCase(exitProgramm)) {
            exitProgram(server, scanner);
        } else if (text.startsWith(kickClient)) {
            kickClient(text, server);
        }
    }

    private static void kickClient(String text, Server server) {
        String name = text.split(".kick")[1].trim();
        int id = -1;

        try {
            id = Integer.parseInt(name);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        boolean isKicked = false;
        if (id == -1)
            isKicked = server.kickClient(id);

        else
            isKicked = server.kickClient(name);

        if (!isKicked)
            System.out.println("Could not kick the client. \nID: " + id + " or name:" + name + " is wrong");
    }

    private static void exitProgram(Server server, Scanner scanner) {
        System.out.println("Stopping server...");
        server.relayMessage("/m/ Server : Server is shutting down shortly /e/");
        scanner.close();

        new Thread() {
            public void run() {

                synchronized (server.socket) {
                    server.running = false;

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    server.socket.close();
                    System.exit(0);
                }
            }
        }.start();
    }

}
