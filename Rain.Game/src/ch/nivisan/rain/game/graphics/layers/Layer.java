package ch.nivisan.rain.game.graphics.layers;

import java.awt.Graphics;

import ch.nivisan.rain.game.events.Event;
import ch.nivisan.rain.game.events.IEventListener;
import ch.nivisan.rain.game.graphics.Screen;

public abstract class Layer implements IEventListener {
	
    public abstract void onEvent(Event event);

    public abstract void update();

    public void onRender(Graphics graphics) {}

    public abstract void render(Screen screen);
}
