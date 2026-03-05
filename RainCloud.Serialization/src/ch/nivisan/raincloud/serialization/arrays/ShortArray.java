package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class ShortArray extends RCArray {

	public ShortArray(String name, short[] values) {
		super(name, RCType.SHORT, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

    public short[] getValues() {
        return SerializationReader.readShortArray(data, 0, elementCount);
    }
}
