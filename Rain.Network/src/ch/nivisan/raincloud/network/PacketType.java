package ch.nivisan.raincloud.network;

public class PacketType {
    public final static byte serverConnect = 1;
    public final static byte serverDisconnect = 2;
    public final static byte serverMessage = 3;

    public final static byte clientConnection = 10;
    public final static byte clientDisconnect = 12;
    public final static byte clientMessage = 10;

    public final static byte relayMessage = 20;
}
