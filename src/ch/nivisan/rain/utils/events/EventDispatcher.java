package ch.nivisan.rain.utils.events;

public class EventDispatcher  {
    private final Event event;

    public EventDispatcher(Event event){
        this.event = event;
    }

    public void dispatch(EventType eventType, IEventHandler handler) {
        if(event.handled)
            return;

        if(event.getEventType() == eventType){
            event.handled = handler.onEvent(event);
        }
    }
}
