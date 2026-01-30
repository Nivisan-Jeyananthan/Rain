package ch.nivisan.rain.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {
    private static int mouseX = -1;
    private static int mouseY = -1;
    private static int mouseButton;
    private static final MouseButton[] mouseButtons = MouseButton.values();

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
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseButton = MouseButton.None.getNumValue();
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
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }
}
