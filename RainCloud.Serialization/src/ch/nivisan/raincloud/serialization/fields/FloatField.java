package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class FloatField extends RCField {
    public FloatField(String name, float value) {
        super(name,RCType.FLOAT);
        SerializationWriter.writeBytes(data, 0, value);
    }

  public float getValue() {
        return SerializationReader.readFloat(data, 0);
    }
}