package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class LongField extends RCField {
    public LongField(String name, long value) {
        super(name,RCType.LONG);
        SerializationWriter.writeBytes(data, 0, value);
    }
}
