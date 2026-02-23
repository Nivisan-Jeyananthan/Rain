package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class ShortField extends Field {
    public ShortField(String name, short value) {
        super(name,Type.SHORT);
        SerializationWriter.writeBytes(data, 0, value);
    }
}