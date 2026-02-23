package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class CharField extends Field {
    public CharField(String name, char value) {
        super(name,Type.CHAR);
        SerializationWriter.writeBytes(data, 0, value);
    }
}