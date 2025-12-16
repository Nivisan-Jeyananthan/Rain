package ch.nivisan.rain.graphics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpriteSheet {
    public final int size;
    public int[] pixels;
    private final String path;
    public static SpriteSheet tiles = new SpriteSheet("../assets/Grass.png", 256);

    public SpriteSheet(String path, int size) {
        this.path = path;
        this.size = size;

        pixels = new int[size * size];
        loadImage();
    }

  private void  loadImage(){
      try {
          BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
          int width = image.getWidth();
          int height = image.getHeight();

          image.getRGB(0, 0, width, height, pixels, 0, width);
      } catch (IOException e) {
          throw new RuntimeException(e);
      }
  }
}
