package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class DoubleArray extends RCArray {

	public DoubleArray(String name, double[] values) {
		super(name, RCType.DOUBLE, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
