package ch.nivisan.rain.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SpriteSheet {
    public static final SpriteSheet tiles = new SpriteSheet("../assets/sheets/Sprites.png", 256);
    public static final SpriteSheet spawnLevel = new SpriteSheet("../assets/sheets/spawn_level.png", 48);
    public static final SpriteSheet wizardProjectiles = new SpriteSheet("../assets/sheets/projectiles/wizard.png", 48);
    public static final SpriteSheet stoneSheet = new SpriteSheet("../assets/tiles/WoodFloor.png", 16);
    public static final SpriteSheet player = new SpriteSheet("../assets/character.png", 128, 96);
    public static final SpriteSheet playerFront = new SpriteSheet(player, 0, 0, 1, 3, 32, 32);
    public static final SpriteSheet playerBack = new SpriteSheet(player, 1, 0, 1, 3, 32, 32);
    public static final SpriteSheet playerRight = new SpriteSheet(player, 2, 0, 1, 3, 32, 32);
    public static final SpriteSheet playerLeft = new SpriteSheet(player, 3, 0, 1, 3, 32, 32);
    public static final SpriteSheet dummy = new SpriteSheet("../assets/Mob.png", 128, 96);
    public static final SpriteSheet dummyFront = new SpriteSheet(dummy, 0, 0, 1, 3, 32, 32);
    public static final SpriteSheet dummyBack = new SpriteSheet(dummy, 1, 0, 1, 3, 32, 32);
    public static final SpriteSheet dummyRight = new SpriteSheet(dummy, 2, 0, 1, 3, 32, 32);
    public static final SpriteSheet dummyLeft = new SpriteSheet(dummy, 3, 0, 1, 3, 32, 32);
    private final int spriteWidth, spriteHeight;

    private final int[] pixels;
    private final int width;
    private final int height;
    private String path;
    private Sprite[] sprites;

    public SpriteSheet(String path, int size) {
        this.path = path;
        this.height = this.width = this.spriteHeight = this.spriteWidth = size;

        pixels = new int[size * size];
        loadImage();
    }

    public SpriteSheet(String path, int width, int height) {
        this.path = path;
        this.width = this.spriteWidth = width;
        this.height = this.spriteHeight = height;
        this.pixels = new int[width * height];
        loadImage();
    }

    /**
     * Creates a spritesheet based on parent sprite sheet.
     * A sub sheet to say.
     *
     * @param parentSheet      the parent
     * @param x                at which tile width it begins
     * @param y                at which tile height it begins
     * @param width            how long it should be
     * @param height           how high it should be
     * @param spriteSizeWidth  the width of each individual sprite
     * @param spriteSizeHeight the height of each individual sprite
     */
    public SpriteSheet(SpriteSheet parentSheet, int x, int y, int width, int height, int spriteSizeWidth, int spriteSizeHeight) {
        int pixelXStart = x * spriteSizeWidth;
        int pixelYStart = y * spriteSizeHeight;
        int spriteWidth = width * spriteSizeWidth;
        int spriteHeight = height * spriteSizeHeight;
        this.spriteWidth = spriteSizeWidth;
        this.spriteHeight = spriteSizeHeight;
        this.width = spriteWidth;
        this.height = spriteHeight;
        this.pixels = new int[spriteHeight * spriteWidth];
        this.sprites = new Sprite[width * height];

        createSubSheetFromSheet(parentSheet, spriteWidth, spriteHeight, pixelYStart, pixelXStart);

        assignSpritesToPixels(width, height, spriteSizeWidth, spriteSizeHeight, spriteHeight, spriteWidth);
    }

    /**
     * Separates the sprites into "tiles" and assigns them to pixels array for render
     *
     * @param width            how many sprites we have in width (1,2,3, ...)
     * @param height           how many sprites we have in height (1,2,3, ...)
     * @param spriteSizeHeight 16,32 or larger
     * @param spriteSizeWidth  16,32 or larger
     * @param spriteHeight     how high our sprite is if not square
     * @param spriteWidth      how wide our sprite is if not square
     */
    private void assignSpritesToPixels(int width, int height, int spriteSizeWidth, int spriteSizeHeight, int spriteHeight, int spriteWidth) {
        int frame = 0;
        for (int tileY = 0; tileY < height; tileY++) {
            for (int tileX = 0; tileX < width; tileX++) {
                int[] spritePixels = new int[spriteSizeWidth * spriteSizeHeight];
                for (int y0 = 0; y0 < spriteSizeHeight; y0++) {
                    for (int x0 = 0; x0 < spriteSizeWidth; x0++) {
                        int pixelIndexY = tileY * spriteSizeHeight;
                        int pixelIndexX = tileX * spriteSizeWidth;

                        spritePixels[x0 + y0 * spriteSizeWidth] = pixels[(x0 + pixelIndexX) + (y0 + pixelIndexY) * spriteWidth];
                    }
                }
                Sprite sprite = new Sprite(spritePixels, this.spriteWidth, this.spriteHeight);
                sprites[frame++] = sprite;
            }
        }
    }

    private void createSubSheetFromSheet(SpriteSheet parentSheet, int spriteWidth, int spriteHeight, int pixelYStart, int pixelXStart) {
        for (int i = 0; i < spriteHeight; i++) {
            int yp = pixelYStart + i;
            for (int j = 0; j < spriteWidth; j++) {
                int xp = pixelXStart + j;
                int sheetIndex = xp + yp * parentSheet.getWidth();
                int pixelIndex = j + i * spriteWidth;
                pixels[pixelIndex] = parentSheet.pixels[sheetIndex];
            }
        }
    }

    private void loadImage() {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(SpriteSheet.class.getResource(path)));
            int width = image.getWidth();
            int height = image.getHeight();

            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception e){
            System.err.println(SpriteSheet.class.getResource(path) + " failed to load!");
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSpriteWidth() {
        return spriteWidth;
    }

    public int getSpriteHeight() {
        return spriteHeight;
    }

    public int[] getPixels(){
        return pixels;
    }

    public Sprite[] getSprites() {
        return sprites;
    }

}
