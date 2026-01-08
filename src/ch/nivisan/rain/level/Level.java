package ch.nivisan.rain.level;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.entity.Spawner;
import ch.nivisan.rain.entity.SpawnerType;
import ch.nivisan.rain.entity.particle.Particle;
import ch.nivisan.rain.entity.projectile.Projectile;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Level {
    public static Level spawn = new SpawnLevel("../assets/levels/spawn.png");
    private final List<Entity> entities = new ArrayList<Entity>();
    private final List<Projectile> projectiles = new ArrayList<Projectile>();
    private final List<Particle> particles = new ArrayList<Particle>();
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
    public Level(String path) {
        loadLevel(path);
        generateLevel();

        addEntity(new Spawner(21 * 16, 60 * 16, SpawnerType.Particle, 50, this));
    }

    public void addEntity(Entity entity) {
        if (entity instanceof Particle) {
            particles.add((Particle) entity);
        } else if (entity instanceof Projectile) {
            projectiles.add((Projectile) entity);
        } else {
            entities.add(entity);
        }
    }

    protected void generateLevel() {
    }

    protected void loadLevel(String path) {
    }

    private void time() {
    }

    /**
     * Checks if any of the upcoming 4 tiles overlaps with our player which would cause a collision
     * when it would, returns true, making our player not able to walk past it
     * Divides by 4 so it is in tile system not pixel
     *
     * @param xMovement
     * @param yMovement
     * @return
     */
    public boolean tileCollision(double x, double y, double xMovement, double yMovement, int size) {
        boolean solid = false;

        int cornerX = 0, cornerY = 0;
        int vertexAmount = 2;

        for (int cornerIndex = 0; cornerIndex < 4; cornerIndex++) {
            cornerX = (((int) xMovement + (int) x) + (cornerIndex % 2) * size / 8) / 16;
            cornerY = (((int) yMovement + (int) y) + (cornerIndex / 2) * size / 8) / 16;

            if (getTile((int) cornerX, (int) cornerY).solid()) return true;
        }
        return solid;
    }

    public void update() {
        for (Entity entity : entities) {
            entity.update();
        }

        for (int i = 0; i < projectiles.size(); i++) {
            var projectile = projectiles.get(i);
            if (projectile.isRemoved()) {
                projectiles.remove(projectile);
            } else {
                projectile.update();
            }
        }

        for (Particle particle : particles) {
            particle.update();
        }

    }

    public void render(int xScroll, int yScroll, Screen screen) {

        screen.setOffsets(xScroll, yScroll);

        renderTiles(xScroll, yScroll, screen);
        renderEntities(screen);
        renderProjectiles(screen);
        renderParticles(screen);
    }

    private void renderTiles(int xScroll, int yScroll, Screen screen) {
        int tileSize = 16;
        // defines render region of the current visible window region:

        // same as (xScroll / 16) = divides into tiles of 16
        // set the 4 corner pins (x,y) top left, bottom right (x,y)
        // convert from pixel precision to tile based so we do not have to deal with huge numbers
        int xStart = xScroll >> 4;
        // without adding the tilesize we would get black bars around right and bottom
        int xEnd = (xScroll + screen.width + tileSize) >> 4;
        int yStart = yScroll >> 4;
        int yEnd = (yScroll + screen.height + tileSize) >> 4;

        for (int y = yStart; y < yEnd; y++) {
            for (int x = xStart; x < xEnd; x++) {
                getTile(x, y).render(x, y, screen);
            }
        }
    }

    private void renderParticles(Screen screen) {
        for (Particle particle : particles) {
            particle.render(screen);
        }
    }

    private void renderProjectiles(Screen screen) {
        for (Projectile projectile : projectiles) {
            projectile.render(screen);
        }
    }

    private void renderEntities(Screen screen) {
        for (Entity entity : entities) {
            entity.render(screen);
        }
    }

    // convert pixel position data to tile position data
    public Tile getTile(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height)
            return Tile.empty;

        int index = x + (y * width);
        int color = tiles[index];
        switch (color) {
            case Tile.color_spawnGrass -> {
                return Tile.spawnGrass;
            }
            case Tile.color_spawnHedge -> {
                return Tile.spawnHedge;
            }
            case Tile.color_spawnWall1 -> {
                return Tile.spawnWall1;
            }
            case Tile.color_spawnWall2 -> {
                return Tile.spawnWall2;
            }
            case Tile.color_spawnFloor -> {
                return Tile.spawnFloor;
            }
            case Tile.color_spawnWater -> {
                return Tile.spawnWater;
            }
            default -> {
                return Tile.empty;
            }
        }
    }
}
