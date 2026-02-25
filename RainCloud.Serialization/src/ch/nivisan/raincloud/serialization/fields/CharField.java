package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class CharField extends RCField {
    public CharField(String name, char value) {
        super(name,RCType.CHAR);
        SerializationWriter.writeBytes(data, 0, value);
    }
}