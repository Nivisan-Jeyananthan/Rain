package ch.nivisan.rain.graphics.layers;


import ch.nivisan.rain.events.Event;
import ch.nivisan.rain.events.IEventListener;
import ch.nivisan.rain.graphics.Screen;

import java.awt.Graphics;

public abstract class Layer implements IEventListener {
    @Override
    public void onEvent(Event event) {
    }

    public void update(){}

    public void onRender(Graphics graphics){}

    public void render(Screen screen){}
}
