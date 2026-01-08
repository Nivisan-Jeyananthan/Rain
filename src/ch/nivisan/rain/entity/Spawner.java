package ch.nivisan.rain.entity;

import ch.nivisan.rain.entity.particle.Particle;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Spawner extends Entity {
    private List<Entity> entities = new ArrayList<Entity>();
    private EntityType type;

    public Spawner(int x, int y, EntityType entityType, int amount, Level level) {
        super(level);
        this.x = x;
        this.y = y;
        this.type = entityType;

        for (int i = 0; i < amount; i++) {
            if (type == EntityType.Particle) {
                level.addEntity(new Particle(x, y, 50, Sprite.particleDefault, level));
            }
        }
    }

    public enum EntityType {
        Mob,
        Particle,
    }
}
