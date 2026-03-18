package ch.nivisan.rain.game.events.mouse;

import ch.nivisan.rain.game.events.EventType;

public class MousePressedEvent extends MouseButtonEvent {

    public MousePressedEvent(int x, int y, int button) {
        super(x, y, button, EventType.MOUSE_PRESSED);
    }
}

