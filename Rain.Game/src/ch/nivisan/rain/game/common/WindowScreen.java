package ch.nivisan.rain.game.common;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import ch.nivisan.rain.game.events.Event;
import ch.nivisan.rain.game.events.IEventListener;
import ch.nivisan.rain.game.input.Keyboard;
import ch.nivisan.rain.game.input.Mouse;

public class WindowScreen extends Canvas implements IEventListener {
    private static final long serialVersionUID = 1L;
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
