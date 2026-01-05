package ch.nivisan.rain.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class SpriteSheet {
    public static SpriteSheet tiles = new SpriteSheet("../assets/sheets/Sprites.png", 256);
    public static SpriteSheet spawnLevel = new SpriteSheet("../assets/sheets/spawn_level.png", 48);
    public final int size;
    private final String path;
    public int[] pixels;


    public SpriteSheet(String path, int size) {
        this.path = path;
        this.size = size;

        pixels = new int[size * size];
        loadImage();
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
