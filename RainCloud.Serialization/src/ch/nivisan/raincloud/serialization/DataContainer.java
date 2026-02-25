package ch.nivisan.raincloud.serialization;

public class DataContainer extends Container {
	public final byte dataType;
	public final byte[] data;

	public DataContainer(byte containerType, String name, byte type) {
		super(containerType, name);
		data = new byte[RCType.getSize(type)];
		dataType = type;
	}

	public DataContainer(byte containerType, String name, byte type, int dataLength) {
		super(containerType, name);
		this.data = new byte[dataLength];
		dataType = type;
	}

	@Override
	public int getBytes(byte[] destination, int pointer) {
		pointer = super.getBytes(destination, pointer);
		pointer = SerializationWriter.writeBytes(destination, pointer, dataType);

		return pointer;
	}

	/**
	 * Calculates how much space the fields take up together
	 * containerType, name, nameLength, dataType, data
	 * 
	 * @return
	 */
	public int getSize() {
		return RCType.BYTE_SIZE + RCType.SHORT_SIZE + nameLength + RCType.BYTE + data.length;
	}
}
