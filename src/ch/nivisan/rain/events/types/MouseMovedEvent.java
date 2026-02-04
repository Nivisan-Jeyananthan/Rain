package ch.nivisan.rain.events.types;

import ch.nivisan.rain.utils.events.Event;
import ch.nivisan.rain.utils.events.EventType;

public class MouseMovedEvent extends Event {

    private final int x;
    private final int y;
    private final boolean dragged;

    public MouseMovedEvent(int x, int y, boolean dragged) {
        super(EventType.MOUSE_MOVED);
        this.x = x;
        this.y = y;
        this.dragged = dragged;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isDragged() {
        return dragged;
    }
}
