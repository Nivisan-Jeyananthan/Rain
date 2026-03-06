package ch.nivisan.raincloud.serialization;

/**
 * Same as CharArray in some sense without need of a char array as input
 * change from 1 byte per character to 2 because of characters out of that
 * range.
 * Nonstandard characters such as : é,ö,ü
 */
public class RCString extends Container {
    private final byte[] data;
    private final short characterCount;
    private final int size;

    public RCString(String name, String value) {
        super(ContainerType.StringType, name);
        char[] valueArray = value.toCharArray();
        size = super.getSize() + (value.length() * RCType.CHAR_SIZE) + RCType.INT_SIZE + RCType.SHORT_SIZE;
        this.characterCount = (short) (valueArray.length * RCType.CHAR_SIZE);

        this.data = new byte[characterCount];
        SerializationWriter.copyBytes(data, 0, valueArray);
    }

    public String getValue() {
        return SerializationReader.readString(data, 0, characterCount);
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
