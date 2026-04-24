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

	protected final PublicKey clientPublicKey;
	protected final SecretKey sessionKey;
	protected final IvParameterSpec sessionIv;
	protected boolean handshakeComplete = false;

	public final static int maxAttempts = 5;

	public ServerClient(String name, InetAddress address, int port, PublicKey clientKey, SecretKey key,
			IvParameterSpec iv, boolean handshakeComplete) {
		Id = UniqueIdentifier.getIdentifier();
		this.name = name;
		this.address = address;
		this.port = port;
		this.clientPublicKey = clientKey;
		this.sessionKey = key;
		this.sessionIv = iv;
		this.handshakeComplete = handshakeComplete;

		System.out.println("ID: " + Id + " handshake=" + handshakeComplete);
	}

	public int getId() {
		return Id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ServerClient that = (ServerClient) obj;
		return Id == that.Id;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(Id);
	}
}
