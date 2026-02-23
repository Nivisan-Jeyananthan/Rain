package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class LongArray extends Array {

	public LongArray(String name, long[] values) {
		super(name, Type.LONG, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
