package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class ByteField extends RCField {
    public ByteField(String name, byte value) {
        super(name, RCType.BYTE);
        SerializationWriter.writeBytes(data, 0, value);
    }

    public byte getValue() {
        return SerializationReader.readByte(data, 0);
    }
}
