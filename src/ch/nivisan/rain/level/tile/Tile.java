package ch.nivisan.rain.level.tile;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.tile.spawnLevels.*;

// is an object which has a sprite.
// it can be anything from
public class Tile {
    // unused
    public static final int color_spawnHedge = 0;
    public static final int color_spawnWater = 1;
    public static final int color_spawnGrass = 0xff00ff00;
    public static final int color_spawnWall1 = 0xff4d7807;
    public static final int color_spawnWall2 = 0xff3b5b05;
    public static final int color_spawnFloor = 0xffffffff;
    public static final int color_spawnFloor2 = 0xff4d4d4d;


    public static Tile grass = new GrassTile(Sprite.grass);
    public static Tile flower = new FlowerTile(Sprite.flower);
    public static Tile rock = new RockTile(Sprite.rock);
    public static Tile wallsStone = new SpawnWallTile(Sprite.walls);
    public static Tile wallsRock = new SpawnWallTile(Sprite.walls2);
    public static Tile woodFloor = new SpawnFloorTile(Sprite.woodFloor);
    public static Tile woodFloorBase = new SpawnFloorTile(Sprite.woodFloorBase);


    public static Tile empty = new EmptyTile(Sprite.empty);
    public static Tile spawnGrass = new SpawnGrassTile(Sprite.spawn_grass);
    public static Tile spawnHedge = new SpawnHedgeTile(Sprite.spawn_hedge);
    public static Tile spawnWater = new SpawnWaterTile(Sprite.spawn_water);
    public static Tile spawnWall1 = new SpawnWallTile(Sprite.spawn_wall1);
    public static Tile spawnWall2 = new SpawnWallTile(Sprite.spawn_wall2);
    public static Tile spawnFloor = new SpawnFloorTile(Sprite.spawn_floor);
    public int width, height;
    public Sprite sprite;


    public Tile(Sprite sprite) {
        this.sprite = sprite;
    }

    public void render(int x, int y, Screen screen) {
    }

    public boolean solid() {
        return false;
    }

    public boolean breakable() {
        return false;
    }
}
