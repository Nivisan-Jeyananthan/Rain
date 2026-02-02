package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class UILabel extends UIComponent {
    protected final String text;
    protected Font textFont = UIDefaults.font;
    protected boolean hasShadow = false;

    public UILabel(Vector2 position, String text) {
        super(position);
        if (text.isBlank()) {
            active = false;
        }
        this.text = text;
        color = UIDefaults.defaultFontColor;
    }

    public UILabel setFont(Font font) {
        this.textFont = font;
        return this;
    }

    public UILabel setShadow(boolean shadow) {
        this.hasShadow = shadow;
        return this;
    }

    @Override
    public void render(Graphics graphics) {
        if (!active) {
            return;
        }

        super.render(graphics);

        graphics.setFont(textFont);
        if (hasShadow) {
            graphics.setColor(Color.BLACK);
            graphics.drawString(text, position.getX() + offset.getX() + 2, position.getY() + offset.getY() + 2);
        }

        graphics.setColor(color);
        graphics.drawString(text, position.getX() + offset.getX(), position.getY() + offset.getY());
    }

    @Override
    public void render(Screen screen) {
//        font.setX(position.getX() + offset.getX())
//                .setY(position.getY() +  offset.getY())
//                .render(text,screen);

    }
}
