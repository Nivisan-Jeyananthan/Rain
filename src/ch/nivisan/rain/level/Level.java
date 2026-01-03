package ch.nivisan.rain.level;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.tile.Tile;

public class Level {
  protected int width;
  protected int height;
  protected int[] tilesInt;
  protected Tile[] tiles;


  public Level(int width, int height) {
      this.width = width;
      this.height = height;

      tilesInt = new int[width * height];
      generateLevel();
  }

  // load from file
  public Level(String path){
      loadLevel(path);
      generateLevel();

  }

  protected void generateLevel() {
  }

  protected void loadLevel(String path) {
  }

  private void time(){
  }

  public void update(){
  }

  public void render(int xScroll, int yScroll, Screen screen) {
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

  // convert pixel position data to tile position data
  public Tile getTile(int x, int y){
      if(x < 0 || y < 0 || x >= width || y >= height)
          return Tile.empty;

      if(tilesInt[x + y * width] == 0) return Tile.grass;
      if(tilesInt[x + y * width] == 1) return Tile.flower;
      if(tilesInt[x + y * width] == 2) return Tile.rock;
      return Tile.empty;
  }
}
