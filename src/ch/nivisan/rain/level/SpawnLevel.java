package ch.nivisan.rain.level;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpawnLevel extends Level {

    public SpawnLevel(String path) {
        super(path);
    }

    protected void loadLevel(String path) {
        try {
            BufferedImage image = ImageIO.read(SpawnLevel.class.getResource(path));
            int width = this.width = image.getWidth();
            int height = this.height = image.getHeight();

            tiles = new int[width * height];
            image.getRGB(0, 0, width, height, tiles, 0, width);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Could not load level file!");
        }

        for (int i = 0; i < 1; i++) {
            // addEntity(new Star(this, 20, 55));
        }

    }

    @Override
    protected void generateLevel() {
        System.out.println("Tile at 0: " + tiles[0]);
    }
}
