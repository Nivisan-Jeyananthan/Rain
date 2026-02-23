package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class BooleanArray extends Array {

	public BooleanArray(String name, Boolean[] values) {
		super(name, Type.BOOLEAN, values.length * Type.BOOLEAN_SIZE);
		SerializationWriter.copyBytes(data, containerType, data);
	}

}
