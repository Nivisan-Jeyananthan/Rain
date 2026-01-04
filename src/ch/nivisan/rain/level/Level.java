package ch.nivisan.rain.level;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.tile.Tile;

public class Level {
  protected int width;
  protected int height;
  protected int[] tiles;
  public static Level spawn = new SpawnLevel("../assets/levels/spawn.png");

  public Level(int width, int height) {
      this.width = width;
      this.height = height;

      tiles = new int[width * height];
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
      int tileSize = 16;
      screen.setOffsets(xScroll, yScroll);

      // defines render region of the current visible window region:

      // same as (xScroll / 16) = divides into tiles of 16
      // set the 4 corner pins (x,y) top left, bottom right (x,y)
      // convert from pixel precision to tile based so we do not have to deal with huge numbers
      int xStart = xScroll >> 4;
      // without adding the tilesize we would get black bars around right and bottom
      int xEnd = (xScroll + screen.width + tileSize) >> 4;
      int yStart = yScroll >> 4;
      int yEnd = (yScroll + screen.height + tileSize) >> 4;

      for(int y = yStart; y < yEnd; y++){
       for(int x = xStart; x < xEnd; x++) {
           getTile(x,y).render(x,y,screen);
       }
      }
  }

  // convert pixel position data to tile position data
  public Tile getTile(int x, int y){
      if(x < 0 || y < 0 || x >= width || y >= height)
          return Tile.empty;

      int index = x + (y * width);
      int color = tiles[index];
      switch(color){
          case Tile.color_spawnGrass -> {return Tile.spawnGrass;}
          case Tile.color_spawnHedge -> {return Tile.spawnHedge;}
          case Tile.color_spawnWall1 -> {return Tile.spawnWall1;}
          case Tile.color_spawnWall2 -> {return Tile.spawnWall2;}
          case Tile.color_spawnFloor -> {return Tile.spawnFloor;}
          case Tile.color_spawnWater -> {return Tile.spawnWater;}
          default -> {return Tile.empty;}
      }
  }
}
