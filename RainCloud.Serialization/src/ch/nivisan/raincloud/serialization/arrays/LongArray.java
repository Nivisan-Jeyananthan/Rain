package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class LongArray extends RCArray {

	public LongArray(String name, long[] values) {
		super(name, RCType.LONG, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

    public long[] getValues() {
        return SerializationReader.readLongArray(data, 0, elementCount);
    }
}
