package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.common.IRender;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class UIPanel implements IRender {
    private final List<UIComponent> components = new ArrayList<UIComponent>();
    private final Vector2 position;
    private final Sprite sprite = new Sprite(100,300);

    public UIPanel(Vector2 position) {
        this.position = position;
    }

    public void addComponent(UIComponent component) {
        components.add(component);
    }

    @Override
    public void render(Screen screen) {
        screen.renderSprite(position.getX(),position.getY(), sprite,false, false);

        for (int i = 0; i < components.size(); i++) {
            components.get(i).render(screen);
        }
    }

    @Override
    public void update() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).setOffset(position);
            components.get(i).update();
        }
    }
}
