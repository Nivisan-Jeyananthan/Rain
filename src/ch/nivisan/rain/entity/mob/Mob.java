package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.graphics.Sprite;

// anything that moves and needs to be displayed
public abstract class Mob extends Entity {
    protected Sprite sprite;
    protected int facingDirection = 0;
    protected boolean moving = false;

    public void move(int xAbsolute, int yAbsolute){
        if(xAbsolute > 0) facingDirection = 1;
        if(xAbsolute < 0) facingDirection = 3;
        if(yAbsolute > 0) facingDirection = 2;
        if(yAbsolute < 0) facingDirection = 0;

        if(!collision()){
            x += xAbsolute;
            y += yAbsolute;
        }
    }

    public void update(){}

    public void render(){

    }

    private boolean collision(){
        return false;
    }
}
