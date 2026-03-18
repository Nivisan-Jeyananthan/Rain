package ch.nivisan.rain.game.entity.projectile;

import java.util.Random;

import ch.nivisan.rain.game.entity.Entity;
import ch.nivisan.rain.game.graphics.Sprite;
import ch.nivisan.rain.game.level.Level;

public abstract class Projectile extends Entity {
    protected static final Random random = new Random();
    protected final float xOrigin;
    protected final float yOrigin;
    protected final float angle;
    protected Sprite sprite;
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

