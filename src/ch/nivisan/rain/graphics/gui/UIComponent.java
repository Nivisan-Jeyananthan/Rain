package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.common.IRender;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UIComponent implements IRender {
    public Vector2 position, offset;
    protected Color color;

    UIComponent(Vector2 position) {
        this.position = position;
    }

    public UIComponent setColor(int color){
        this.color = new Color(color);
        return this;
    }

    public void render(Graphics graphics) {}

    @Override
    public void render(Screen screen) {}

    @Override
    public void update() {}

    void setOffset(Vector2 offset) {
        this.offset = offset;
    }
}
