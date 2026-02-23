package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class DoubleArray extends Array {

	public DoubleArray(String name, double[] values) {
		super(name, Type.DOUBLE, values.length * Type.DOUBLE_SIZE);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
