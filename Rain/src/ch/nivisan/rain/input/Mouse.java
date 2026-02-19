package ch.nivisan.rain.input;

import ch.nivisan.rain.events.IEventListener;
import ch.nivisan.rain.events.types.MouseMovedEvent;
import ch.nivisan.rain.events.types.MousePressedEvent;
import ch.nivisan.rain.events.types.MouseReleasedEvent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
    private static final MouseButton[] mouseButtons = MouseButton.values();
    private static int mouseX = -1;
    private static int mouseY = -1;
    private static int mouseButton;
    private final IEventListener eventListener;

    public Mouse(IEventListener listener) {
        this.eventListener = listener;
    }

    public static int getXPosition() {
        return mouseX;
    }

    public static int getYPosition() {
        return mouseY;
    }

    public static int getButton() {
        return mouseButton;
    }

    public static MouseButton getButtonState() {
        return mouseButtons[mouseButton];
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseButton = e.getButton();

        MousePressedEvent event = new MousePressedEvent(mouseX,mouseY,mouseButton);
        eventListener.onEvent(event);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseButton = e.getButton();

        MouseReleasedEvent event = new MouseReleasedEvent(mouseX,mouseY,mouseButton);
        eventListener.onEvent(event);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        MouseMovedEvent event = new MouseMovedEvent(mouseX,mouseY,true);
        eventListener.onEvent(event);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        MouseMovedEvent event = new MouseMovedEvent(mouseX,mouseY,false);
        eventListener.onEvent(event);
    }
}
