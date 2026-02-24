package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class BooleanArray extends Array {

	public BooleanArray(String name, boolean[] values) {
		super(name, Type.BOOLEAN, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
