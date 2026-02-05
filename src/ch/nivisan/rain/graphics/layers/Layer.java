package ch.nivisan.rain.graphics.layers;


import ch.nivisan.rain.events.Event;
import ch.nivisan.rain.events.IEventListener;
import java.awt.Graphics;

public abstract class Layer implements IEventListener {
    @Override
    public void onEvent(Event event) {
    }

    public void onUpdate(){}

    public void onRender(Graphics graphics){}
}
