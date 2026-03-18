package ch.nivisan.rain.game.level.tile;

import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.Sprite;

public class FlowerTile extends Tile {
    public FlowerTile(Sprite sprite) {
        super(sprite);
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 4, y << 4, this);
    }
}
