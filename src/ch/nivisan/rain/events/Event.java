package ch.nivisan.rain.events;

public abstract class Event {

    private final EventType type;
    protected boolean handled;

    protected Event(EventType type) {
        this.type = type;
    }

    public EventType getEventType() {
        return type;
    }
}
