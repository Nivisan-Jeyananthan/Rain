package ch.nivisan.rain.game.events.mouse;

import ch.nivisan.rain.game.events.EventType;

public class MouseReleasedEvent extends MouseButtonEvent {

    public MouseReleasedEvent(int x, int y, int button) {
        super(x, y, button, EventType.MOUSE_RELEASED);
    }
}
