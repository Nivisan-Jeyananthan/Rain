package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class IntField extends RCField {
    public IntField(String name, int value) {
        super(name,RCType.INT);
        SerializationWriter.writeBytes(data, 0, value);
    }
}
