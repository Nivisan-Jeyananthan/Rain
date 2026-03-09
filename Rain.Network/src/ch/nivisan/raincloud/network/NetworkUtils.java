package ch.nivisan.raincloud.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class NetworkUtils {
    // udp socket
    private DatagramSocket socket;
    private InetAddress ip;
    private int port;

    private Thread sendThread;
    private Thread recieveThread;

    public NetworkUtils(String address, int port) {
        try {
            socket = new DatagramSocket(port);
            ip = InetAddress.getByName(address);
            this.port = port;
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
        }
    }

    public boolean connected() {
        return ip != null;
    }

    private String recieveBytes() {
        byte[] data = new byte[1024];
        DatagramPacket packet = new DatagramPacket(data, data.length);

        recieveThread = new Thread("recieve") {
            public void run() {
                try {
                    socket.receive(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String message = new String(packet.getData());
            }
        };

        return null;
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

}
