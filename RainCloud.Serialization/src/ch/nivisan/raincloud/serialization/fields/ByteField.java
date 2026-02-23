package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class ByteField extends Field {
    public ByteField(String name, byte value) {
        super(name,Type.BYTE);
        SerializationWriter.writeBytes(data, 0, value);
    }
}
