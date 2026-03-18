package ch.nivisan.rain.game.entity.spawner;

import ch.nivisan.rain.game.entity.particle.Particle;
import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.Sprite;
import ch.nivisan.rain.game.level.Level;

public class ParticleSpawner extends Spawner {
    private final float lifeTime;
    private boolean infinite;

    public ParticleSpawner(int x, int y, int amount, float lifeTime, Level level) {
        super(x, y, amount, level);
        this.lifeTime = lifeTime;

        for (int i = 0; i < amount; i++) {
            level.addEntity(new Particle(x, y, lifeTime, Sprite.particleDefault, level));
        }
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Screen screen) {

    }
}
