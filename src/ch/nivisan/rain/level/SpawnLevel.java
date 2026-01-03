package ch.nivisan.rain.level;

import ch.nivisan.rain.level.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpawnLevel extends Level{
    private int[] pixels;

    public SpawnLevel(String path) {
        super(path);
    }

    protected void loadLevel(String path) {
        try{
            BufferedImage image = ImageIO.read(SpawnLevel.class.getResource(path));
            int width = image.getWidth();
            int height = image.getHeight();
            tiles = new Tile[width * height];
            pixels = new int[width * height];
            image.getRGB(0,0,width,height,pixels,0,width);
        }
        catch(IOException ex){
            ex.printStackTrace();
            System.out.println("Could not load level file!");
        }
    }

    // Grass = 0x00ff00
    // Flower = 0xffff00
    // Rock = 0x7f7f00
    @Override
    protected void generateLevel() {
        for(int i = 0; i < pixels.length; i++){
            if(pixels[i] == 0xff00ff00){
                tiles[i] = Tile.grass;
            }
            else if(pixels[i] == 0xffffff00){
                tiles[i] = Tile.flower;
            }
            else if(pixels[i] == 0xff7f7f00){
                tiles[i] = Tile.rock;
            }
        }
    }
}
