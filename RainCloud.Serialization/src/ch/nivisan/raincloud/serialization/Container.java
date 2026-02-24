package ch.nivisan.raincloud.serialization;

public class Container {
	public final byte containerType;
	public byte[] name;
	public short nameLength;
	public final byte dataType;
	public final byte[] data;

	public Container(byte containerType, String name, byte type) {
		this.containerType = containerType;
		setName(name);
		data = new byte[Type.getSize(type)];
		dataType = type;
	}

	/**
	 * Used by sub classes array when data length is determined by outside factors
	 * 
	 * @param containerType
	 * @param name
	 * @param type
	 * @param dataLength
	 */
	public Container(byte containerType, String name, byte type, int dataLength) {
		this.containerType = containerType;
		setName(name);
		this.data = new byte[dataLength];
		dataType = type;
	}

	private void setName(String name) {
		assert (name.length() < Short.MAX_VALUE);

		nameLength = (short) name.length();
		this.name = name.getBytes();
	}

	public int getBytes(byte[] destination, int pointer) {
		destination[pointer++] = containerType;
		pointer = SerializationWriter.writeBytes(destination, pointer, nameLength);
		pointer = SerializationWriter.copyBytes(destination, pointer, name);
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
		return Type.BYTE_SIZE + Type.SHORT_SIZE + nameLength + Type.BYTE + data.length;
	}
}
