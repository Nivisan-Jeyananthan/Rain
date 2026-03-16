package ch.nivisan.raincloud.network.server;

import java.net.InetSocketAddress;
import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ClientInfo {

    public static final int PORT = 4445;
    public static final int MAX_PACKET = 1024;
    public static final int GCM_TAG_BYTES = 16;
    public static final int SESSION_PREFIX_LEN = 4;
final int clientId;
        final SecretKey key;
        final byte[] sessionPrefix = new byte[SESSION_PREFIX_LEN];
        // recv replay
        long highestRecv = -1;
        long recvWindow = 0L;
        // send seq for server->client messages
        final AtomicLong sendSeq = new AtomicLong(0);
        // last known address
        volatile InetSocketAddress addr;

    
        ClientInfo(int id, byte[] keyBytes) {
            this.clientId = id;
            this.key = new SecretKeySpec(keyBytes, "AES");
            new SecureRandom().nextBytes(sessionPrefix);
        }
        synchronized boolean acceptRecv(long seq) {
            if (highestRecv == -1) return true;
            if (seq > highestRecv) return true;
            long diff = highestRecv - seq;
            return diff < 64 && ((recvWindow >>> diff) & 1L) == 0;
        }
        synchronized void markRecv(long seq) {
            if (highestRecv == -1 || seq > highestRecv) {
                long shift = (highestRecv == -1) ? 0 : seq - highestRecv;
                if (shift >= 64) recvWindow = 0L;
                else if (shift > 0) recvWindow <<= shift;
                highestRecv = seq;
                recvWindow |= 1L;
            } else {
                int pos = (int)(highestRecv - seq);
                if (pos < 64) recvWindow |= (1L << pos);
            }
        }
}
