package ch.nivisan.rain.entity;

import ch.nivisan.rain.entity.particle.Particle;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Spawner extends Entity {
    private List<Entity> entities = new ArrayList<Entity>();
    private SpawnerType spawnerType;

    public Spawner(int x, int y, SpawnerType spawnerType, int amount, Level level) {
        super(level);
        this.x = x;
        this.y = y;
        this.spawnerType = spawnerType;

        for (int i = 0; i < amount; i++) {
            if (this.spawnerType == SpawnerType.Particle) {
                level.addEntity(new Particle(x, y, 50, Sprite.particleDefault, level));
            }
        }
    }
}
