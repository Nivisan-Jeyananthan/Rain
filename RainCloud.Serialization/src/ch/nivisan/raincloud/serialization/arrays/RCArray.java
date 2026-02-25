package ch.nivisan.raincloud.serialization.arrays;

import ch.nivisan.raincloud.serialization.ContainerType;
import ch.nivisan.raincloud.serialization.DataContainer;
import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.RCType;

public class RCArray extends DataContainer {
    public final int elementCount;

    public RCArray(String name, byte type, int elements) {
        super(ContainerType.Array, name, type, elements * RCType.getSize(type));
        elementCount = elements;
    }

    /**
     * Bitfield usage
     * @param name
     * @param type
     * @param elements
     * @param elementCount
     */
    public RCArray(String name, byte type, int elements, int elementCount) {
        super(ContainerType.Array, name, type, elements * RCType.getSize(type));
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
        return RCType.BYTE_SIZE + RCType.SHORT_SIZE + nameLength + RCType.BYTE + RCType.INT
                + elementCount * RCType.getSize(dataType);
    }
}