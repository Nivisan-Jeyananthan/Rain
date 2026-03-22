package ch.nivisan.rain.game.events.keyboard;

import ch.nivisan.rain.game.events.EventType;

public class KeyPressedEvent extends KeyboardEvent {

	public KeyPressedEvent(int key) {
		super(EventType.KEY_PRESSED, key);
	}

}
