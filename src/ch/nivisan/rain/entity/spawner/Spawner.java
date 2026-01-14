package ch.nivisan.rain.entity.spawner;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class Spawner extends Entity  {
    public Spawner(int x, int y,  int amount, Level level) {
        super(level);
        this.x = x;
        this.y = y;
    }
}
