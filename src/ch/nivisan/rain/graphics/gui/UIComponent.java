package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.common.IRenderable;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UIComponent implements IRenderable {
    public Vector2 position;
    protected Vector2 offset;
    protected Color color =  Color.WHITE;

    UIComponent(Vector2 position) {
        this.position = position;
    }

    public UIComponent setColor(int color) {
        this.color = new Color(color);
        return this;
    }

    public void render(Graphics graphics) {
    }

    @Override
    public void render(Screen screen) {
    }

    @Override
    public void update() {
    }

    void setOffset(Vector2 offset) {
        this.offset = offset;
    }
}
