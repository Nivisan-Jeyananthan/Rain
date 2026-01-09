package ch.nivisan.rain.entity.spawner;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Spawner extends Entity  {

    private List<Entity> entities = new ArrayList<Entity>();

    public Spawner(int x, int y,  int amount, Level level) {
        super(level);
        this.x = x;
        this.y = y;
    }
}
