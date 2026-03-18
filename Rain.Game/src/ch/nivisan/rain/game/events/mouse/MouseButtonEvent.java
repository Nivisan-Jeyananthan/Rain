package ch.nivisan.rain.game.events.mouse;

import ch.nivisan.rain.game.events.Event;
import ch.nivisan.rain.game.events.EventType;

public class MouseButtonEvent extends Event {

    protected final int x;
    protected final int y;
    protected int button;

    protected MouseButtonEvent(int x, int y, int button, EventType eventType) {
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
