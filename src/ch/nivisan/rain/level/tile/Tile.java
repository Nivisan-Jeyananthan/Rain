package ch.nivisan.rain.level.tile;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;

// is an object which has a sprite.
// it can be anything from
public class Tile {
    public int width, height;
    public Sprite sprite;
    public static Tile grass = new GrassTile(Sprite.grass);

    public Tile(Sprite sprite) {
       this.sprite = sprite;
    }

    public void render(int x, int y, Screen screen){
    }

    public boolean solid() {
        return false;
    }
}
