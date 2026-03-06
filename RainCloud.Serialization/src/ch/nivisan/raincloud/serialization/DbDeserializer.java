package ch.nivisan.raincloud.serialization;

import ch.nivisan.raincloud.serialization.arrays.Bitfield;
import ch.nivisan.raincloud.serialization.arrays.BooleanArray;
import ch.nivisan.raincloud.serialization.arrays.ByteArray;
import ch.nivisan.raincloud.serialization.arrays.CharArray;
import ch.nivisan.raincloud.serialization.arrays.DoubleArray;
import ch.nivisan.raincloud.serialization.arrays.FloatArray;
import ch.nivisan.raincloud.serialization.arrays.IntegerArray;
import ch.nivisan.raincloud.serialization.arrays.LongArray;
import ch.nivisan.raincloud.serialization.arrays.RCArray;
import ch.nivisan.raincloud.serialization.arrays.ShortArray;
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

        short version = SerializationReader.readShort(data, pointer);
        pointer += RCType.SHORT_SIZE;

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

        if (objectCount == 0)
            return database;

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
        if (fieldCount != 0)
            for (int i = 0; i < fieldCount; i++) {
                RCField currentField = getField(data, pointer);
                obj.addField(currentField);
            }

        short stringCount = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;
        if (stringCount != 0)
            for (int i = 0; i < stringCount; i++) {
                RCString currentString = getRCString(data, pointer);
                obj.addString(currentString);
            }

        short arrayCount = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;

        if (arrayCount != 0)
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

        RCField field;

        switch (fieldType) {
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
                pointer[0] += RCType.BOOLEAN_SIZE;
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
                field = new Bitfield(fieldName, SerializationReader.readBitField(data, pointer[0]));
                pointer[0] += RCType.BITFIELD_SIZE;
                break;
            default:
                field = null;
                break;
        }

        return field;
    }

    private static RCString getRCString(byte[] data, int[] pointer) {
        byte containerType = data[pointer[0]++];
        assert (containerType == ContainerType.StringType);

        short nameLength = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;

        String name = SerializationReader.readString(data, pointer[0], nameLength);
        pointer[0] += nameLength;

        int size = SerializationReader.readInt(data, pointer[0]);
        pointer[0] += RCType.INT_SIZE;

        short characterCount = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;

        var characters = SerializationReader.readString(data, pointer[0], characterCount);
        pointer[0] += characterCount;
        assert (characterCount == characters.length());

        var str = new String(characters);
        return new RCString(name, str);
    }

    private static RCArray getArray(byte[] data, int[] pointer) {
        byte containerType = data[pointer[0]++];
        assert (containerType == ContainerType.Object);

        short nameLength = SerializationReader.readShort(data, pointer[0]);
        pointer[0] += RCType.SHORT_SIZE;

        String name = SerializationReader.readString(data, pointer[0], nameLength);
        pointer[0] += nameLength;

        byte dataType = data[pointer[0]++];

        int elementsCount = SerializationReader.readInt(data, pointer[0]);
        pointer[0] += RCType.INT_SIZE;

        RCArray array;

        switch (dataType) {
            case RCType.BYTE:
                array = new ByteArray(name, SerializationReader.readBytes(data, pointer, elementsCount));
                break;
            case RCType.SHORT:
                array = new ShortArray(name, SerializationReader.readShortArray(data, pointer, elementsCount));
                break;
            case RCType.CHAR:
                array = new CharArray(name, SerializationReader.readCharArray(data, pointer, elementsCount));
                break;
            case RCType.BOOLEAN:
                array = new BooleanArray(name, SerializationReader.readBoolArray(data, pointer, elementsCount));
                break;
            case RCType.INT:
                array = new IntegerArray(name, SerializationReader.readIntArray(data, pointer, elementsCount));
                break;
            case RCType.LONG:
                array = new LongArray(name, SerializationReader.readLongArray(data, pointer, elementsCount));
                break;
            case RCType.FLOAT:
                array = new FloatArray(name, SerializationReader.readFloatArray(data, pointer, elementsCount));
                break;
            case RCType.DOUBLE:
                array = new DoubleArray(name, SerializationReader.readDoubleArray(data, pointer, elementsCount));
                break;
            default:
                array = null;
                break;
        }
        return array;
    }
}
