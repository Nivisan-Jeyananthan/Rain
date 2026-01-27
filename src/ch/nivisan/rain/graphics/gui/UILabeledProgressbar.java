package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UILabeledProgressbar extends UIProgressbar{
    private final String text;
    private Font textFont = UIDefaults.font;
    private Color fontColor = UIDefaults.color;
    private boolean hasShadow = false;

    public UILabeledProgressbar(Vector2 position, Vector2 size, Color foregroundColor, Color fontColor, String text) {
        super(position, size, foregroundColor);
        this.text = text;
        this.fontColor = fontColor;
    }

    public UILabeledProgressbar(Vector2 position, Vector2 size, int foregroundColor, int fontColor, String text) {
        super(position, size, new Color(foregroundColor));
        this.text = text;
        this.fontColor = new Color(fontColor);
    }

    public UILabeledProgressbar setFont(Font font) {
        this.textFont = font;
        return this;
    }

    public UILabeledProgressbar setShadow(boolean shadow) {
        this.hasShadow = shadow;
        return this;
    }

    @Override
    public void render(Graphics graphics) {
        super.render(graphics);
        int xOffset = position.getX() + offset.getX() + 2;
        int yOffset =  position.getY() + offset.getY() + 15;

        graphics.setFont(textFont);
        if (hasShadow) {
            graphics.setColor(Color.BLACK);
            graphics.drawString(text, xOffset + 2, yOffset + 2);
        }

        graphics.setColor(fontColor);
        graphics.drawString(text,xOffset , yOffset);
    }
}
