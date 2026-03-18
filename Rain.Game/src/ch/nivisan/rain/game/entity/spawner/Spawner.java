package ch.nivisan.rain.game.entity.spawner;

import ch.nivisan.rain.game.entity.Entity;
import ch.nivisan.rain.game.level.Level;

public abstract class Spawner extends Entity {
    public Spawner(int x, int y, int amount, Level level) {
        super(level);
        this.x = x;
        this.y = y;
    }
}
