package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.common.IRenderable;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.utils.Vector2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIPanel implements IRenderable {
    private static final Color defaultColor = new Color(0xB3000000, true);
    private final List<UIComponent> components = new ArrayList<UIComponent>();
    private final Vector2 position;
    private final Vector2 size;
    private final Color color;

    public UIPanel(Vector2 position, Vector2 size, Color color) {
        this.position = position;
        this.color = color.getRGB() != 0xfff ? color : defaultColor;
        this.size = size;
    }

    public UIPanel(Vector2 position, Vector2 size, int color) {
        this.position = position;
        this.color = color != 0 ? new Color(color) : defaultColor;
        this.size = size;
    }


    public void addComponent(UIComponent component) {
        components.add(component);
    }


    public void render(Graphics graphics) {
        graphics.setColor(color);
        graphics.fillRect(position.getX(), position.getY(), size.getX(), size.getY());
        for (UIComponent component : components) {
            component.render(graphics);
        }
    }

    @Override
    public void render(Screen screen) {
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
