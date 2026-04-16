package ch.nivisan.rain.game.graphics.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.gui.UIDefaults;
import ch.nivisan.rain.game.utils.Vector2;

public class UIPanel extends UIComponent {
    private final List<UIComponent> components = new ArrayList<UIComponent>();
    protected final Vector2 position;
    private final Vector2 size;
    private final Color color;

    public UIPanel(Vector2 position, Vector2 size, Color color) {
        super(position, size);
        this.position = position;
        this.color = color != null ? color : UIDefaults.defaultColorPanel;
        this.size = size;
    }

    public UIPanel(Vector2 position, Vector2 size, int color) {
        super(position, size);
        this.position = position;
        this.color = color != 0 ? new Color(color) : UIDefaults.defaultColorPanel;
        this.size = size;
    }

    public void addComponent(UIComponent component) {
        component.setOffset(getAbsolutePosition());
        components.add(component);
    }


    public void render(Graphics graphics) {
        if (!active) {
            return;
        }
    	Vector2 absolutePosition = getAbsolutePosition();
        graphics.setColor(color);
        graphics.fillRect(absolutePosition.getX(), absolutePosition.getY(), size.getX(), size.getY());
        for (UIComponent component : components) {
            component.render(graphics);
        }
    }

    @Override
    public void update() {
        if (!active) {
            return;
        }

        for (int i = 0; i < components.size(); i++) {
            components.get(i).update();
        }
    }

    @Override
    public void render(Screen screen) {
        if (!active) {
            return;
        }

        for (int i = 0; i < components.size(); i++) {
            components.get(i).render(screen);
        }
    }
}

