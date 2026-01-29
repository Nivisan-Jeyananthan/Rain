package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.common.IRenderable;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UIComponent implements IRenderable {
    protected Vector2 position;
    protected Vector2 size;
    protected Vector2 offset;
    protected Color color =  Color.WHITE;
    protected boolean active = true;

    UIComponent(Vector2 position) {
        this.position = position;
        offset = new Vector2();
    }

    UIComponent(Vector2 position,  Vector2 size) {
        this.position = position;
        this.size = size;
        offset = new Vector2();
    }

    public UIComponent setColor(int color) {
        this.color = new Color(color);
        return this;
    }

    public UIComponent setColor(Color color) {
        this.color = color;
        return this;
    }

    public Vector2 getAbsolutePosition() {
        return Vector2.addVector(position, offset);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
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
