package ch.nivisan.rain.level;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.tile.Tile;

public class Level {
  protected int width;
  protected int height;
  protected int[] tiles;

  public Level(int width, int height) {
      this.width = width;
      this.height = height;

      tiles = new int[width * height];
      generateLevel();
  }

  // load from file
  public Level(String path){
     loadLevel(path);
  }

  protected void generateLevel() {
  }

  private void loadLevel(String path) {
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
      int xEnd = (xScroll + screen.width) >> 4;
      int yStart = yScroll >> 4;
      int yEnd = (yScroll + screen.height) >> 4;

      for(int y = yStart; y < yEnd; y++){
       for(int x = xStart; x < xEnd; x++){
           getTile(x,y).render(x,y,screen);
       }
      }

  }

  // convert pixel position data to tile position data
  public Tile getTile(int x, int y){
      if(tiles[x + y * width] == 0) return Tile.grass;
      return Tile.empty;
  }
}
