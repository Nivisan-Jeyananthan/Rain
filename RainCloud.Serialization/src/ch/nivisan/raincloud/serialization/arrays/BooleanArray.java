package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class BooleanArray extends RCArray {

	public BooleanArray(String name, boolean[] values) {
		super(name, RCType.BOOLEAN, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

	public boolean[] getValues() {
		return SerializationReader.readBoolArray(data, 0, elementCount);
	}
}
