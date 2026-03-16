package ch.nivisan.raincloud.network.utilities;

import java.util.Arrays;

public class Base32 {
    private static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray();
    private static final int[] lookup = new int[128]; // ASCII lookup table
 static {
        Arrays.fill(lookup, -1);
        for (int i = 0; i < alphabet.length; i++) {
            lookup[alphabet[i]] = i;
            lookup[Character.toLowerCase(alphabet[i])] = i; // accept lower‑case
        }
    }

    /** Encode a byte array to a Base32 string (RFC 4648). */
    public static String encode(byte[] data) {
        if (data == null || data.length == 0) return "";

        StringBuilder sb = new StringBuilder((data.length + 4) / 5 * 8);
        int i = 0;
        while (i < data.length) {
            int remaining = data.length - i;

            // pack up to 5 bytes into a 40‑bit buffer
            long buffer = 0;
            for (int j = 0; j < 5; j++) {
                buffer <<= 8;
                if (j < remaining) {
                    buffer |= (data[i + j] & 0xFF);
                }
            }

            // extract eight 5‑bit groups
            int outputBytes = (remaining * 8 + 4) / 5; // 1‑8 chars
            for (int j = 0; j < outputBytes; j++) {
                int index = (int) ((buffer >> (35 - 5 * j)) & 0x1F);
                sb.append(alphabet[index]);
            }

            // pad with '=' to a multiple of 8 characters
            for (int j = outputBytes; j < 8; j++) {
                sb.append('=');
            }

            i += 5;
        }
        return sb.toString();
    }

    /** Decode a Base32 string (RFC 4648) back to a byte array. */
    public static byte[] decode(String base32) {
        if (base32 == null) return new byte[0];

        // Remove padding and whitespace
        String clean = base32.trim().replaceAll("=+", "").replaceAll("\\s+", "");
        if (clean.isEmpty()) return new byte[0];

        int outputLen = clean.length() * 5 / 8;
        byte[] result = new byte[outputLen];
        int buffer = 0;      // holds up to 8 bits of the current byte
        int bitsLeft = 0;    // number of bits currently in buffer
        int outPos = 0;

        for (char c : clean.toCharArray()) {
            int val = (c < lookup.length) ? lookup[c] : -1;
            if (val == -1) {
                throw new IllegalArgumentException("Invalid Base32 character: " + c);
            }

            buffer = (buffer << 5) | val;
            bitsLeft += 5;

            if (bitsLeft >= 8) {
                bitsLeft -= 8;
                result[outPos++] = (byte) ((buffer >> bitsLeft) & 0xFF);
            }
        }
        return result;
    }
}
