package ch.nivisan.rain.level;

import ch.nivisan.rain.entity.mob.Shooter;

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
            addEntity(new Shooter(this, 20, 55));
        }
        addEntity(new Shooter(this, 25, 55));

    }

    // Grass = 0x00ff00
    // Flower = 0xffff00
    // Rock = 0x7f7f00
    @Override
    protected void generateLevel() {
        System.out.println("Tile at 0: " + tiles[0]);
    }
}
