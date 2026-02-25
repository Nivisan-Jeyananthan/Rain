package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class ShortField extends RCField {
    public ShortField(String name, short value) {
        super(name,RCType.SHORT);
        SerializationWriter.writeBytes(data, 0, value);
    }
}