package ch.nivisan.rain.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SpriteSheet {
    private final int width;
    private final int height;
    private String path;
    public final int[] pixels;
    public static SpriteSheet tiles = new SpriteSheet("../assets/sheets/Sprites.png", 256);
    public static SpriteSheet spawnLevel = new SpriteSheet("../assets/sheets/spawn_level.png", 48);
    public static SpriteSheet wizardProjectiles = new SpriteSheet("../assets/sheets/projectiles/wizard.png", 48);
    public static SpriteSheet stoneSheet = new SpriteSheet("../assets/tiles/WoodFloor.png", 16);

    public static SpriteSheet player = new SpriteSheet("../assets/sheets/Sprites.png", 256);
    public static SpriteSheet playerDown = new SpriteSheet(player,0,5,1,3,32);

    public SpriteSheet(String path, int width, int height){
        this.path = path;
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
        loadImage();
    }

    public SpriteSheet(SpriteSheet subSheet,int x, int y, int width, int height, int spriteSize){
        int pixelXStart = x * spriteSize;
        int pixelYStart = y * spriteSize;
        int spriteWidth = width * spriteSize;
        int spriteHeight = height * spriteSize;
        this.width = spriteWidth;
        this.height = spriteHeight;
        this.pixels = new int[spriteHeight * spriteWidth];

        for (int i = 0; i < spriteHeight; i++) {
            int yp = pixelYStart + i;
            for (int j = 0; j < spriteWidth; j++) {
                int xp = pixelXStart + j;
                pixels[j + i * spriteWidth] = subSheet.pixels[xp + yp * subSheet.getWidth()];
            }
        }
    }

    public SpriteSheet(String path, int size) {
        this.path = path;
        this.height = size;
        this.width = size;

        pixels = new int[size * size];
        loadImage();
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }


    private void loadImage() {
        try {
            BufferedImage image = ImageIO.read(Objects.requireNonNull(SpriteSheet.class.getResource(path)));
            int width = image.getWidth();
            int height = image.getHeight();

            image.getRGB(0, 0, width, height, pixels, 0, width);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
