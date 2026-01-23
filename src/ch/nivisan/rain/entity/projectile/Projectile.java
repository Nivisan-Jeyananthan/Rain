package ch.nivisan.rain.entity.projectile;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

import java.util.Random;

public abstract class Projectile extends Entity {
    protected static final Random random = new Random();
    protected final float xOrigin;
    protected final float yOrigin;
    protected final float angle;
    protected final Sprite sprite;
    protected float nx, ny;
    protected float speed, range, damage;

    public Projectile(float x, float y, float angle, Sprite sprite, Level level) {
        super(level);
        this.angle = angle;
        this.xOrigin = x;
        this.yOrigin = y;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public Sprite getSprite() {
        return sprite;
    }
}

