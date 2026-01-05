package ch.nivisan.rain.graphics;

import java.util.Arrays;

// Is a part of a image (16 pixels by 16 pixels)
public class Sprite {
    public static Sprite grass = new Sprite(16, 0, 0, SpriteSheet.tiles);
    public static Sprite flower = new Sprite(16, 1, 0, SpriteSheet.tiles);
    public static Sprite rock = new Sprite(16, 2, 0, SpriteSheet.tiles);
    public static Sprite empty = new Sprite(16, 0x1B87E0);
    public static Sprite spawn_grass = new Sprite(16, 0, 0, SpriteSheet.spawnLevel);
    public static Sprite spawn_hedge = new Sprite(16, 1, 0, SpriteSheet.spawnLevel);
    public static Sprite spawn_water = new Sprite(16, 2, 0, SpriteSheet.spawnLevel);
    public static Sprite spawn_wall1 = new Sprite(16, 0, 1, SpriteSheet.spawnLevel);
    public static Sprite spawn_wall2 = new Sprite(16, 0, 2, SpriteSheet.spawnLevel);
    public static Sprite spawn_floor = new Sprite(16, 1, 1, SpriteSheet.spawnLevel);
    // player sprites here:
    // unused since mathematically flipped
    public static Sprite playerLeft = new Sprite(32, 3, 5, SpriteSheet.tiles);
    public static Sprite playerBack = new Sprite(32, 0, 5, SpriteSheet.tiles);
    public static Sprite playerBack1 = new Sprite(32, 0, 6, SpriteSheet.tiles);
    public static Sprite playerBack2 = new Sprite(32, 0, 7, SpriteSheet.tiles);
    public static Sprite playerRight = new Sprite(32, 1, 5, SpriteSheet.tiles);
    public static Sprite playerRight1 = new Sprite(32, 1, 6, SpriteSheet.tiles);
    public static Sprite playerRight2 = new Sprite(32, 1, 7, SpriteSheet.tiles);
    public static Sprite playerFront = new Sprite(32, 2, 5, SpriteSheet.tiles);
    public static Sprite playerFront1 = new Sprite(32, 2, 6, SpriteSheet.tiles);
    public static Sprite playerFront2 = new Sprite(32, 2, 7, SpriteSheet.tiles);
    public final int size;
    // which pixels of the sprite get rendered
    public int[] pixels;
    private int x, y;
    private SpriteSheet sheet;

    public Sprite(int size, int x, int y, SpriteSheet sheet) {
        this.size = size;
        this.x = x * size;
        this.y = y * size;
        this.sheet = sheet;
        pixels = new int[size * size];

        loadSpriteFromSheet();
    }

    public Sprite(int size, int color) {
        this.size = size;
        pixels = new int[size * size];
        setColor(color);
    }

    private void setColor(int color) {
        Arrays.fill(pixels, color);
    }

    // goes through the SpriteSheet and loads it into the pixels array
    private void loadSpriteFromSheet() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int pixelIndex = x + y * size;
                int sheetIndex = (x + this.x) + (y + this.y) * sheet.size;

                pixels[pixelIndex] = sheet.pixels[sheetIndex];
            }
        }

    }

}
