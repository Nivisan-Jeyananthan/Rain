package ch.nivisan.raincloud.serialization;

import java.util.ArrayList;
import java.util.List;

public class RCDatabase extends Container {
    private static final byte[] header = "RCDB".getBytes();
    private int size;
    private short objectCount;
    private List<RCObject> objects = new ArrayList<RCObject>();

    public RCDatabase(String name) {
        super(ContainerType.Database, name);
        size = super.getSize();
        size += header.length + RCType.INT_SIZE + RCType.SHORT_SIZE;
    }

    public void addObject(RCObject object) {
        objects.add(object);
        size += object.getSize();

        objectCount = (short) objects.size();
    }

    /**
     * 3 bytes (containertype + namelength) + nameLength in bytes +
     * 4 bytes (size) + 2 bytes (objectCount) + objects in bytes
     */
    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getBytes(byte[] destination, int pointer) {
        pointer = SerializationWriter.copyBytes(destination, pointer, header);
        pointer = super.getBytes(destination, pointer);

        pointer = SerializationWriter.writeBytes(destination, pointer, size);

        pointer = SerializationWriter.writeBytes(destination, pointer, objectCount);
        for (RCObject currnetObject : objects)
            pointer = currnetObject.getBytes(destination, pointer);
        return pointer;
    }

}
