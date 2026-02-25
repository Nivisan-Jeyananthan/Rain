package ch.nivisan.raincloud.serialization;

public abstract class Container {
	public final byte containerType;
	public byte[] name;
	public short nameLength;

	public Container(byte containerType, String name) {
		this.containerType = containerType;
		setName(name);
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

		return pointer;
	}

	/**
	 * Calculates how much space the fields take up together
	 * containerType, name, nameLength 
	 * 
	 * @return
	 */
	public int getSize() {
		return RCType.BYTE_SIZE + RCType.SHORT_SIZE + nameLength;
	}
}
