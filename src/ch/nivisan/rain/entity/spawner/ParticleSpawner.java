package ch.nivisan.rain.entity.spawner;

import ch.nivisan.rain.entity.particle.Particle;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

public class ParticleSpawner extends Spawner {
    private final float lifeTime;
    private boolean infinite;

    public ParticleSpawner(int x, int y, int amount, float lifeTime, Level level) {
        super(x, y, amount, level);
        this.lifeTime = lifeTime;

        for (int i = 0; i < amount; i++) {
            level.addEntity(new Particle(x, y, 0.5f, Sprite.particleDefault, level));
        }
    }
}
