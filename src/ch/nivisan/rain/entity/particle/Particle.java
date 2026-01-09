package ch.nivisan.rain.entity.particle;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

public class Particle extends Entity {
    private int lifeTime = 0;
    private float maxLifeTime = 0;
    private final Sprite sprite;
    protected double xMovement, yMovement, actualX, actualY;
    protected double z, gravityMovement;

    public Particle(int x, int y, float maxLifeTime, Sprite sprite, Level level) {
        super(level);
        this.x = x;
        this.y = y;
        this.actualX = x;
        this.actualY = y;
        this.maxLifeTime = (maxLifeTime * 60) + (random.nextInt(50) - 25);
        this.sprite = Sprite.particleDefault;

        this.xMovement = random.nextGaussian() + 1.8;
        if (this.xMovement < 0) {
            xMovement = 0.1;
        }
        this.yMovement = random.nextGaussian();
        this.z = random.nextFloat() + 2.0;
    }

    @Override
    public void update() {
        lifeTime++;
        if (lifeTime >= 7400) {
            lifeTime = 0;
        }

        if (lifeTime > maxLifeTime) {
            remove();
        }

        if (z < 0) {
            z = 0;
            gravityMovement *= -0.5;
            xMovement *= 0.4;
            yMovement *= 0.4;
        }

        gravityMovement -= 0.1;

        this.actualX += xMovement;
        this.actualY += yMovement;
        this.z += gravityMovement;
    }

    @Override
    public void render(Screen screen) {
        screen.renderSprite((int) actualX - 3, (int) actualY - (int) z, sprite, true);
    }
}