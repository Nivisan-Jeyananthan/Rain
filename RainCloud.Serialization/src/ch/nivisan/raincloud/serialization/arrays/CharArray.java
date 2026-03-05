package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class CharArray extends RCArray {

	public CharArray(String name, char[] values) {
		super(name, RCType.CHAR, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

	public char[] getValues() {
		return SerializationReader.readCharArray(data, 0, elementCount);
	}
}
