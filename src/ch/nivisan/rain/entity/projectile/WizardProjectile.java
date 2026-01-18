package ch.nivisan.rain.entity.projectile;

import ch.nivisan.rain.entity.spawner.ParticleSpawner;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

public class WizardProjectile extends Projectile {
    // refers to the rate of how many per second (60 = 1)
    public static final float fireRate = 60f;

    public WizardProjectile(float xOrigin, float yOrigin, double angle, Level level) {
        super(xOrigin, yOrigin, angle, Sprite.wizardProjectile, level);
        range = 200;
        damage = 20;
        speed = 4;

        // angle and vector length
        nx = (float)(speed * Math.cos(angle));
        ny = (float)(speed * Math.sin(angle));
    }

    public void update() {
        removeOnCollision();
        move();
    }

    protected void move() {
        x += nx;
        y += ny;
    }

    private void removeOnCollision() {
        if (calculateDistance() > range || level.tileCollision((int) (x + nx), (int) (y + ny), 4, 4, 8)) {
            level.addEntity(new ParticleSpawner((int) x, (int) y, 500, 1.0f, level));
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
