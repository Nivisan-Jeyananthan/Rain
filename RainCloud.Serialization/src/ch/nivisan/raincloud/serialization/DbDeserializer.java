package ch.nivisan.raincloud.serialization;

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
            byte objContainer = SerializationReader.readByte(data, pointer++);
            assert (containerType == ContainerType.Object);

            short objNameLength = SerializationReader.readShort(data, pointer);
            pointer += RCType.SHORT_SIZE;

            String objName = SerializationReader.readString(data, pointer, objNameLength);
            pointer += nameLength;

            database.addObject(new RCObject(objName));
        }

        return database;
    }

}
