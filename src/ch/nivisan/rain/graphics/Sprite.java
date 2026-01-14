package ch.nivisan.rain.graphics;

import java.util.Arrays;

// Is a part of a image (16 pixels by 16 pixels)
public class Sprite {
    public static Sprite grass = new Sprite(16, 12, 1, SpriteSheet.tiles);
    public static Sprite flower = new Sprite(16, 1, 0, SpriteSheet.tiles);
    public static Sprite rock = new Sprite(16, 2, 0, SpriteSheet.tiles);
    public static Sprite walls = new Sprite(16, 0, 1, SpriteSheet.tiles);
    public static Sprite walls2 = new Sprite(16, 6, 1, SpriteSheet.tiles);
    public static Sprite woodFloor = new Sprite(16, 5, 1, SpriteSheet.tiles);
    public static Sprite woodFloorBase = new Sprite(16, 5, 1, SpriteSheet.tiles);
    public static Sprite empty = new Sprite(16, 0x1B87E0);

    public static Sprite spawn_grass = new Sprite(16, 0, 0, SpriteSheet.spawnLevel);
    public static Sprite spawn_hedge = new Sprite(16, 1, 0, SpriteSheet.spawnLevel);
    public static Sprite spawn_water = new Sprite(16, 2, 0, SpriteSheet.spawnLevel);
    public static Sprite spawn_wall1 = new Sprite(16, 0, 1, SpriteSheet.spawnLevel);
    public static Sprite spawn_wall2 = new Sprite(16, 0, 2, SpriteSheet.spawnLevel);
    public static Sprite spawn_floor = new Sprite(16, 1, 1, SpriteSheet.spawnLevel);
    // player sprites here:
    // unused since mathematically flipped
    public static Sprite playerLeft = new Sprite(32, 3, 0, SpriteSheet.player);
    public static Sprite playerFront = new Sprite(32, 0, 0, SpriteSheet.player);
    public static Sprite playerFront1 = new Sprite(32, 0, 1, SpriteSheet.player);
    public static Sprite playerFront2 = new Sprite(32, 0, 2, SpriteSheet.player);
    public static Sprite playerBack = new Sprite(32, 1, 0, SpriteSheet.player);
    public static Sprite playerBack1 = new Sprite(32, 1, 1, SpriteSheet.player);
    public static Sprite playerBack2 = new Sprite(32, 1, 2, SpriteSheet.player);
    public static Sprite playerRight = new Sprite(32, 2, 0, SpriteSheet.player);
    public static Sprite playerRight1 = new Sprite(32, 2, 1, SpriteSheet.player);
    public static Sprite playerRight2 = new Sprite(32, 2, 2, SpriteSheet.player);


    // projectiles
    public static Sprite wizardProjectile = new Sprite(16, 0, 0, SpriteSheet.wizardProjectiles);

    // particles
    public static Sprite particleDefault = new Sprite(3, 0xAAAAAAAA);
    // which pixels of the sprite get rendered
    public final int[] pixels;
    private final int width;
    private final int height;
    protected SpriteSheet sheet;
    private int x, y;

    public Sprite(int width, int height, int color) {
        this.width = width;
        this.height = height;
        this.pixels = new int[height * width];
        setColor(color);
    }

    protected Sprite(SpriteSheet spriteSheet, int width, int height) {
        this.width = width;
        this.height = height;
        this.sheet = spriteSheet;
        this.pixels = new int[width * height];
    }

    /**
     * Create a sprite from an existing spritesheet, where the sprite is the entire size
     *
     * @param size
     * @param x
     * @param y
     * @param sheet
     */
    public Sprite(int size, int x, int y, SpriteSheet sheet) {
        this.x = x * size;
        this.y = y * size;
        this.sheet = sheet;
        this.height = size;
        this.width = size;

        pixels = new int[size * size];

        loadSpriteFromSheet();
    }

    /**
     * Creates a sprite of given size filled by the given color
     *
     * @param size
     * @param color
     */
    public Sprite(int size, int color) {
        this.height = size;
        this.width = size;
        pixels = new int[size * size];
        setColor(color);
    }

    public Sprite(int[] pixels, int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public int getSize() {
        return width;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private void setColor(int color) {
        Arrays.fill(pixels, color);
    }


    /**
     * goes through the SpriteSheet and loads it into the pixels array raw
     */
    private void loadSpriteFromSheet() {
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                int pixelIndex = x + y * width;
                int sheetIndex = (x + this.x) + (y + this.y) * sheet.getWidth();

                pixels[pixelIndex] = sheet.pixels[sheetIndex];
            }
        }

    }

}
