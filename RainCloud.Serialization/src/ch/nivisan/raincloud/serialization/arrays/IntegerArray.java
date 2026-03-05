package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class IntegerArray extends RCArray {

	public IntegerArray(String name, int[] values) {
		super(name, RCType.INT, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

	public int[] getValues() {
		return SerializationReader.readIntArray(data, 0, elementCount);
	}
}
