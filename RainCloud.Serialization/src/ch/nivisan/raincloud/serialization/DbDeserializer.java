package ch.nivisan.raincloud.serialization;

import ch.nivisan.raincloud.serialization.arrays.Bitfield;
import ch.nivisan.raincloud.serialization.arrays.RCArray;
import ch.nivisan.raincloud.serialization.fields.BooleanField;
import ch.nivisan.raincloud.serialization.fields.ByteField;
import ch.nivisan.raincloud.serialization.fields.CharField;
import ch.nivisan.raincloud.serialization.fields.DoubleField;
import ch.nivisan.raincloud.serialization.fields.FloatField;
import ch.nivisan.raincloud.serialization.fields.IntField;
import ch.nivisan.raincloud.serialization.fields.LongField;
import ch.nivisan.raincloud.serialization.fields.RCField;
import ch.nivisan.raincloud.serialization.fields.ShortField;

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

        short fieldNameLength = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;

        String fieldName = SerializationReader.readString(data, pointer[0], fieldNameLength);
        pointer[0] += fieldNameLength;

        byte fieldType = data[pointer[0]++];

        byte fieldValue = RCType.getSize(fieldType);
        RCField field;

        switch (fieldValue) {
            case RCType.BYTE:
                field = new ByteField(fieldName, data[pointer[0]++]);
                break;
            case RCType.SHORT:
                field = new ShortField(fieldName, SerializationReader.readShort(data, pointer[0]));
                pointer[0] += RCType.SHORT_SIZE;
                break;
            case RCType.CHAR:
                field = new CharField(fieldName, SerializationReader.readChar(data, pointer[0]));
                pointer[0] += RCType.CHAR_SIZE;
                break;
            case RCType.BOOLEAN:
                field = new BooleanField(fieldName, SerializationReader.readBoolean(data, pointer[0]));
                pointer[0] += RCType.BOOLEAN;
                break;
            case RCType.INT:
                field = new IntField(fieldName, SerializationReader.readInt(data, pointer[0]));
                pointer[0] += RCType.INT_SIZE;
                break;
            case RCType.LONG:
                field = new LongField(fieldName, SerializationReader.readLong(data, pointer[0]));
                pointer[0] += RCType.LONG_SIZE;
                break;
            case RCType.FLOAT:
                field = new FloatField(fieldName, SerializationReader.readFloat(data, pointer[0]));
                pointer[0] += RCType.FLOAT_SIZE;
                break;
            case RCType.DOUBLE:
                field = new DoubleField(fieldName, SerializationReader.readDouble(data, pointer[0]));
                pointer[0] += RCType.DOUBLE_SIZE;
                break;
            case RCType.BITFIELD:
                field = new Bitfield(fieldName, SerializationReader.readBitfield(data, pointer[0]));
                pointer[0] += RCType.BITFIELD;
                break;
            default:
                byte[] fieldData = new byte[fieldValue];
                pointer[0] = SerializationReader.readBytes(data, pointer[0], fieldData);
                field = null;
                break;
        }

        return field;
    }

    private static RCString getString(byte[] data, int[] pointer) {
        return null;
    }

    private static RCArray getArray(byte[] data, int[] pointer) {
        return null;
    }
}
