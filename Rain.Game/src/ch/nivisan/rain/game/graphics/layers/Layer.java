package ch.nivisan.rain.game.graphics.layers;

import java.awt.Graphics;

import ch.nivisan.rain.game.events.Event;
import ch.nivisan.rain.game.events.IEventListener;
import ch.nivisan.rain.game.graphics.Screen;

public abstract class Layer implements IEventListener {
    @Override
    public void onEvent(Event event) {
    }

    public void update() {
    }

    public void onRender(Graphics graphics) {
    }

    public void render(Screen screen) {
    }
}
