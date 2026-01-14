package ch.nivisan.rain.entity.particle;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

public class Particle extends Entity {
    private final Sprite sprite;
    protected double xMovement, yMovement, actualX, actualY;
    // z represents here gravity
    protected double z, zMovement;
    private int lifeTime = 0;
    private int maxLifeTime = 0;

    public Particle(int x, int y, float maxLifeTime, Sprite sprite, Level level) {
        super(level);
        this.x = x;
        this.y = y;
        this.actualX = x;
        this.actualY = y;
        this.maxLifeTime = ((int) (maxLifeTime * 60)) + (random.nextInt(20) - 10);
        this.sprite = Sprite.particleDefault;

        this.xMovement = random.nextGaussian();
        this.yMovement = random.nextGaussian();
        this.z = random.nextFloat() + 2.0;
    }

    @Override
    public void update() {
        lifeTime++;
        if (lifeTime >= 7400) lifeTime = 0;
        if (lifeTime > maxLifeTime) remove();
        zMovement -= 0.1;

        if (z < 0) {
            z = 0;
            zMovement *= -0.55;
            xMovement *= 0.4;
            yMovement *= 0.4;
        }

        move(actualX + xMovement, (actualY + yMovement) + (z + zMovement));
    }

    private void move(double x, double y) {
        if (collision(x, y)) {
            this.xMovement *= -0.5;
            this.yMovement *= -0.5;
            this.zMovement *= -0.5;
        }
        this.actualX += xMovement;
        this.actualY += yMovement;
        this.z += zMovement;
    }


    public boolean collision(double x, double y) {
        boolean solid = false;
        for (int cornerIndex = 0; cornerIndex < 4; cornerIndex++) {
            double xt = (x - cornerIndex % 2 * 16) / 16;
            double yt = (y - cornerIndex / 2 * 16) / 16;
            int ix = (int) Math.ceil(xt);
            int iy = (int) Math.ceil(yt);
            if (cornerIndex % 2 == 0) ix = (int) Math.floor(xt);
            if (cornerIndex / 2 == 0) iy = (int) Math.floor(yt);
            if (level.getTile(ix, iy).solid()) solid = true;
        }
        return solid;
    }

    @Override
    public void render(Screen screen) {
        screen.renderSprite((int) actualX, (int) actualY - (int) z - 1, sprite, true);
    }
}