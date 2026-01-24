package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.SpriteFont;
import ch.nivisan.rain.utils.Vector2;

public class UILabel extends UIComponent{
    public String text;
    private final SpriteFont font;

    public UILabel(Vector2 position, String text) {
        super(position);
        font = new SpriteFont();
        this.text = text;
    }

    @Override
    public void render(Screen screen) {
        font.setX(position.getX() + offset.getX())
                .setY(position.getY() +  offset.getY())
                .render(text,screen);

    }
}
