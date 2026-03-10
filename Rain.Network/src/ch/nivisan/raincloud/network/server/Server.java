package ch.nivisan.raincloud.network.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {
    private List<ServerClient> clients = new ArrayList<ServerClient>();

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
        manageClients();
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
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    processPacket(packet);
                }
            }
        };

        recieveThread.start();
    }

    private void processPacket(DatagramPacket packet) {
        String value = new String(packet.getData());
        if (value.startsWith("/c/")) {
            ServerClient serverClient = new ServerClient(value.substring(4, value.length()), packet.getAddress(),
                    packet.getPort());
            clients.add(serverClient);
            String id = "/c/" + serverClient.Id;
            sendMessage(id, packet.getAddress(), packet.getPort());

        } else if (value.startsWith("/m/")) {
            relayMessage(value);
        } else {
        }
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
                }
            }
        };

        manageThread.start();
    }

    private void sendMessage(String message, InetAddress address, int port) {
        message += "/e/";
        sendBytes(message.getBytes(), address, port);
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
