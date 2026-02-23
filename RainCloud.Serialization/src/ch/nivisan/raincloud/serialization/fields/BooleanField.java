package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class BooleanField extends Field {
    public BooleanField(String name, boolean value) {
        super(name,Type.BOOLEAN);
        SerializationWriter.writeBytes(data, 0, value);
    }
}
