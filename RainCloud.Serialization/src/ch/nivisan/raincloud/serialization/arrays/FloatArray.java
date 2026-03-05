package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class FloatArray extends RCArray {

	public FloatArray(String name, float[] values) {
		super(name, RCType.FLOAT, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

    public float[] getValues() {
        return SerializationReader.readFloatArray(data, 0, elementCount);
    }
}
