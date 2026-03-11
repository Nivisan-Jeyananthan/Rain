package ch.nivisan.raincloud.network.server;

public class ServerMain {
    private final Server server;
    private final int port;

    public ServerMain(int port) {
        this.port = port;
        server = new Server(port);
    }

    public static void main(String[] args) {
        if (args.length > 1 || args.length == 0) {
            System.out.println("Usage: java ServerMain.jar <port>");
            return;
        }

        int localPort = Integer.parseInt(args[0]);
        new ServerMain(localPort);
    }
}
