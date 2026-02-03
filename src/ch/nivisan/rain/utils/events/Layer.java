package ch.nivisan.rain.utils.events;

import java.awt.*;

public abstract class Layer implements IEventListener {
    @Override
    public void onEvent(Event event) {
    }

    public void onUpdate(){}

    public void onRender(Graphics graphics){}
}
