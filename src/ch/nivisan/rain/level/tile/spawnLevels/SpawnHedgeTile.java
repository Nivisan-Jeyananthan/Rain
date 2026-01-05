package ch.nivisan.rain.level.tile.spawnLevels;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.tile.Tile;

public class SpawnHedgeTile extends Tile {
    public SpawnHedgeTile(Sprite sprite) {
        super(sprite);
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 4, y << 4, this);
    }

    @Override
    public boolean solid() {
        return true;
    }

    public boolean breakable() {
        return true;
    }
}
