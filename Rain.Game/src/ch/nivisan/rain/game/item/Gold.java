package ch.nivisan.rain.game.item;

public class Gold {
	private int value;
	
	public Gold(final int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void increaseValue(int value) {
		this.value += value;
	}
	
	public void decreaseValue(int value) {
		this.value -= value;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == this)
			return true;

		if(!(obj instanceof Gold))
			return false;

		Gold tempObj = (Gold) obj;
		return this.value == tempObj.value;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}


}
