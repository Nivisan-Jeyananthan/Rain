package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class LongField extends Field {
    public LongField(String name, long value) {
        super(name,Type.LONG);
        SerializationWriter.writeBytes(data, 0, value);
    }
}
