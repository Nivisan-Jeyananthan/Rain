package ch.nivisan.rain.entity.particle;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

public class Particle extends Entity {
    private final int lifeTime;
    private final Sprite sprite;
    protected double xMovement, yMovement, actualX, actualY;

    public Particle(int x, int y, int lifeTime, Sprite sprite, Level level) {
        super(level);
        this.x = x;
        this.y = y;
        this.actualX = x;
        this.actualY = y;
        this.lifeTime = lifeTime;
        this.sprite = Sprite.particleDefault;

        this.xMovement = random.nextGaussian();
        this.yMovement = random.nextGaussian();
    }

    @Override
    public void update() {
        this.actualX += xMovement;
        this.actualY += yMovement;
    }

    @Override
    public void render(Screen screen) {
        screen.renderSprite((int) actualX, (int) actualY, sprite, true);
    }
}
