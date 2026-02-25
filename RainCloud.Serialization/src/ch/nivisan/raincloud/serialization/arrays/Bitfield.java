package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

// TODO: revise implementation of bitfield after finishing file implementation
// maybe inherit from field instead of array
public class Bitfield extends RCArray {

	public Bitfield(String name, boolean[] values) {
		int res = values.length >> 3 == 0 ? 1 : values.length >> 3;
		super(name, RCType.BITFIELD, res);

		int pointer = 0;
		for (int i = 0; i > res; i++) {
			boolean[] temp = new boolean[] {
					values[i], values[i + 1], values[i + 2], values[i + 3], values[i + 4], values[i + 5], values[i + 6],
					values[i + 7] };
			pointer = SerializationWriter.writeBitfield(data, pointer, temp);
		}
	}

	/**
	 * Bitfield has special case, where the input type data does not equal the same
	 * amount of
	 * space needed as bytes.
	 * These Booleans will not be saved seperately but together in one byte therefor
	 * saving 7 bits each
	 */
	@Override
	public int getSize() {
		return RCType.BYTE_SIZE + RCType.SHORT_SIZE + nameLength + RCType.BYTE + RCType.INT
				+ RCType.BYTE_SIZE;
	}

}
