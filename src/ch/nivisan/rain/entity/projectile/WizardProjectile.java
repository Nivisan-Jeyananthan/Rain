package ch.nivisan.rain.entity.projectile;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.entity.spawner.ParticleSpawner;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

public class WizardProjectile extends Projectile {
    public static final double fireRate = 30;

    public WizardProjectile(int xOrigin, int yOrigin, double angle, Level level) {
        super(xOrigin, yOrigin, angle, Sprite.wizardProjectile, level);
        range = 200;
        damage = 20;
        speed = 4;

        nx = speed * Math.cos(angle);
        ny = speed * Math.sin(angle);
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
        if (calculateDistance() > range || level.tileCollision((int) (x + nx), (int) (y + ny),4,4, 8)) {
            level.addEntity(new ParticleSpawner((int) x, (int) y, 3, 3.0f, level));
            remove();
        }
    }

    /**
     * Calculates the length of the vector
     * @return
     */
    private double calculateDistance() {
        double distance = 0;
        distance = Math.sqrt(Math.abs(((x - xOrigin) * (x - xOrigin)) + ((y - yOrigin) * (y - yOrigin))));
        return distance;
    }

    public void render(Screen screen) {
        screen.renderProjectile((int) x - 5, (int) y - 11, this);
    }
}
