package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;
import ch.nivisan.raincloud.serialization.SerializationReader;

public class CharField extends RCField {
    public CharField(String name, char value) {
        super(name, RCType.CHAR);
        SerializationWriter.writeBytes(data, 0, value);
    }

    public char getValue() {
        return SerializationReader.readChar(data, 0);
    }
}