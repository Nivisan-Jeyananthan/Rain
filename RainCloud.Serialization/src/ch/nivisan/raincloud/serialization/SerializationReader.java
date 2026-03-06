package ch.nivisan.raincloud.serialization;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
        return ByteBuffer.wrap(source,pointer,2).getShort();
    }

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as int
     */
    public static int readInt(byte[] source, int pointer) {
        return ByteBuffer.wrap(source, pointer, 4).getInt();
    }

    /**
     * Read data at said pointer position
     * 
     * @param source  from where to read the data
     * @param pointer at what position our data begins
     * @return our data as long
     */
    public static long readLong(byte[] source, int pointer) {
        return ByteBuffer.wrap(source, pointer, 8).getLong();
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

    /**
     * Read multiple booleans from one byte at given position
     * 
     * @param source
     * @param pointer
     * @return
     */
    public static boolean[] readBitField(byte[] source, int pointer) {
        byte field = source[pointer++];
        boolean[] bools = new boolean[8];
        for (byte i = 0; i < 8; i++)
            bools[i] = ((field >> i) & 1) != 0;

        return bools;
    }

    /**
     * Reads a string from a byte array with given length
     * 
     * @param source
     * @param pointer
     * @param length
     * @return A new String based on values from the byte array
     */
    public static String readString(byte[] source, int pointer, int length) {
        return new String(source, pointer, length);
    }

    /**
     * Reads a byte array from a byte array
     * 
     * @param source      from where to read the data
     * @param pointer     at which index it will begin reading
     * @param destination to where to write the data
     * @return the pointers new location
     */
    public static int readBytes(byte[] source, int pointer, byte[] destination) {
        for (int i = 0; i < destination.length; i++) {
            destination[i] = source[i + pointer];
        }

        return pointer += destination.length;
    }

    /**
     * Reads a byte array from a byte array by length
     * 
     * @param source  the array to read values from
     * @param pointer at which index it will begin reading
     * @param length  the desired length of the new array
     * @return a new byte array with given length
     */
    public static byte[] readBytes(byte[] source, int pointer, int length) {
        return ByteBuffer.wrap(source, pointer, length).array();
    }

    /**
     * Reads a byte array from a byte array by length
     * 
     * @param source  the array to read values from
     * @param pointer at which index it will begin reading
     * @param length  the desired length of the new array
     * @return a new byte array with given length
     */
    public static byte[] readBytes(byte[] source, int[] pointer, int length) {
        var bytes = ByteBuffer.wrap(source, pointer[0], length).array();
        pointer[0] += bytes.length;
        return bytes;
    }

    /**
     * Reads character values from a given byte array and retrieves one
     * 
     * @param source  the array to read values from
     * @param pointer at which index it will begin reading
     * @param length  the desired length of the new array
     * @return a new byte array with given length
     * @return
     */
    public static char[] readCharArray(byte[] source, int pointer, int length) {
        assert (source.length >= pointer + (length * RCType.CHAR_SIZE));
        char[] results = new char[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readChar(source, pointer);
            pointer += RCType.CHAR_SIZE;
        }
        return results;
    }

    /**
     * Reads character values from a given byte array and retrieves one
     * 
     * @param source  the array to read values from
     * @param pointer at which index it will begin reading as an array of 1 element
     * @param length  the desired length of the new array
     * @return a new byte array with given length
     * @return
     */
    public static char[] readCharArray(byte[] source, int[] pointer, int length) {
        assert (source.length >= pointer[0] + (length * RCType.CHAR_SIZE));
        char[] results = new char[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readChar(source, pointer[0]);
            pointer[0] += RCType.CHAR_SIZE;
        }
        return results;
    }

    /**
     * Reads boolean values from a given byte array and creates a new one with the
     * values
     * 
     * @param source  the array to read values from
     * @param pointer at which index it will begin reading
     * @param length  the desired length of the new array
     * @return a new byte array with given length
     * @return the new boolean array with desired length
     */
    public static boolean[] readBoolArray(byte[] source, int pointer, int length) {
        assert (source.length >= pointer + (length * RCType.BOOLEAN_SIZE));
        boolean[] results = new boolean[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = source[pointer] != 0;
            pointer += RCType.BOOLEAN_SIZE;
        }
        return results;
    }

    /**
     * Reads boolean values from a given byte array and creates a new one with the
     * values
     * 
     * @param source  the array to read values from
     * @param pointer at which index it will begin reading as an array of 1 element
     * @param length  the desired length of the new array
     * @return a new byte array with given length
     * @return the new boolean array with desired length
     */
    public static boolean[] readBoolArray(byte[] source, int[] pointer, int length) {
        assert (source.length >= pointer[0] + (length * RCType.BOOLEAN_SIZE));
        boolean[] results = new boolean[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = source[pointer[0]] != 0;
            pointer[0] += RCType.BOOLEAN_SIZE;
        }
        return results;
    }

    /**
     * Reads short values from a given byte array and creates a new short array to
     * return
     * 
     * @param source  the array to read values from
     * @param pointer at which index it will begin reading
     * @param length  the desired length of the new array
     * @return a new byte array with given length
     * @return the new short array with desired length
     */
    public static short[] readShortArray(byte[] source, int pointer, int length) {
        assert (source.length >= pointer + (length * RCType.SHORT_SIZE));
        short[] results = new short[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readShort(source, pointer);
            pointer += RCType.SHORT_SIZE;
        }
        return results;
    }

    /**
     * Reads short values from a given byte array and creates a new short array to
     * return
     * 
     * @param source  the array to read values from
     * @param pointer at which index it will begin reading
     * @param length  the desired length of the new array
     * @return a new byte array with given length
     * @return the new short array with desired length
     */
    public static short[] readShortArray(byte[] source, int[] pointer, int length) {
        assert (source.length >= pointer[0] + (length * RCType.SHORT_SIZE));
        short[] results = new short[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readShort(source, pointer[0]);
            pointer[0] += RCType.SHORT_SIZE;
        }
        return results;
    }

    /**
     * Creates an int array from a byte array
     * 
     * @param source
     * @param pointer from which point we can expect the values to be
     * @param length  the length of the new array
     * @return a new int array with values from the byte array
     */
    public static int[] readIntArray(byte[] source, int pointer, int length) {
        assert (source.length >= pointer + (length * RCType.INT_SIZE));
        int[] results = new int[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readInt(source, pointer);
            pointer += RCType.INT_SIZE;
        }
        return results;
    }

    /**
     * Creates an int array from a byte array
     * 
     * @param source
     * @param pointer the index where to start reading as an array of 1 element
     * @param length  the length of the new array
     * @return a new int array with values from the byte array
     */
    public static int[] readIntArray(byte[] source, int[] pointer, int length) {
        assert (source.length >= pointer[0] + (length * RCType.INT_SIZE));
        int[] results = new int[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readInt(source, pointer[0]);
            pointer[0] += RCType.INT_SIZE;
        }
        return results;
    }

    /**
     * Creates a long array from a byte array
     * 
     * @param source
     * @param pointer
     * @param length  length of the new array
     * @return a long array with the values form the byte array
     */
    public static long[] readLongArray(byte[] source, int pointer, int length) {
        assert (source.length >= pointer + (length * RCType.LONG_SIZE));
        long[] results = new long[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readLong(source, pointer);
            pointer += RCType.LONG_SIZE;
        }
        return results;
    }

    /**
     * Creates a long array from a byte array
     * 
     * @param source
     * @param pointer the index where to start reading as an array of 1 element
     * @param length  length of the new array
     * @return a long array with the values form the byte array
     */
    public static long[] readLongArray(byte[] source, int[] pointer, int length) {
        assert (source.length >= pointer[0] + (length * RCType.LONG_SIZE));
        long[] results = new long[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readLong(source, pointer[0]);
            pointer[0] += RCType.LONG_SIZE;
        }
        return results;
    }

    /**
     * Reads a float array from a byte array
     * 
     * @param source
     * @param pointer the index where to start reading as an array of 1 element
     * @param length  length of the new array
     * @return the byte array vlaues as a float array
     */
    public static float[] readFloatArray(byte[] source, int[] pointer, int length) {
        assert (source.length >= pointer[0] + (length * RCType.FLOAT_SIZE));
        float[] results = new float[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readFloat(source, pointer[0]);
            pointer[0] += RCType.FLOAT_SIZE;
        }
        return results;
    }

    /**
     * Reads a float array from a byte array
     * 
     * @param source
     * @param pointer the index where to start reading as an array of 1 element
     * @param length  length of the new array
     * @return the byte array vlaues as a float array
     */
    public static float[] readFloatArray(byte[] source, int pointer, int length) {
        assert (source.length >= pointer + (length * RCType.FLOAT_SIZE));
        float[] results = new float[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readFloat(source, pointer);
            pointer += RCType.FLOAT_SIZE;
        }
        return results;
    }

    /**
     * Reads a double array from a byte array
     * 
     * @param source
     * @param pointer
     * @param length  length of the new array
     * @return the byte array as a double array
     */
    public static double[] readDoubleArray(byte[] source, int pointer, int length) {
        assert (source.length >= pointer + (length * RCType.DOUBLE_SIZE));
        double[] results = new double[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readDouble(source, pointer);
            pointer += RCType.DOUBLE_SIZE;
        }
        return results;
    }

    /**
     * Reads a double array from a byte array
     * 
     * @param source
     * @param pointer the index where to start reading as an array of 1 element
     * @param length  length of the new array
     * @return the byte array as a double array
     */
    public static double[] readDoubleArray(byte[] source, int[] pointer, int length) {
        assert (source.length >= pointer[0] + (length * RCType.DOUBLE_SIZE));
        double[] results = new double[length];
        for (int i = 0; i < results.length; i++) {
            results[i] = readDouble(source, pointer[0]);
            pointer[0] += RCType.DOUBLE_SIZE;
        }
        return results;
    }
}