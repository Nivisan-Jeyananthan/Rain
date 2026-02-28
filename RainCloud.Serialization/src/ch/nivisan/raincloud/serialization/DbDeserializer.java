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

        for (int i = 0; i < objectCount; i++) {
            RCObject currenObject = getObject(data, pointer);
            database.addObject(currenObject);
            pointer += currenObject.getSize();
        }

        return database;
    }

    private static RCObject getObject(byte[] data, int pointer) {
        byte containerType = data[pointer++];
        assert (containerType == ContainerType.Object);

        short objNameLength = SerializationReader.readShort(data, pointer);
        pointer += RCType.SHORT_SIZE;

        String objName = SerializationReader.readString(data, pointer, objNameLength);
        pointer += objNameLength;

        RCObject obj = new RCObject(objName);

        short fieldCount = SerializationReader.readShort(data, pointer);
        pointer += RCType.SHORT_SIZE;
        for (int i = 0; i < fieldCount; i++) {
            RCField currentField = getField(data, pointer);
            obj.addField(currentField);
            pointer += currentField.getSize();
        }

        short stringCount = SerializationReader.readShort(data, pointer);
        pointer += RCType.SHORT_SIZE;
        for (int i = 0; i < stringCount; i++) {
            RCString currentString = getString(data, pointer);
            obj.addString(currentString);
            pointer += currentString.getSize();
        }

        short arrayCount = SerializationReader.readShort(data, pointer);
        pointer += RCType.SHORT_SIZE;
        for (int i = 0; i < arrayCount; i++) {
            RCArray currentArray = getArray(data, pointer);
            obj.addArray(currentArray);
            pointer += currentArray.getSize();
        }

        return obj;
    }

    private static RCField getField(byte[] data, int pointer) {
        byte containerType = data[pointer++];
        assert (containerType == ContainerType.Field);

        short objNameLength = SerializationReader.readShort(data, pointer);
        pointer += RCType.SHORT_SIZE;

        String objName = SerializationReader.readString(data, pointer, objNameLength);
        pointer += objNameLength;

        byte fieldType = data[pointer++];
        RCType.getSize(fieldType);

        return null;
    }

    private static RCString getString(byte[] data, int pointer) {
        return null;
    }

    private static RCArray getArray(byte[] data, int pointer) {
        return null;
    }
}
