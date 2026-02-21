package ch.nivisan.raincloud.serialization;

import java.util.Random;

/**
 * rESPONIBLE FOR INTERACTING WITH LOWEST LEVEL OF OPERATIONS.
 * IDEALLY WRITES THE FORMAT ITSELF.
 * 
 * USER GIVES STRINGS OR OTHER OBJ DATA, IT GETS CONVERTED INTO BYTES
 * IS NOT RESPONSIBLE FOR INPUT/OUTPUT
 */

public class SerializationWriter {
	public static final byte[] headerName = "RC".getBytes();
	public static final short version = 0x0100; // big-endian
	
	
	public static int copyBytes(byte[] destination, int pointer, byte[] source) {		
		for (int i = 0; i < source.length; i++) {
			destination[pointer++] = source[i];
		}
		return pointer;
	}
	
	public static int writeBytes(byte[] destination, int pointer, byte value) {
		destination[pointer++] = value;
		return pointer;
	}
	
	/**
	 * Shorts are 16bit
	 */
	public static int writeBytes(byte[] destination, int pointer, short value) {
		destination[pointer++] = (byte)((value >> 8) & 0xff);
		destination[pointer++] = (byte)((value >> 0) & 0xff);
		
		return pointer;
	}
	

	/**
	 * Char is unsinged Short 16 bit
	 * @param destination
	 * @param pointer
	 * @param value
	 * @return pointer to next location
	 */ 
	public static int writeBytes(byte[] destination, int pointer, char value) {
		destination[pointer++] = (byte)((value >> 8) & 0xff);
		destination[pointer++] = (byte)((value >> 0) & 0xff);
		
		return pointer;
	}
	
	/**
	 * Integers are 32bit
	 * @param destination
	 * @param pointer
	 * @param value
	 * @return pointer to next location
	 */
	public static int writeBytes(byte[] destination, int pointer, int value) {
		destination[pointer++] = (byte)((value >> 24) & 0xff);
		destination[pointer++] = (byte)((value >> 16) & 0xff);
		destination[pointer++] = (byte)((value >> 8) & 0xff);
		destination[pointer++] = (byte)((value >> 0) & 0xff);
		
		return pointer;
	}
	
	/**
	 * long are 64bit values
	 * @param destination
	 * @param pointer
	 * @param value
	 * @return pointer to next location
	 */
	public static int writeBytes(byte[] destination, int pointer, long value) {
		destination[pointer++] = (byte)((value >> 56) & 0xff);
		destination[pointer++] = (byte)((value >> 48) & 0xff);
		destination[pointer++] = (byte)((value >> 40) & 0xff);
		destination[pointer++] = (byte)((value >> 32) & 0xff);
		destination[pointer++] = (byte)((value >> 24) & 0xff);
		destination[pointer++] = (byte)((value >> 16) & 0xff);
		destination[pointer++] = (byte)((value >> 8) & 0xff);
		destination[pointer++] = (byte)((value >> 0) & 0xff);
		
		return pointer;
	}
	
	
	public static int writeBytes(byte[] destination, int pointer, float value) {
		int data = Float.floatToIntBits(value);

		return writeBytes(destination,pointer,data);
	}
	
	public static int writeBytes(byte[] destination, int pointer, double value) {
		double data = Double.doubleToLongBits(value);
		
		return writeBytes(destination,pointer,data);
	}
	
	/**
	 * Boolean = 1 Byte
	 * @param destination
	 * @param pointer
	 * @param value
	 * @return pointer to next location
	 */
	public static int writeBytes(byte[] destination, int pointer, boolean value) {
		
		destination[pointer++] = (byte)  (value ? 1 : 0);
		
		return pointer;
	}
	
	
	/**
	 * Boolean = 1 Byte
	 * TODO: test and rewrite for arrays
	 * @param destination
	 * @param pointer
	 * @param values
	 * @return pointer to next location
	 */
	public static int writeBytes(byte[] destination, int pointer, boolean[] values) {
		
		  byte bitfield = 0;
		    
		    for (int i = 0; i < values.length -1; i++) {
		 	   System.out.println(values[i]);
		    	}
		    int max = values.length >= 8 ? 8 : values.length -1;
		    
		    for (int i = 0; i < max; i++) {
		 	   bitfield |=  (values[i] ? 1 : 0) << i;
		    }
		    
		    destination[pointer++] = bitfield;
		    System.out.println(bitfield);
		
		return pointer;
	}
	 
  
	/**
	 * 	// 2 options: 
		// 1. Write the size of the string before the string as a short ( 07 NI VI SA N).
		// bytes of 2 for size so we are not limited to 255 characters but have plenty of space
		// 2. Null-termination character (binary 0) at the end: (NI VI SA N0).
		// it is not the same as the character 0
	 * @param destination
	 * @param pointer
	 * @param value
	 * @return
	 */
	public static int writeBytes(byte[] destination, int pointer, String value) {
		pointer = writeBytes(destination, pointer, (short)value.length());
		return copyBytes(destination, pointer, value.getBytes());
	}
	
	public static byte readByte(byte[] source, int pointer) {
		return source[pointer];
	}
	
	public static char readChar(byte[] source, int pointer) {
		return (char)  (source[pointer] << 8 | source[pointer+1]);
	}
	
	
	public static short readShort(byte[] source, int pointer) {
		return (short)  (source[pointer] << 8 | source[pointer+1]);
	}
	
	
	public static int readInt(byte[] source, int pointer) {
		return source[pointer] << 24 | source[pointer+1] << 16 | source[pointer+2] << 8 | source[pointer+3] << 0;
	}
	
	public static int readLong(byte[] source, int pointer) {
		return source[pointer] << 56 |
				source[pointer+1] << 48 |
				source[pointer+2] << 40 |
				source[pointer+3] << 32 |
				source[pointer+4] << 24 | 
				source[pointer+5] << 16 | 
				source[pointer+6] << 8 |
				source[pointer+7] << 0;
	}
	
	public static float readFloat(byte[] source, int pointer) {
		return Float.intBitsToFloat(readInt(source, pointer));
	}
	
	public static double readDouble(byte[] source, int pointer) {
		return Double.longBitsToDouble(readLong(source, pointer));
	}
	
	public static boolean readBoolean(byte[] source, int pointer) {
		return source[pointer] != 0;
	}
}
