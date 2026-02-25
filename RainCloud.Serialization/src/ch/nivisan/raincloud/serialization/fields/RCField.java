package ch.nivisan.raincloud.serialization.fields;

import ch.nivisan.raincloud.serialization.ContainerType;
import ch.nivisan.raincloud.serialization.DataContainer;
import ch.nivisan.raincloud.serialization.SerializationWriter;

/**
 * Stream of bytes
 */
public abstract class RCField extends DataContainer {
    public RCField(String name, byte type) {
        super(ContainerType.Field, name, type);
    }

    @Override
    public int getBytes(byte[] destination, int pointer) {
        pointer = super.getBytes(destination, pointer);
        pointer = SerializationWriter.copyBytes(destination, pointer, data);

        return pointer;
    }
}
