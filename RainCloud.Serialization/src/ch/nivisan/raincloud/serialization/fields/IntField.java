package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class IntField extends Field {
    public IntField(String name, int value) {
        super(name,Type.INT);
        SerializationWriter.writeBytes(data, 0, value);
    }
}
