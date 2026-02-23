package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

// TODO: revise implementation of bitfield after finishing file implementation
public class Bitfield extends Array {

	public Bitfield(String name, boolean[] values) {
		super(name, Type.BITFIELD, values.length * Type.BOOLEAN_SIZE);
		
		 SerializationWriter.writeBitfield(data, 0, values);
	}

}
