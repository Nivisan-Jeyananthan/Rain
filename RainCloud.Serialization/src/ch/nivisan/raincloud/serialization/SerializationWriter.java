package ch.nivisan.raincloud.serialization;

/**
 * responible for interacting with lowest level of operations.
 * ideally writes the format itself.
 * 
 * user gives strings or other obj data, it gets converted into bytes
 * is not responsible for input/output
 */

public class SerializationWriter {
	public static final byte[] headerName = "RC".getBytes();
	public static final short version = 0x0100; // big-endian

	/**
	 * Copies the bytes from 1 array to another using the pointer as a starting
	 * position
	 * 
	 * @param destination into which array the data will be written to
	 * @param pointer     the starting position at which we will insert our data
	 * @param source      from where we will take the data
	 * @return pointer position to the end of our data
	 */
	public static int copyBytes(byte[] destination, int pointer, byte[] source) {
		assert (destination.length >= pointer + source.length);

		for (int i = 0; i < source.length; i++) {
			destination[pointer++] = source[i];
		}
		return pointer;
	}

	/**
	 * Copies the bytes from 1 array to another using the pointer as a starting
	 * position
	 * 
	 * @param destination into which array the data will be written to
	 * @param pointer     the starting position at which we will insert our data
	 * @param source      from where we will take the data
	 * @return pointer position to the end of our data
	 */
	public static int copyBytes(byte[] destination, int pointer, char[] source) {
		assert (destination.length >= pointer + source.length);

		for (int i = 0; i < source.length; i++) {
			pointer = writeBytes(destination, pointer, source[i]);
		}
		return pointer;
	}

	/**
	 * Copies the bytes from 1 array to another using the pointer as a starting
	 * position
	 * 
	 * @param destination into which array the data will be written to
	 * @param pointer     the starting position at which we will insert our data
	 * @param source      from where we will take the data
	 * @return pointer position to the end of our data
	 */
	public static int copyBytes(byte[] destination, int pointer, short[] source) {
		assert (destination.length >= pointer + source.length);

		for (int i = 0; i < source.length; i++) {
			pointer = writeBytes(destination, pointer, source[i]);
		}
		return pointer;
	}

	/**
	 * Copies the bytes from 1 array to another using the pointer as a starting
	 * position
	 * 
	 * @param destination into which array the data will be written to
	 * @param pointer     the starting position at which we will insert our data
	 * @param source      from where we will take the data
	 * @return pointer position to the end of our data
	 */
	public static int copyBytes(byte[] destination, int pointer, boolean[] source) {
		assert (destination.length >= pointer + source.length);

		for (int i = 0; i < source.length; i++) {
			pointer = writeBytes(destination, pointer, source[i]);
		}
		return pointer;
	}

	/**
	 * Copies the bytes from 1 array to another using the pointer as a starting
	 * position
	 * 
	 * @param destination into which array the data will be written to
	 * @param pointer     the starting position at which we will insert our data
	 * @param source      from where we will take the data
	 * @return pointer position to the end of our data
	 */
	public static int copyBytes(byte[] destination, int pointer, int[] source) {
		assert (destination.length >= pointer + source.length);

		for (int i = 0; i < source.length; i++) {
			pointer = writeBytes(destination, pointer, source[i]);
		}
		return pointer;
	}

	/**
	 * Copies the bytes from 1 array to another using the pointer as a starting
	 * position
	 * 
	 * @param destination into which array the data will be written to
	 * @param pointer     the starting position at which we will insert our data
	 * @param source      from where we will take the data
	 * @return pointer position to the end of our data
	 */
	public static int copyBytes(byte[] destination, int pointer, float[] source) {
		assert (destination.length >= pointer + source.length);

		for (int i = 0; i < source.length; i++) {
			pointer = writeBytes(destination, pointer, source[i]);
		}
		return pointer;
	}

	/**
	 * Copies the bytes from 1 array to another using the pointer as a starting
	 * position
	 * 
	 * @param destination into which array the data will be written to
	 * @param pointer     the starting position at which we will insert our data
	 * @param source      from where we will take the data
	 * @return pointer position to the end of our data
	 */
	public static int copyBytes(byte[] destination, int pointer, double[] source) {
		assert (destination.length >= pointer + source.length);

		for (int i = 0; i < source.length; i++) {
			pointer = writeBytes(destination, pointer, source[i]);
		}
		return pointer;
	}

	/**
	 * Copies the bytes from 1 array to another using the pointer as a starting
	 * position
	 * 
	 * @param destination into which array the data will be written to
	 * @param pointer     the starting position at which we will insert our data
	 * @param source      from where we will take the data
	 * @return pointer position to the end of our data
	 */
	public static int copyBytes(byte[] destination, int pointer, long[] source) {
		assert (destination.length >= pointer + source.length);

		for (int i = 0; i < source.length; i++) {
			pointer = writeBytes(destination, pointer, source[i]);
		}
		return pointer;
	}

	/**
	 * Writes the given float value at the given pointer to the desination array
	 * 1 byte is 8 bits
	 * 
	 * @param destination the byte array to write to
	 * @param pointer     at which position the given value should be inserted to
	 * @param value       the value to insert
	 * @return pointer position to the end of our data
	 */
	public static int writeBytes(byte[] destination, int pointer, byte value) {
		assert (destination.length >= pointer + RCType.BYTE_SIZE);

		destination[pointer++] = value;
		return pointer;
	}

	/**
	 * Writes the given float value at the given pointer to the desination array
	 * short are 16 bit
	 * 
	 * @param destination the byte array to write to
	 * @param pointer     at which position the given value should be inserted to
	 * @param value       the value to insert
	 * @return pointer position to the end of our data
	 */
	public static int writeBytes(byte[] destination, int pointer, short value) {
		assert (destination.length >= pointer + RCType.SHORT_SIZE);

		destination[pointer++] = (byte) ((value >> 8) & 0xff);
		destination[pointer++] = (byte) ((value >> 0) & 0xff);

		return pointer;
	}

	/**
	 * Writes the given float value at the given pointer to the desination array
	 * Char is in java a unsinged short 16 bit
	 * 
	 * @param destination the byte array to write to
	 * @param pointer     at which position the given value should be inserted to
	 * @param value       the value to insert
	 * @return pointer position to the end of our data
	 */
	public static int writeBytes(byte[] destination, int pointer, char value) {
		assert (destination.length >= pointer + RCType.CHAR_SIZE);

		destination[pointer++] = (byte) ((value >> 8) & 0xff);
		destination[pointer++] = (byte) ((value >> 0) & 0xff);

		return pointer;
	}

	/**
	 * Writes the given float value at the given pointer to the desination array
	 * integers are 32 bit
	 * 
	 * @param destination the byte array to write to
	 * @param pointer     at which position the given value should be inserted to
	 * @param value       the value to insert
	 * @return pointer position to the end of our data
	 */
	public static int writeBytes(byte[] destination, int pointer, int value) {
		assert (destination.length >= pointer + RCType.INT_SIZE);

		destination[pointer++] = (byte) ((value >> 24) & 0xff);
		destination[pointer++] = (byte) ((value >> 16) & 0xff);
		destination[pointer++] = (byte) ((value >> 8) & 0xff);
		destination[pointer++] = (byte) ((value >> 0) & 0xff);

		return pointer;
	}

	/**
	 * Writes the given float value at the given pointer to the desination array
	 * long are 64 bit
	 * 
	 * @param destination the byte array to write to
	 * @param pointer     at which position the given value should be inserted to
	 * @param value       the value to insert
	 * @return pointer position to the end of our data
	 */
	public static int writeBytes(byte[] destination, int pointer, long value) {
		assert (destination.length >= pointer + RCType.LONG_SIZE);

		destination[pointer++] = (byte) ((value >> 56) & 0xff);
		destination[pointer++] = (byte) ((value >> 48) & 0xff);
		destination[pointer++] = (byte) ((value >> 40) & 0xff);
		destination[pointer++] = (byte) ((value >> 32) & 0xff);
		destination[pointer++] = (byte) ((value >> 24) & 0xff);
		destination[pointer++] = (byte) ((value >> 16) & 0xff);
		destination[pointer++] = (byte) ((value >> 8) & 0xff);
		destination[pointer++] = (byte) ((value >> 0) & 0xff);

		return pointer;
	}

	/**
	 * Writes the given float value at the given pointer to the desination array
	 * 
	 * @param destination the byte array to write to
	 * @param pointer     at which position the given value should be inserted to
	 * @param value       the value to insert
	 * @return pointer position to the end of our data
	 */
	public static int writeBytes(byte[] destination, int pointer, float value) {
		assert (destination.length >= pointer + RCType.FLOAT_SIZE);

		int data = Float.floatToIntBits(value);

		return writeBytes(destination, pointer, data);
	}

	public static int writeBytes(byte[] destination, int pointer, double value) {
		assert (destination.length >= pointer + RCType.DOUBLE_SIZE);

		long data = Double.doubleToLongBits(value);

		return writeBytes(destination, pointer, data);
	}

	/**
	 * Writes the given float value at the given pointer to the desination array
	 * Boolean = 1 Byte
	 * 
	 * @param destination the byte array to write to
	 * @param pointer     at which position the given value should be inserted to
	 * @param value       the value to insert
	 * @return pointer position to the end of our data
	 */
	public static int writeBytes(byte[] destination, int pointer, boolean value) {
		assert (destination.length >= pointer + RCType.BOOLEAN);

		destination[pointer++] = (byte) (value ? 1 : 0);

		return pointer;
	}

	/**
	 * Writes the given float value at the given pointer to the desination array
	 * Boolean = 1 Byte = 8 bit
	 * Trying to put multiple booleans into 1 Byte, since the actual data is only 1
	 * Bit
	 * TODO: test and rewrite for array works in theory
	 * 
	 * @param destination the byte array to write to
	 * @param pointer     at which position the given value should be inserted to
	 * @param value       the value to insert
	 * @return pointer position to the end of our data
	 */
	public static int writeBitfield(byte[] destination, int pointer, boolean[] values) {
		assert (destination.length >= pointer + RCType.BYTE_SIZE);

		byte bitfield = 0;
		int max = values.length >= 8 ? 8 : values.length - 1;

		for (int i = 0; i < max; i++) {
			bitfield |= (values[i] ? 1 : 0) << i;
		}
		destination[pointer++] = bitfield;

		return pointer;
	}

	/**
	 * 2 options:
	 * 1. Write the size of the string before the string as a short ( 07 NI VI SA
	 * bytes of 2 for size so we are not limited to 255 characters but have
	 * plenty of space
	 * 2. Null-termination character (binary 0) at the end: (NI VI SA N0).
	 * it is not the same as the character 0
	 * 
	 * @param destination the byte array to write to
	 * @param pointer     at which position the given value should be inserted to
	 * @param value       the value to insert
	 * @return pointer to next location
	 */
	public static int writeBytes(byte[] destination, int pointer, String value) {
		assert (destination.length >= pointer + (short) value.length());

		pointer = writeBytes(destination, pointer, (short) value.length());
		return copyBytes(destination, pointer, value.getBytes());
	}

}
