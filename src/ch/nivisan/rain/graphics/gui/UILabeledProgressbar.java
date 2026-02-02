package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UILabeledProgressbar extends UIProgressbar {
    private final UILabel label;

    public UILabeledProgressbar(Vector2 position, Vector2 size, Color foregroundColor, Color fontColor, String text) {
        super(position, size, foregroundColor);
        label = new UILabel(position, text);
        label.setColor(fontColor);
        label.setFont(new Font("Courier New", Font.BOLD, 20));
    }

    public UILabeledProgressbar(Vector2 position, Vector2 size, int foregroundColor, int fontColor, String text) {
        super(position, size, new Color(foregroundColor));
        label = new UILabel(position, text);
        label.setColor(fontColor);
        label.setFont(new Font("Courier New", Font.BOLD, 20));
    }

    public UILabeledProgressbar setFont(Font font) {
        label.setFont(font);
        return this;
    }

    public UILabeledProgressbar setShadow(boolean shadow) {
        label.setShadow(shadow);
        return this;
    }

    @Override
    public void setOffset(Vector2 offset) {
        super.setOffset(offset);
        label.setOffset(new Vector2(offset.getX() + 2,offset.getY() + 15));
    }

    @Override
    public void update() {
        if(!active){ return; }

        super.update();
        label.update();
    }

    @Override
    public void render(Graphics graphics) {
        if(!active){ return; }

        super.render(graphics);
        label.render(graphics);
    }
}
