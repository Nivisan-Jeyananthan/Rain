package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class CharArray extends Array {

	public CharArray(String name, char[] values) {
		super(name, Type.CHAR, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
