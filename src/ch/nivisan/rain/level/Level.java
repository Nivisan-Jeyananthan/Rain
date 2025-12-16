package ch.nivisan.rain.level;

import ch.nivisan.rain.graphics.Screen;

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
  }
}
