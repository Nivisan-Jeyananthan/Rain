package ch.nivisan.raincloud.network.server;

import java.net.DatagramSocket;
import java.net.SocketException;

public class Server implements Runnable {
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
        }

        serverThread = new Thread(this, "Server");
    }

    @Override
    public void run() {
        running = true;
        manageClients();
        recieveBytes();
    }

    private void recieveBytes() {
        recieveThread = new Thread("Recieve"){
            public void run(){
                while(running){
                    
                }
            }
        };

        recieveThread.start();
    }

    private void manageClients() {
        manageThread = new Thread("Manage"){
            public void run(){
                while(running){
                    
                }
            }
        };

        manageThread.start();
    }
}
