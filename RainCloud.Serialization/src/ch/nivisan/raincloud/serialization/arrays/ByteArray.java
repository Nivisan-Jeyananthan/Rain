package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class ByteArray extends RCArray {

	public ByteArray(String name, byte[] values) {
		super(name, RCType.BYTE, values.length);
		SerializationWriter.copyBytes(data, 0, values);
	}

	public byte[] getValues() {
		return SerializationReader.readBytes(data, 0, elementCount);
	}
}
