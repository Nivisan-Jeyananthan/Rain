package ch.nivisan.rain.game.events.keyboard;

import ch.nivisan.rain.game.events.EventType;

public class KeyReleasedEvent extends KeyboardEvent{

	protected KeyReleasedEvent(int key) {
		super(EventType.KEY_RELEASED, key);
	}

}
