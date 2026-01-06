package ch.nivisan.rain.entity.projectile;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;

public class WizardProjectile extends Projectile {
    public WizardProjectile(int xOrigin, int yOrigin, double angle) {
        super(xOrigin, yOrigin, angle,Sprite.wizardProjectile);
        range = 200;
        damage = 20;
        rateOfFire = 15;
        speed = 4;

        nx = speed * Math.cos(angle);
        ny = speed * Math.sin(angle);
    }

    public void update(){
        move();
    }

    protected void move(){
        x += (int) nx;
        y += (int) ny;
    }

    public void render(Screen screen){
        screen.renderProjectile(x,y,this);
    }
}
