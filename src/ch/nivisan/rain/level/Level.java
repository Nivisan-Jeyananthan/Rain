package ch.nivisan.rain.level;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.entity.mob.Player;
import ch.nivisan.rain.entity.particle.Particle;
import ch.nivisan.rain.entity.projectile.Projectile;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.tile.Tile;

import java.util.ArrayList;
import java.util.List;

public class Level {
    public static Level spawn = new SpawnLevel("../assets/levels/spawn2.png");
    private final List<Entity> entities = new ArrayList<Entity>();
    private final List<Projectile> projectiles = new ArrayList<Projectile>();
    private final List<Particle> particles = new ArrayList<Particle>();
    private final List<Player> players = new ArrayList<Player>();
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
    }

    public List<Entity> getEntities(Entity e, int radius){
        List<Entity> result = new ArrayList<>();
        int ex = e.getX();
        int ey = e.getY();

        for (Entity entity : entities) {
            int x = entity.getX();
            int y = entity.getY();

            int dx = Math.abs(x - ex);
            int dy = Math.abs(y - ey);
            double distance = Math.sqrt((dx * dx) + (dy * dy));
            if (distance <= radius) {
                result.add(entity);
            }
        }

        return  result;
    }

    public List<Player> getPlayers(Entity e, int radius){
        List<Player> result = new ArrayList<>();
        int ex = e.getX();
        int ey = e.getY();

        for (Player entity : players) {
            int x = entity.getX();
            int y = entity.getY();

            int dx = Math.abs(x - ex);
            int dy = Math.abs(y - ey);
            double distance = Math.sqrt((dx * dx) + (dy * dy));

            if (distance <= radius) {
                result.add(entity);
            }
        }

        return  result;
    }

    public Player getClientPlayer(){
        return players.getFirst();
    }

    public Player getPlayer(int index){
        return players.get(index);
    }

    public List<Player> getPlayers(){
        return players;
    }

    public void addEntity(Entity entity) {
        switch (entity) {
            case Particle particle -> particles.add(particle);
            case Projectile projectile -> projectiles.add(projectile);
            case Player player -> players.add(player);
            case null, default -> entities.add(entity);
        }
    }

    protected void generateLevel() {
    }

    protected void loadLevel(String path) {
    }

    private void time() {
    }

    /**
     * Checks if any of the upcoming 4 tiles overlaps with our player which would
     * cause a collision
     * when it would, returns true, making our player not able to walk past it
     * Divides by 4 so it is in tile system not pixel.
     * Checks based on offset, useful when having a sprite that is in center or does not fill entire
     * sprite.
     *
     * @param y       position y
     * @param x       position x
     * @param size    the size of the sprite without surroundings (width)
     * @param xOffset the pixel offset in x direction left to right
     * @param yOffset the pixel offset in y direction from top down
     * @return
     */
    public boolean tileCollision(int x, int y, int xOffset, int yOffset, int size) {
        boolean solid = false;
        int cornerX = 0, cornerY = 0;
        int vertexAmount = 2;

        for (int cornerIndex = 0; cornerIndex < 4; cornerIndex++) {
            cornerX = (x - (cornerIndex % vertexAmount) * size + xOffset) >> 4;
            cornerY = (y - (cornerIndex / vertexAmount) * size + yOffset) >> 4;

            if (getTile(cornerX, cornerY).solid())
                return true;
        }
        return solid;
    }

    public void update() {
        updateEntities();
        updateProjectiles();
        updateParticles();
        updatePlayers();

    }

    private void updateEntities() {
        for (int i = 0; i < entities.size(); i++) {
            var entity = entities.get(i);
            if (entity.isRemoved()) {
                entities.remove(entity);
            } else {
                entity.update();
            }
        }
    }

    private void updateProjectiles() {
        for (int i = 0; i < projectiles.size(); i++) {
            var projectile = projectiles.get(i);
            if (projectile.isRemoved()) {
                projectiles.remove(projectile);
            } else {
                projectile.update();
            }
        }
    }

    private void updateParticles() {
        for (int i = 0; i < particles.size(); i++) {
            var particle = particles.get(i);
            if (particle.isRemoved()) {
                particles.remove(particle);
            } else {
                particle.update();
            }
        }
    }

    private void updatePlayers() {
        for (int i = 0; i < players.size(); i++) {
            var player = players.get(i);
            if (player.isRemoved()) {
                players.remove(player);
            } else {
                player.update();
            }
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
                return Tile.grass;
            }
            case Tile.color_spawnHedge -> {
                return Tile.spawnHedge;
            }
            case Tile.color_spawnWall1 -> {
                return Tile.wallsStone;
            }
            case Tile.color_spawnWall2 -> {
                return Tile.wallsRock;
            }
            case Tile.color_spawnFloor -> {
                return Tile.woodFloor;
            }
            case Tile.color_spawnFloor2 -> {
                return Tile.woodFloorBase;
            }
            case Tile.color_spawnWater -> {
                return Tile.spawnWater;
            }
            default -> {
                return Tile.empty;
            }
        }
    }

    public void render(int xScroll, int yScroll, Screen screen) {

        screen.setOffsets(xScroll, yScroll);

        renderTiles(xScroll, yScroll, screen);
        renderEntities(screen);
        renderProjectiles(screen);
        renderParticles(screen);
        renderPlayers(screen);
    }

    private void renderTiles(int xScroll, int yScroll, Screen screen) {
        int tileSize = 16;
        // defines render region of the current visible window region:

        // same as (xScroll / 16) = divides into tiles of 16
        // set the 4 corner pins (x,y) top left, bottom right (x,y)
        // convert from pixel precision to tile based so we do not have to deal with
        // huge numbers
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

    private void renderPlayers(Screen screen){
        for (Player player : players) {
            player.render(screen);
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
}
