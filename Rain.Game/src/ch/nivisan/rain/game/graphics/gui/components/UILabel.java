package ch.nivisan.rain.game.graphics.gui.components;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.gui.UIDefaults;
import ch.nivisan.rain.game.utils.Vector2;

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

