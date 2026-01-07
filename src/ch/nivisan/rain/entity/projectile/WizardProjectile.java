package ch.nivisan.rain.entity.projectile;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;

public class WizardProjectile extends Projectile {
    public static final double fireRate = 15;

    public WizardProjectile(int xOrigin, int yOrigin, double angle) {
        super(xOrigin, yOrigin, angle,Sprite.wizardProjectile);
        range = 200;
        damage = 20;
        speed = 4;

        nx = speed * Math.cos(angle);
        ny = speed * Math.sin(angle);
    }

    public void update(){
        move();
    }

    protected void move(){
        x +=  nx;
        y +=  ny;

        if(calculateDistance() > range){
            remove();
        }
    }

    private double calculateDistance(){
        double distance = 0;
        distance = Math.sqrt(Math.abs(((x - xOrigin) * (x - xOrigin)) + ((y - yOrigin)*(y - yOrigin))));
        return distance;
    }

    public void render(Screen screen){
        screen.renderProjectile((int) x -5,(int) y -11,this);
    }
}
