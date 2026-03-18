package ch.nivisan.rain.game.common;

import java.awt.Graphics;

import ch.nivisan.rain.game.graphics.Screen;

public interface IRenderable {
    /**
     * Defines how to render the current component using graphics api of java.
     * Rendering is uncapped.
     *
     * @param graphics
     */
    void render(Graphics graphics);

    /**
     * Defines how to render the current component.
     * Rendering is uncapped.
     *
     * @param screen
     */
    void render(Screen screen);

    /**
     * Defines how to update the current component
     */
    void update();
}
