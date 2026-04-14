package ch.nivisan.rain.game.entity;

import java.util.Random;

import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.level.Level;

//Entites do not have sprites, they can be anything from timer itself to monsters
public abstract class Entity {
 protected static final Random random = new Random();
 protected float x, y;
 protected Level level;
 private boolean removed = false;

 public Entity(Level level) {
     this.level = level;
 }

 public float getX() {
     return x;
 }

 public float getY() {
     return y;
 }

 public void setLevel(Level level) {
     this.level = level;
 }

 public abstract void update();

 public abstract void render(Screen screen);

 public void remove() {
     removed = true;
 }

 public boolean isRemoved() {
     return removed;
 }
}
