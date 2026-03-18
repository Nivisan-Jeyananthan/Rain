package ch.nivisan.rain.game.level.tile.spawn;

import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.Sprite;
import ch.nivisan.rain.game.level.tile.Tile;

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
