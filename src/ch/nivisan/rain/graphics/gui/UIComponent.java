package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.common.IRender;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.utils.Vector2;

public class UIComponent implements IRender {
    public Vector2 position, offset;
    public int backgroundColor;

    UIComponent(Vector2 position) {
        this.position = position;
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
