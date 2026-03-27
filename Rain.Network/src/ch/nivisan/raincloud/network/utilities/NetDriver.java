package ch.nivisan.raincloud.network.utilities;

import java.io.BufferedOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.InvalidParameterException;

public class NetDriver {
    protected final String serverAddress;
    protected final int port;
    private DatagramSocket socket;
    private InetAddress address;

    public NetDriver(String serverAddress, int port) {
        this.serverAddress = serverAddress;

        if (port < 0 || port > 65_000)
            throw new InvalidParameterException("Port must be between 0 and 65’536");

        this.port = port;

        try {
            this.socket = new DatagramSocket();
            this.address = InetAddress.getByName(serverAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(byte[] data) {
        try {
            DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
            socket.send(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        if (socket != null && !socket.isClosed())
            socket.close();
    }

    public InetAddress getAddress() {
        return address;
    }
}
