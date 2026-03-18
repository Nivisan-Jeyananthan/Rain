package ch.nivisan.rain.game.level.tile.spawn;

import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.Sprite;
import ch.nivisan.rain.game.level.tile.Tile;

public class SpawnWaterTile extends Tile {
    public SpawnWaterTile(Sprite sprite) {
        super(sprite);
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 4, y << 4, this);
    }

    public boolean solid() {
        return false;
    }

}

