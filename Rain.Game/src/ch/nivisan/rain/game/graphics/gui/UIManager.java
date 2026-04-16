package ch.nivisan.rain.game.graphics.gui;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import ch.nivisan.rain.game.common.IRenderable;
import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.gui.components.UIPanel;

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

    public void removePanel(UIPanel panel) {
        panels.remove(panel);
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
