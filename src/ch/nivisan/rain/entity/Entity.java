package ch.nivisan.rain.entity;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.Level;

import java.util.Random;

// Entites do not have sprites, they can be anything from time itself to monsters
public abstract class Entity {
    public int x,y;
    private boolean removed = false;
    protected Level level;
    protected final Random random = new Random();

    public void init(Level level){
        this.level = level;
    }

    public void update(Screen screen){
    }

    public void remove(){
        removed = true;
    }

    public boolean isRemoved(){
        return removed;
    }
}
