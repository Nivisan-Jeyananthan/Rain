package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class FloatField extends Field {
    public FloatField(String name, float value) {
        super(name,Type.FLOAT);
        SerializationWriter.writeBytes(data, 0, value);
    }
}