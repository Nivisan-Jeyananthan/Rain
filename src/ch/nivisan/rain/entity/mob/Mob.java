package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.graphics.Sprite;

// anything that moves and needs to be displayed
public abstract class Mob extends Entity {
    protected Sprite sprite;
    protected Direction facingDirection = Direction.North;
    protected boolean moving = false;

    public void move(int xMovement, int yMovement){
        if(yMovement < 0) facingDirection = Direction.North;
        if(xMovement > 0) facingDirection = Direction.East;
        if(yMovement > 0) facingDirection = Direction.South;
        if(xMovement < 0) facingDirection = Direction.West;

        if(!collision(xMovement,0)){
            x += xMovement;
        }

        if(!collision(0,yMovement)){
            y += yMovement;
        }
    }

    public void update(){}

    public void render(){

    }

    private boolean collision(int xMovement, int yMovement){
        boolean solid = false;

        return level.getTile(((xMovement + x) >> 4), ((yMovement+y) >> 4)).solid();
    }
}
