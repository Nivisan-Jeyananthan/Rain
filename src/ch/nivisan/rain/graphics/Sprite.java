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

    public static Sprite dummy = new Sprite(32, 0, 0, SpriteSheet.dummyFront);

    // projectiles
    public static Sprite wizardProjectile = new Sprite(16, 0, 0, SpriteSheet.wizardProjectiles);
    public static Sprite roachProjectile = new Sprite(16, 1, 0, SpriteSheet.wizardProjectiles);
    public static Sprite nuclearProjectile = new Sprite(16, 2, 0, SpriteSheet.wizardProjectiles);
    public static Sprite arrowProjectile = new Sprite(16, 0, 1, SpriteSheet.wizardProjectiles);

    // particles
    public static Sprite particleDefault = new Sprite(3, 0xAAAAAAAA);
    // which pixels of the sprite get rendered
    private final int[] pixels;
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
     * goes through the SpriteSheet and loads it into the pixels array raw
     */
    private void loadSpriteFromSheet() {
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                int pixelIndex = x + y * width;
                int sheetIndex = (x + this.x) + (y + this.y) * sheet.getWidth();

                pixels[pixelIndex] = sheet.getPixels()[sheetIndex];
            }
        }
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

    public static Sprite scale(int[] pixels, int oldWidth, int oldHeight, int newWidth, int newHeight) {
        int[] new_pixels = new int[newWidth * newHeight];

        int xr = ((oldWidth << 4) / newWidth) + 1;
        int yr = ((oldHeight << 4) / newHeight) + 1;

        int x1, y1;

        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
                x1 = ((x * xr) >> 4);
                y1 = ((y * yr) >> 4);
                new_pixels[x + (y * newWidth)] = pixels[x1 + (y1 * oldWidth)];
            }
        }

        return new Sprite(new_pixels, newWidth, newHeight);
    }

    public static Sprite rotateSprite(Sprite sprite, float angle){
        return new Sprite(rotate(sprite.pixels, sprite.getWidth(), sprite.getHeight(), angle), sprite.getWidth(), sprite.getHeight());
    }

    /**
     * Rotation is done clockwise instead of according to unit of circle (counterclockwise).
     * Therefor units are in negative instead of positive
     * @param pixels
     * @param spriteWidth
     * @param spriteHeight
     * @param angle
     * @return
     */
    private static int[] rotate(int[] pixels, int spriteWidth, int spriteHeight, float angle) {
        final int[] result = new int[spriteWidth * spriteHeight];
        final int alphaColor = 0xffff00ff;

        float nx_x = rotationX(-angle,1.0f, 0.0f);
        float nx_y = rotationY(-angle,1.0f,0.0f);

        float ny_x = rotationX(-angle,0.0f, 1.0f);
        float ny_y = rotationY(-angle,0.0f,1.0f);

        float initialRotationX = rotationX(-angle, -spriteWidth / 2.0f, -spriteHeight / 2.0f) + spriteWidth / 2.0f;
        float initialRotationY = rotationY(-angle, -spriteWidth / 2.0f, -spriteHeight / 2.0f) + spriteHeight / 2.0f;


        for (int y = 0; y < spriteHeight; y++) {
            float x0 = initialRotationX;
            float y0 = initialRotationY;
            for (int x = 0; x < spriteWidth; x++) {
                int x1 = (int) x0;
                int y1 = (int) y0;
                int color = 0;
                if(x1 < 0 || x1 >= spriteWidth || y1 < 0 || y1 >= spriteHeight){
                    color = alphaColor;
                }else {
                    color = pixels[x1 + y1 * spriteWidth];
                }
                result[x + y * spriteWidth] = color;
                x0 += nx_x;
                y0 += nx_y;
            }

            initialRotationY += ny_y;
            initialRotationX += ny_x;
        }

        return result;
    }

    /**
     * Calculates where the new x positions should be.
     * Negation of sin due to clock wise rotation
     * @param angle in radians therefor no conversion from degrees needed
     * @param x if we want to do it in x axis
     * @param y if we want to do it in y axis
     * @return
     */
    private static float rotationX(float angle, float x, float y){
        float cos = (float) Math.cos(angle - Math.PI / 2);
        float sin = (float) Math.sin(angle - Math.PI / 2);

        return x * cos + y * -sin;
    }

    /**
     * Calculates where the new y positions should be.
     * @param angle in radians therefor no conversion from degrees needed
     * @param x
     * @param y
     * @return
     */
    private static float rotationY(float angle, float x, float y){
        float cos = (float) Math.cos(angle - Math.PI / 2);
        float sin = (float) Math.sin(angle - Math.PI / 2);

        return x * sin + y * cos;
    }

    public static Sprite[] split(SpriteSheet sheet, int spriteWidth, int spriteHeight){
        int amount = (sheet.getWidth() * sheet.getHeight())/(spriteWidth * spriteHeight);
        Sprite[] sprites = new Sprite[amount];

        int current = 0;
        for (int spriteY = 0; spriteY < sheet.getHeight() / spriteHeight; spriteY++) {
            for (int spriteX = 0; spriteX < sheet.getWidth() / spriteWidth; spriteX++) {
                int[] spritePixels = new int[spriteHeight * spriteWidth];

                for (int y = 0; y < spriteHeight; y++) {
                    for (int x = 0; x < spriteWidth; x++) {
                        int originalPixelX = x + spriteX * spriteWidth;
                        int originalPixelY = y + spriteY * spriteHeight;

                        spritePixels[x + y * spriteWidth] = sheet.getPixels()[originalPixelX + originalPixelY * sheet.getWidth()];
                    }
                }
                sprites[current++] = new Sprite(spritePixels,spriteWidth, spriteHeight);
            }

        }

        return sprites;
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

    public int[] getPixels() { return pixels; }

    private void setColor(int color) {
        Arrays.fill(pixels, color);
    }



}
