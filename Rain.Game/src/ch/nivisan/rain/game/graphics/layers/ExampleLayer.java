package ch.nivisan.rain.game.graphics.layers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import ch.nivisan.rain.game.events.*;
import ch.nivisan.rain.game.events.mouse.*;

public class ExampleLayer extends Layer {

    private static final Random random = new Random();
    private final String name;
    private final Color color;
    private final Rectangle box;
    private int previousX;
    private int previousY;
    private boolean dragging = false;

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

    public void onEvent(Event event) {
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

