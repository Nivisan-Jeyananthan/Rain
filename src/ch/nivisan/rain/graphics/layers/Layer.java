package ch.nivisan.rain.graphics.layers;

import ch.nivisan.rain.utils.events.Event;
import ch.nivisan.rain.utils.events.IEventListener;

import java.awt.*;

public abstract class Layer implements IEventListener {
    @Override
    public void onEvent(Event event) {
    }

    public void onUpdate(){}

    public void onRender(Graphics graphics){}
}
