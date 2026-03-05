package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class DoubleField extends RCField {
    public DoubleField(String name, double value) {
        super(name,RCType.DOUBLE);
        SerializationWriter.writeBytes(data, 0, value);
    }

    public double getValue() {
        return SerializationReader.readDouble(data, 0);
    }
}
