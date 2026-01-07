package ch.nivisan.rain.entity.projectile;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.graphics.Sprite;

import java.util.Random;

public abstract class Projectile extends Entity {
    protected final int xOrigin;
    protected final int yOrigin;
    protected final double angle;
    protected final Sprite sprite;
    protected double nx,ny;
    protected double speed, range, damage;
    protected double x,y;
    protected static final Random random = new Random();

    public Projectile(int x, int y, double angle, Sprite sprite){
        this.angle = angle;
        this.xOrigin = x;
        this.yOrigin = y;
        this.sprite = sprite;
        this.x = x;
        this.y = y;
    }

    public Sprite getSprite(){
        return sprite;
    }
}

