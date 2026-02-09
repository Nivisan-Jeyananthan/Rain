package ch.nivisan.rain.level;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.entity.mob.Mob;
import ch.nivisan.rain.entity.mob.Player;
import ch.nivisan.rain.entity.particle.Particle;
import ch.nivisan.rain.entity.projectile.Projectile;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.layers.Layer;
import ch.nivisan.rain.level.tile.Tile;
import ch.nivisan.rain.utils.Node;
import ch.nivisan.rain.utils.Vector2;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Level extends Layer {

    public static Level spawn = new SpawnLevel("../assets/levels/spawn2.png");
    private final List<Entity> entities = new ArrayList<Entity>();
    private final List<Projectile> projectiles = new ArrayList<Projectile>();
    private final List<Particle> particles = new ArrayList<Particle>();
    private final List<Player> players = new ArrayList<Player>();
    private final List<Mob> mobs = new ArrayList<>();

    protected int width;
    protected int height;
    protected int[] tiles;
    private int xScroll;
    private int yScroll;

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

    /**
     * Gets path to target using A* algorithm
     *
     * @param start from where we start from.
     * @param goal  where the target is located at
     * @return
     */
    public List<Node> getPath(Vector2 start, Vector2 goal) {
        List<Node> openQueue = new ArrayList<Node>();
        List<Node> closedQueue = new ArrayList<Node>();
        Node currentNode = new Node(start, null, 0, start.getDistance(goal));
        openQueue.add(currentNode);

        while (!openQueue.isEmpty()) {
            Collections.sort(openQueue);

            currentNode = openQueue.getFirst();
            if (currentNode.tile.equals(goal)) {
                List<Node> path = new ArrayList<Node>();
                while (currentNode.parent != null) {
                    path.add(currentNode);
                    currentNode = currentNode.parent;
                }
                openQueue.clear();
                closedQueue.clear();
                return path;
            }

            openQueue.remove(currentNode);
            closedQueue.add(currentNode);
            for (int i = 0; i < 9; i++) {
                if (i == 4) continue;
                int x = currentNode.tile.getX();
                int y = currentNode.tile.getY();
                // ranges from -1,0, 1
                int xDirection = (i % 3) - 1;
                int yDirection = (i / 3) - 1;
                Tile at = getTile(x + xDirection, y + yDirection);
                if (at == null || at.solid()) continue;
                Vector2 atVector = new Vector2(x + xDirection, y + yDirection);
                // compare immediate distance (from middle (current) to other tiles in "circle")
                float gCost =
                    currentNode.gCost +
                    ((currentNode.tile.getDistance(atVector) == 1
                            ? 1.0f
                            : 0.95f));
                float hCost = atVector.getDistance(goal);
                Node node = new Node(atVector, currentNode, gCost, hCost);

                if (
                    isInList(closedQueue, atVector) &&
                    gCost >= currentNode.gCost
                ) continue;
                if (
                    !isInList(openQueue, atVector) || gCost < node.gCost
                ) openQueue.add(node);
            }
        }

        closedQueue.clear();
        return null;
    }

    private boolean isInList(List<Node> list, Vector2 vector) {
        for (Node node : list) {
            if (node.tile.equals(vector)) return true;
        }
        return false;
    }

    public List<Mob> getMobs(Entity e, int radius) {
        List<Mob> result = new ArrayList<>();
        float ex = e.getX();
        float ey = e.getY();

        for (Mob mob : mobs) {
            float x = mob.getX();
            float y = mob.getY();

            float dx = Math.abs(x - ex);
            float dy = Math.abs(y - ey);
            float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
            if (distance <= radius) {
                result.add(mob);
            }
        }

        return result;
    }

    public List<Entity> getEntities(Entity e, int radius) {
        List<Entity> result = new ArrayList<>();
        float ex = e.getX();
        float ey = e.getY();

        for (Entity entity : entities) {
            float x = entity.getX();
            float y = entity.getY();

            float dx = Math.abs(x - ex);
            float dy = Math.abs(y - ey);
            float distance = (float) Math.sqrt((dx * dx) + (dy * dy));
            if (distance <= radius) {
                result.add(entity);
            }
        }

        return result;
    }

    public List<Player> getPlayers(Entity e, int radius) {
        List<Player> result = new ArrayList<>();
        float ex = e.getX();
        float ey = e.getY();

        for (Player entity : players) {
            float x = entity.getX();
            float y = entity.getY();

            float dx = Math.abs(x - ex);
            float dy = Math.abs(y - ey);
            float distance = (float) Math.sqrt((dx * dx) + (dy * dy));

            if (distance <= radius) {
                result.add(entity);
            }
        }
        return result;
    }

    public Player getClientPlayer() {
        return players.getFirst();
    }

    public Player getPlayer(int index) {
        return players.get(index);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addEntity(Entity entity) {
        switch (entity) {
            case Particle particle -> particles.add(particle);
            case Projectile projectile -> projectiles.add(projectile);
            case Player player -> players.add(player);
            case Mob mob -> mobs.add(mob);
            case null, default -> entities.add(entity);
        }
    }

    protected void generateLevel() {}

    protected void loadLevel(String path) {}

    private void time() {}

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
    public boolean tileCollision(
        int x,
        int y,
        int xOffset,
        int yOffset,
        int size
    ) {
        boolean solid = false;
        int cornerX = 0,
            cornerY = 0;
        int vertexAmount = 2;

        for (int cornerIndex = 0; cornerIndex < 4; cornerIndex++) {
            cornerX = (x - (cornerIndex % vertexAmount) * size + xOffset) >> 4;
            cornerY = (y - (cornerIndex / vertexAmount) * size + yOffset) >> 4;

            if (getTile(cornerX, cornerY).solid()) return true;
        }
        return solid;
    }

    public void update() {
        updateEntities();
        updateProjectiles();
        updateParticles();
        updatePlayers();
        updateMobs();
    }

    private void updateMobs() {
        for (int i = 0; i < mobs.size(); i++) {
            var mob = mobs.get(i);
            if (mob.isRemoved()) {
                mobs.remove(mob);
            } else {
                mob.update();
            }
        }
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
        if (x < 0 || y < 0 || x >= width || y >= height) return Tile.empty;

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

    public void setScrolls(int xScroll, int yScroll) {
        this.xScroll = xScroll;
        this.yScroll = yScroll;
    }

    public void render(Screen screen) {
        screen.setOffsets(xScroll, yScroll);

        renderTiles(screen);
        renderEntities(screen);
        renderProjectiles(screen);
        renderParticles(screen);
        renderPlayers(screen);
        renderMobs(screen);
    }

    private void renderTiles(Screen screen) {
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

    private void renderMobs(Screen screen) {
        for (Mob mob : mobs) {
            mob.render(screen);
        }
    }

    private void renderPlayers(Screen screen) {
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
