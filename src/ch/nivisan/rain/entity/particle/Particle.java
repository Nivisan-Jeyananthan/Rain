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
    // z represents here gravity
    protected double z, zMovement;

    public Particle(int x, int y, float maxLifeTime, Sprite sprite, Level level) {
        super(level);
        this.x = x;
        this.y = y;
        this.actualX = x;
        this.actualY = y;
        this.maxLifeTime = (maxLifeTime * 60) + (random.nextInt(50) - 25);
        this.sprite = Sprite.particleDefault;

        this.xMovement = random.nextGaussian();
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
        zMovement -= 0.1;

        if (z < 0) {
            z = 0;
            // reverses direction and slows it down
            zMovement *= -0.55;
            xMovement *= 0.4;
            yMovement *= 0.4;
        }

        move(x + xMovement, (y + yMovement) + (z + zMovement));

    }

    private void move(double x, double y) {
        if(collision(x,y)){
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
        double cornerX = 0, cornerY = 0;
        double vertexAmount = 2;

        for (int cornerIndex = 0; cornerIndex < 4; cornerIndex++) {
            cornerX = (x - cornerIndex % vertexAmount * 16) / 16;
            cornerY = (y - cornerIndex / vertexAmount * 16) / 16;

            int c1 = (int) Math.ceil(cornerX);
            int c2 = (int) Math.ceil(cornerY);

            if (level.getTile(c1,c2).solid()) {
                solid = true;
            }
        }
        return solid;
    }

    @Override
    public void render(Screen screen) {
        screen.renderSprite((int) actualY , (int) actualY , sprite, true);
    }
}