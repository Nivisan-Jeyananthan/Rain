package ch.nivisan.raincloud.network.server;

import java.net.InetAddress;
import java.security.PublicKey;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class ServerClient {
	public static final AtomicInteger count = new AtomicInteger(0);

	public final int Id;
	public final String name;
	public final InetAddress address;
	public final int port;
	public int attempt = 0;

	public PublicKey clientPublicKey;
	public SecretKey sessionKey;
	public IvParameterSpec sessionIv;
	public boolean handshakeComplete = false;

	public final static int maxAttempts = 5;

	public ServerClient(String name, InetAddress address, int port) {
		Id = UniqueIdentifier.getIdentifier();
		System.out.println("ID: " + Id);
		this.name = name;
		this.address = address;
		this.port = port;
	}

	public int getId() {
		return Id;
	}
}
