package ch.nivisan.rain.graphics.layers;

import ch.nivisan.rain.events.Event;
import ch.nivisan.rain.events.EventDispatcher;
import ch.nivisan.rain.events.EventType;
import ch.nivisan.rain.events.types.MouseMovedEvent;
import ch.nivisan.rain.events.types.MousePressedEvent;
import ch.nivisan.rain.events.types.MouseReleasedEvent;
import java.awt.*;
import java.util.Random;

public class ExampleLayer extends Layer {

    private final String name;
    private final Color color;
    private final Rectangle box;
    private int previousX;
    private int previousY;

    private boolean dragging = false;

    private static final Random random = new Random();

    public ExampleLayer(String name, Color color) {
        this.name = name;
        this.color = color;

        box = new Rectangle(
            random.nextInt(300) + 100,
            random.nextInt(200) + 80,
            80,
            50
        );
    }

    public void onEvent(ch.nivisan.rain.events.Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(
            EventType.MOUSE_PRESSED,
            (Event e) ->
                (onMousePressed((MousePressedEvent) e))
        );
        dispatcher.dispatch(
            EventType.MOUSE_RELEASED,
            (Event e) ->
                (onMouseReleased((MouseReleasedEvent) e))
        );
        dispatcher.dispatch(EventType.MOUSE_MOVED, (Event e) ->
            (onMouseMoved((MouseMovedEvent) e))
        );
    }

    public boolean onMousePressed(MousePressedEvent event) {
        if (box.contains(new Point(event.getX(), event.getY()))) {
            dragging = true;
        }

        return dragging;
    }

    public boolean onMouseReleased(MouseReleasedEvent event) {
        dragging = false;
        return dragging;
    }

    public boolean onMouseMoved(MouseMovedEvent event) {
        int x = event.getX();
        int y = event.getY();

        if (dragging) {
            box.x += x - previousX;
            box.y += y - previousY;
        }
        previousX = x;
        previousY = y;

        return dragging;
    }

    public void onRender(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect(box.x, box.y, box.width, box.height);

        graphics.setColor(Color.white);
        graphics.drawString(name, box.x + 5, box.y + 10);
    }
}
