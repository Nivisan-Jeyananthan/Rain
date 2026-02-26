package ch.nivisan.raincloud.serialization;

public class RCString extends Container {
    private byte[] data;
    private short count;
    private int size;

    public RCString(String name, String data) {
        super(ContainerType.StringType, name);
        this.data = data.getBytes();
        this.count = (short) data.length();
        size = super.getSize() + data.length() + RCType.INT_SIZE + RCType.SHORT_SIZE;
    }

    @Override
    public int getBytes(byte[] destination, int pointer) {
        pointer = super.getBytes(destination, pointer);

        pointer = SerializationWriter.writeBytes(destination, pointer, size);
        pointer = SerializationWriter.writeBytes(destination, pointer, count);
        pointer = SerializationWriter.copyBytes(destination, pointer, data);
        return pointer;
    }

    @Override
    public int getSize() {
        return size;
    }
}
