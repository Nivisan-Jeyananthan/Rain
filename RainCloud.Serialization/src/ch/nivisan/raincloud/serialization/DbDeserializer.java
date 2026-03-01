package ch.nivisan.raincloud.serialization;

import ch.nivisan.raincloud.serialization.arrays.RCArray;
import ch.nivisan.raincloud.serialization.fields.IntField;
import ch.nivisan.raincloud.serialization.fields.RCField;

public class DbDeserializer {
    private static RCDatabase database;

    private DbDeserializer() {
    }

    public static RCDatabase Deserialize(byte[] data) {
        int pointer = 0;
        String header = SerializationReader.readString(data, pointer, RCDatabase.header.length);
        assert (header.equals(RCDatabase.header));
        pointer += RCDatabase.header.length;

        byte containerType = SerializationReader.readByte(data, pointer++);
        assert (containerType == ContainerType.Database);

        short nameLength = SerializationReader.readShort(data, pointer);
        pointer += RCType.SHORT_SIZE;

        String name = SerializationReader.readString(data, pointer, nameLength);
        pointer += nameLength;

        int size = SerializationReader.readInt(data, pointer);
        pointer += RCType.INT_SIZE;

        short objectCount = SerializationReader.readShort(data, pointer);
        pointer += RCType.SHORT_SIZE;

        database = new RCDatabase(name);
        var pointerArray = new int[] { pointer };
        for (int i = 0; i < objectCount; i++) {
            RCObject currenObject = getObject(data, pointerArray);
            database.addObject(currenObject);
        }

        return database;
    }

    private static RCObject getObject(byte[] data, int[] pointer) {
        byte containerType = data[pointer[0]++];
        assert (containerType == ContainerType.Object);

        short objNameLength = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;

        String objName = SerializationReader.readString(data, pointer[0], objNameLength);
        pointer[0] += objNameLength;

        int objSize = SerializationReader.readInt(data, pointer[0]);
        pointer[0] += RCType.INT_SIZE;

        RCObject obj = new RCObject(objName);
       
        short fieldCount = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;
        for (int i = 0; i < fieldCount; i++) {
            RCField currentField = getField(data, pointer);
            obj.addField(currentField);
        }

        short stringCount = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;
        for (int i = 0; i < stringCount; i++) {
            RCString currentString = getString(data, pointer);
            obj.addString(currentString);
        }

        short arrayCount = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;
        for (int i = 0; i < arrayCount; i++) {
            RCArray currentArray = getArray(data, pointer);
            obj.addArray(currentArray);
        }

        return obj;
    }

    private static RCField getField(byte[] data, int[] pointer) {
        byte containerType = data[pointer[0]++];
        assert (containerType == ContainerType.Field);

        short objNameLength = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;

        String objName = SerializationReader.readString(data, pointer[0], objNameLength);
        pointer[0] += objNameLength;

        byte fieldType = data[pointer[0]++];
        byte field = RCType.getSize(fieldType);

        // TODO: create specific field based on type, switch.

        return null;
    }

    private static RCString getString(byte[] data, int[] pointer) {
        return null;
    }

    private static RCArray getArray(byte[] data, int[] pointer) {
        return null;
    }
}
