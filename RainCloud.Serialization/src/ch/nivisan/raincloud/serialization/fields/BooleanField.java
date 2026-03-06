package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class BooleanField extends RCField {
    public BooleanField(String name, boolean value) {
        super(name, RCType.BOOLEAN);
        SerializationWriter.writeBytes(data, 0, value);
    }

    public boolean getValue() {
        return SerializationReader.readBoolean(data, 0);
    }
}
