package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class ByteArray extends Array {

	public ByteArray(String name, byte[] values) {
		super(name, Type.BYTE, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

}
