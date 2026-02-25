package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class FloatField extends RCField {
    public FloatField(String name, float value) {
        super(name,RCType.FLOAT);
        SerializationWriter.writeBytes(data, 0, value);
    }
}