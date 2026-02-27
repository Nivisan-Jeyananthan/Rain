package ch.nivisan.raincloud.serialization;

/**
 * Same as CharArray in some sense without need of a char array as input
 * change from 1 byte per character to 2 because of characters out of that range.
 * Nonstandard characters such as : é,ö,ü
 */
public class RCString extends Container {
    private byte[] data;
    private short characterCount;
    private int size;

    public RCString(String name, String value) {
        super(ContainerType.StringType, name);
        size = super.getSize() + data.length + RCType.INT_SIZE + RCType.SHORT_SIZE;
        this.characterCount = (short) value.length();

        this.data = new byte[value.length() * RCType.SHORT_SIZE];
        SerializationWriter.copyBytes(data, 0, value.toCharArray());
    }

    @Override
    public int getBytes(byte[] destination, int pointer) {
        pointer = super.getBytes(destination, pointer);

        pointer = SerializationWriter.writeBytes(destination, pointer, size);
        pointer = SerializationWriter.writeBytes(destination, pointer, characterCount);
        pointer = SerializationWriter.copyBytes(destination, pointer, data);
        return pointer;
    }

    @Override
    public int getSize() {
        return size;
    }
}
