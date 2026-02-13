package ch.nivisan.rain.common;

import ch.nivisan.rain.events.Event;
import ch.nivisan.rain.events.IEventListener;
import ch.nivisan.rain.input.Keyboard;
import ch.nivisan.rain.input.Mouse;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class WindowScreen extends Canvas implements IEventListener {
    private final Keyboard keyboard;
    private final Mouse mouse;
    private BufferStrategy bufferStrategy;
    private Graphics graphics;

    public WindowScreen(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        keyboard = new Keyboard();
        addKeyListener(keyboard);
        mouse = new Mouse(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public void init() {
        createBufferStrategy(3);
    }

    public void beginRendering() {
        bufferStrategy = getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();
    }

    public void clear() {
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    public void endRendering() {
        graphics.dispose();
        bufferStrategy.show();
    }

    @Override
    public void onEvent(Event event) {

    }
}
