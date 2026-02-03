package ch.nivisan.rain.utils.events.types;

import ch.nivisan.rain.utils.events.EventType;

public class MouseReleasedEvent extends MouseButtonEvent{
    public MouseReleasedEvent(int x, int y, int button) {
        super(x, y, button, EventType.MOUSE_RELEASED);
    }
}
