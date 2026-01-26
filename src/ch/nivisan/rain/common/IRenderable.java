package ch.nivisan.rain.common;

import ch.nivisan.rain.graphics.Screen;

public interface IRenderable {
    /**
     * Defines how to render the current component.
     * Rendering is uncapped.
     * @param screen
     */
    void render(Screen screen);

    /**
     * Defines how to update the current component
     */
    void update();
}
