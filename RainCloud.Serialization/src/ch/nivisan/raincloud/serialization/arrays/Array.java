package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.Container;
import ch.nivisan.raincloud.serialization.ContainerType;
import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class Array extends Container{
    public final int elementCount;    
    
    public Array(String name, byte type, int elements) {
    	super(ContainerType.Array, name, type, elements * Type.getSize(type));
    	elementCount = elements;
    }
    
    @Override
    public int getBytes(byte[] destination, int pointer) {
        destination[pointer++] = containerType;
        pointer = SerializationWriter.writeBytes(destination, pointer, nameLength);
        pointer = SerializationWriter.copyBytes(destination, pointer, name);
        pointer = SerializationWriter.writeBytes(destination, pointer, dataType);
        pointer = SerializationWriter.writeBytes(destination, pointer, elementCount);
        pointer = SerializationWriter.copyBytes(destination, pointer, data);

        return pointer;
    }
}
