package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class FloatArray extends Array {

	public FloatArray(String name, float[] values) {
		super(name, Type.FLOAT, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
