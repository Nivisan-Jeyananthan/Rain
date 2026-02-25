package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class DoubleField extends RCField {
    public DoubleField(String name, double value) {
        super(name,RCType.DOUBLE);
        SerializationWriter.writeBytes(data, 0, value);
    }
}
