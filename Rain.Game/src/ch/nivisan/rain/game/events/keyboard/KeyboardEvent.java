package ch.nivisan.rain.game.events.keyboard;

import ch.nivisan.rain.game.events.Event;
import ch.nivisan.rain.game.events.EventType;

public class KeyboardEvent extends Event {
	private final int Key;

	protected KeyboardEvent(EventType type, int key) {
		super(type);
		this.Key = key;
	}
	
	public int getKey() {
		return Key;
	}

}
