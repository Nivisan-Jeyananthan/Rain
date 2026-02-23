package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class IntegerArray extends Array{

	
	public IntegerArray(String name, int[] values) {
		super(name, Type.INT, values.length);
		SerializationWriter.copyBytes(data, 0,values);
	}
	

}
