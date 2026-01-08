package ch.nivisan.rain.entity;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.Level;

import java.util.Random;

// Entites do not have sprites, they can be anything from time itself to monsters
public abstract class Entity {
    protected final Random random = new Random();
    public int x, y;
    protected Level level;
    private boolean removed = false;

    public Entity(Level level) {
        this.level = level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void update() {
    }

    public void render(Screen screen) {
    }

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }
}
