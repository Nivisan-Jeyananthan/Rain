package ch.nivisan.raincloud.serialization;

import java.nio.ByteBuffer;

public class SerializationReader {
    public static final byte[] headerName = "RC".getBytes();
    public static final short version = 0x0100; // big-endian

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as byte
     */
    public static byte readByte(byte[] source, int pointer) {
        return source[pointer];
    }

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as char
     */
    public static char readChar(byte[] source, int pointer) {
        return (char) (source[pointer] << 8 | source[pointer + 1]);
    }

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as short
     */
    public static short readShort(byte[] source, int pointer) {
        return (short) (source[pointer] << 8 | source[pointer + 1]);
    }

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as int
     */
    public static int readInt(byte[] source, int pointer) {
        return ByteBuffer.wrap(source,pointer,4).getInt();
        /* 
        return source[pointer] << 24 |
                source[pointer + 1] << 16 |
                source[pointer + 2] << 8 |
                source[pointer + 3] << 0;
        */
    }

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as long
     */
    public static long readLong(byte[] source, int pointer) {
        return source[pointer] << 56 |
                source[pointer + 1] << 48 |
                source[pointer + 2] << 40 |
                source[pointer + 3] << 32 |
                source[pointer + 4] << 24 |
                source[pointer + 5] << 16 |
                source[pointer + 6] << 8 |
                source[pointer + 7] << 0;
    }

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as float
     */
    public static float readFloat(byte[] source, int pointer) {
        return Float.intBitsToFloat(readInt(source, pointer));
    }

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as double
     */
    public static double readDouble(byte[] source, int pointer) {
        return Double.longBitsToDouble(readLong(source, pointer));
    }

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as boolean
     */
    public static boolean readBoolean(byte[] source, int pointer) {
        return source[pointer] != 0;
    }

    public static String readString(byte[] source, int pointer, int length) {
        return new String(source, pointer, length);
    }
}
