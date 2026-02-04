package ch.nivisan.rain.events.types;

import ch.nivisan.rain.utils.events.EventType;

public class MousePressedEvent extends MouseButtonEvent {

    public MousePressedEvent(int x, int y, int button) {
        super(x, y, button, EventType.MOUSE_PRESSED);
    }
}
