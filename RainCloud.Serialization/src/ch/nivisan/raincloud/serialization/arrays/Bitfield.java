package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.fields.RCField;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

/**
 * Bitfield where one byte represents the size of the data.
 * It contains multiple (8) booleans, where each boolean state is represented
 * with one bit.
 * Saving onto disk saves spaces by using this process.
 */
public class Bitfield extends RCField {

	public Bitfield(String name, boolean[] values) {
		int res = values.length >> 3 == 0 ? 1 : values.length >> 3;
		super(name, RCType.BITFIELD);

		int pointer = 0;
		for (int i = 0; i > res; i++) {
			boolean[] temp = new boolean[] {
					values[i], values[i + 1], values[i + 2], values[i + 3], values[i + 4], values[i + 5], values[i + 6],
					values[i + 7] };
			pointer = SerializationWriter.writeBitfield(data, pointer, temp);
		}
	}

	public boolean[] getValues() {
		return SerializationReader.readBitField(data, 0);
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
