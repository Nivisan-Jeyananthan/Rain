package ch.nivisan.rain.level;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.tile.Tile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

// level based on image data at /assets/level.png
public class ImageLevel extends Level{
    private int[] pixels;
    // used to be in Level class
    Tile[] tiles;

    public ImageLevel(String path) {
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

    // code to make the level class render from an image (render method)
    private void oldCodeInLevelClass(int xScroll, int yScroll, Screen screen){
            screen.setOffsets(xScroll, yScroll);

            // defines render region of screen:

            // same as (xScroll / 16) = divides into tiles of 16
            int xStart = xScroll >> 4;
            int xEnd = (xScroll + screen.width + 16) >> 4;
            int yStart = yScroll >> 4;
            int yEnd = (yScroll + screen.height + 16) >> 4;

            for(int y = yStart; y < yEnd; y++){
                for(int x = xStart; x < xEnd; x++) {
                    int index = x + y * 16;

                    // getTile(x,y).render(x,y,screen);
                    if (index < 0 || index >= 256){
                        Tile.empty.render(x, y, screen);
                        continue;
                    }
                    tiles[index].render(x, y, screen);
                }
            }


    }
}
