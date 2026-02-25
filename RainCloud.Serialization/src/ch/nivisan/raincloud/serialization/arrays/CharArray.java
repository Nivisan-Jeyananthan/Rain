package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class CharArray extends RCArray {

	public CharArray(String name, char[] values) {
		super(name, RCType.CHAR, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
