package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class LongArray extends RCArray {

	public LongArray(String name, long[] values) {
		super(name, RCType.LONG, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
