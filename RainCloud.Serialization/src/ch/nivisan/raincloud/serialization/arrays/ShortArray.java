package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class ShortArray extends Array {

	public ShortArray(String name, short[] values) {
		super(name, Type.SHORT, values.length * Type.SHORT_SIZE);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
