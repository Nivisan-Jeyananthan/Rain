package ch.nivisan.rain.level.tile.spawnLevels;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.tile.Tile;

public class SpawnWallTile extends Tile {
    public SpawnWallTile(Sprite sprite) {
        super(sprite);
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 4, y << 4, this);
    }

    public boolean solid() {
        return true;
    }

}
