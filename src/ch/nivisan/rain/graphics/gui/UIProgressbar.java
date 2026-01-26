package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UIProgressbar extends UIComponent {
    private Vector2 size;
    private int progress;
    private Color foregroundColor;

    UIProgressbar(Vector2 position, Vector2 size, Color foregroundColor) {
        super(position);
        this.size = size;
        this.foregroundColor = foregroundColor;
    }

    public void setProgress(int progress) {
        if (progress < 0 || progress > 100) {
            throw new IllegalArgumentException("Progress must be between 0 and 100");
        }

        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    @Override
    public void update() {

    }

    public void render(Graphics graphics)
    {
        graphics.setColor(color);
        graphics.fillRect(position.getX(), position.getY(), size.getX(), size.getY());
    }
}
