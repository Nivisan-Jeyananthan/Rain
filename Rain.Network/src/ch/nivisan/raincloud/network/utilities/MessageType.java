package ch.nivisan.raincloud.network.utilities;

public class MessageType {
	public static final String CONNECT = "/c/";
	public static final String KEY_SYNC = "/ks/";
	public static final String ENCRYPTED = "/e/";
	public static final String KEEP_ALIVE = "/i/";
	public static final String VOICE = "/v/";
	public static final String MESSAGE = "/m/";
	public static final String USERS = "/u/";
	public static final String DISCONNECT = "/d/";
	
	private MessageType() {}
	
}
