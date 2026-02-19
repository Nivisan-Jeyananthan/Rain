package ch.nivisan.rain.entity.projectile;

import ch.nivisan.rain.Game;
import ch.nivisan.rain.entity.spawner.ParticleSpawner;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

public class ShurikenProjectile extends Projectile {
    // refers to the rate of how many per second (60 = 1)
    public static final float fireRate = 20.0f;
    private int time = 0;

    public ShurikenProjectile(float xOrigin, float yOrigin, float angle, Level level) {
        super(xOrigin, yOrigin, angle, Sprite.rotateSprite(Sprite.wizardProjectile, angle), level);
        range = 200;
        damage = 20;
        speed = 1;

        // angle and vector length
        nx = (float) (speed * Math.cos(angle));
        ny = (float) (speed * Math.sin(angle));
    }

    public void update() {
        time++;
        if (time % (Game.framerate / 10) == 0) {
            sprite = Sprite.rotateSprite(sprite, (float) (Math.PI / 20.0));
        }
        removeOnCollision();
        move();
    }

    protected void move() {
        x += nx;
        y += ny;
    }

    private void removeOnCollision() {
        if (calculateDistance() > range || level.tileCollision((int) (x + nx), (int) (y + ny), 4, 4, 8)) {
            level.addEntity(new ParticleSpawner((int) x, (int) y, 20, 1.0f, level));
            remove();
        }
    }

    /**
     * Calculates the length of the vector
     *
     * @return
     */
    private float calculateDistance() {
        float distance = 0;
        distance = (float) Math.sqrt(Math.abs(((xOrigin - x) * (xOrigin - x)) + ((yOrigin - y) * (yOrigin - y))));
        return distance;
    }

    public void render(Screen screen) {
        screen.renderProjectile((int) x, (int) (y), this);
    }
}
