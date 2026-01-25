package ch.nivisan.rain.graphics.gui;

import ch.nivisan.rain.common.IRender;
import ch.nivisan.rain.graphics.Screen;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UIManager implements IRender {
    private final List<UIPanel> panels = new ArrayList<UIPanel>();


    public void addPanel(UIPanel panel) {
        panels.add(panel);
    }


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

    @Override
    public void update() {
        for (UIPanel panel : panels) {
            panel.update();
        }
    }
}
