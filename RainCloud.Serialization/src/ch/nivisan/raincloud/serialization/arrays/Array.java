package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.Container;
import ch.nivisan.raincloud.serialization.ContainerType;
import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.Type;

public class Array extends Container {
    public final int elementCount;

    public Array(String name, byte type, int elements) {
        super(ContainerType.Array, name, type, elements * Type.getSize(type));
        elementCount = elements;
    }

    /**
     * Bitfield usage
     * @param name
     * @param type
     * @param elements
     * @param elementCount
     */
    public Array(String name, byte type, int elements, int elementCount) {
        super(ContainerType.Array, name, type, elements * Type.getSize(type));
        this.elementCount = elementCount;
    }

    @Override
    public int getBytes(byte[] destination, int pointer) {
        pointer = super.getBytes(destination,pointer);
        pointer = SerializationWriter.writeBytes(destination, pointer, elementCount);
        pointer = SerializationWriter.copyBytes(destination, pointer, data);

        return pointer;
    }

    @Override
    public int getSize() {
        return Type.BYTE_SIZE + Type.SHORT_SIZE + nameLength + Type.BYTE + Type.INT
                + elementCount * Type.getSize(dataType);
    }
}