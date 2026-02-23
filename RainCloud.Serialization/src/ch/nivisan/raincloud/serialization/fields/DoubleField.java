package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class DoubleField extends Field {
    public DoubleField(String name, double value) {
        super(name,Type.DOUBLE);
        SerializationWriter.writeBytes(data, 0, value);
    }
}
