package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class BooleanField extends RCField {
    public BooleanField(String name, boolean value) {
        super(name,RCType.BOOLEAN);
        SerializationWriter.writeBytes(data, 0, value);
    }
}
