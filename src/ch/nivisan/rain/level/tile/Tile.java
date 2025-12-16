package ch.nivisan.rain.level.tile;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;

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
