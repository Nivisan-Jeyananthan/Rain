package ch.nivisan.rain.utils.events.types;

import ch.nivisan.rain.utils.events.Event;
import ch.nivisan.rain.utils.events.EventType;

public class MouseButtonEvent extends Event {
    protected int button;
    protected final int x;
    protected final int y;

    protected MouseButtonEvent(int x, int y, int button ,EventType eventType) {
        super(eventType);
        this.x = x;
        this.y = y;
        this.button = button;
    }

    public int getButton() {
        return button;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
