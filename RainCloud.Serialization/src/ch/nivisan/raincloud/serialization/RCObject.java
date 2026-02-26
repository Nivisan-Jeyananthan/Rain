package ch.nivisan.raincloud.serialization;

import java.util.ArrayList;
import java.util.List;

import ch.nivisan.raincloud.serialization.arrays.RCArray;
import ch.nivisan.raincloud.serialization.fields.RCField;

public class RCObject extends Container {
    private int size;
    private short fieldCount;
    private short arrayCount;
    private short stringCount;
    private List<RCString> strings = new ArrayList<RCString>();
    private List<RCField> fields = new ArrayList<RCField>();
    private List<RCArray> arrays = new ArrayList<RCArray>();

    public RCObject(String name) {
        super(ContainerType.Object, name);
        size = super.getSize();
    }

    public void addArray(RCArray array) {
        arrays.add(array);
        size += array.getSize();

        arrayCount = (short) arrays.size();
    }

    public void addField(RCField field) {
        fields.add(field);
        size += field.getSize();

        fieldCount = (short) fields.size();
    }

    public void addString(RCString value) {
        strings.add(value);
        size += value.getSize();

        stringCount = (short) fields.size();
    }

    /**
     * 3 bytes (containertype + namelength) + nameLength in bytes + fieldCount
     * (2 bytes) + arrayCount (2 bytes) + stringCount (2 bytes) + (fields) + (arrays)
     */
    @Override
    public int getSize() {
        return size + RCType.SHORT_SIZE + RCType.SHORT_SIZE + RCType.SHORT_SIZE;

    }

    @Override
    public int getBytes(byte[] destination, int pointer) {
        pointer = super.getBytes(destination, pointer);

        pointer = SerializationWriter.writeBytes(destination, pointer, stringCount);
        for (RCString string : strings)
            pointer = string.getBytes(destination, pointer);

        pointer = SerializationWriter.writeBytes(destination, pointer, fieldCount);
        for (RCField field : fields)
            pointer = field.getBytes(destination, pointer);

        pointer = SerializationWriter.writeBytes(destination, pointer, arrayCount);
        for (RCArray currentArray : arrays)
            pointer = currentArray.getBytes(destination, pointer);
        return pointer;
    }
}
