package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UIProgressbar extends UIComponent {
    private Vector2 size;
    private float progress = 0;
    private final Color foregroundColor;

    public UIProgressbar(Vector2 position, Vector2 size, Color foregroundColor) {
        super(position);
        this.size = size;
        this.foregroundColor = foregroundColor;
    }

    public void setProgress(float progress) {
        if (progress < 0.0f || progress > 1.0f) {
            throw new IllegalArgumentException("Progress must be between 0.1 and 1");
        }

        this.progress = progress;
    }

    public float getProgress() {
        return progress;
    }

    @Override
    public void update() {

    }

    public void render(Graphics graphics)
    {
        graphics.setColor(color);
        graphics.fillRect(position.getX() + offset.getX(), position.getY() + offset.getY(), size.getX(), size.getY());

        graphics.setColor(foregroundColor);
        graphics.fillRect(position.getX() + offset.getX(), position.getY() + offset.getY(), (int)(progress * size.getX()) , size.getY());
    }
}
