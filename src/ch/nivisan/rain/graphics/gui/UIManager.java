package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.common.IRenderable;
import ch.nivisan.rain.graphics.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIManager implements IRenderable {
    private static final List<UIPanel> panels = new ArrayList<UIPanel>();
    private static UIManager instance;

    private UIManager() {
    }

    public static UIManager getInstance() {
        if (instance == null) {
            instance = new UIManager();
        }
        return instance;
    }

    public void addPanel(UIPanel panel) {
        panels.add(panel);
    }

    @Override
    public void update() {
        for (UIPanel panel : panels) {
            panel.update();
        }
    }

    @Override
    public void render(Graphics graphics) {
        for (UIPanel panel : panels) {
            panel.render(graphics);
        }
    }

    @Override
    public void render(Screen screen) {
        for (UIPanel panel : panels) {
            panel.render(screen);
        }
    }
}
