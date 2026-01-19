package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.entity.projectile.Projectile;
import ch.nivisan.rain.entity.projectile.WizardProjectile;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

// anything that moves and needs to be displayed
public abstract class Mob extends Entity {
    protected Sprite sprite;
    protected Direction facingDirection = Direction.North;
    protected boolean moving = false;
    protected boolean walking = false;
    protected float walkSpeed;


    public Mob(Level level) {
        super(level);
    }

    protected void move(float xMovement, float yMovement) {
        if (xMovement != 0 && yMovement != 0) {
            move(xMovement, 0);
            move(0, yMovement);
            return;
        }

        if (yMovement < 0) facingDirection = Direction.North;
        if (xMovement > 0) facingDirection = Direction.East;
        if (yMovement > 0) facingDirection = Direction.South;
        if (xMovement < 0) facingDirection = Direction.West;

        while(xMovement != 0){
            if((Math.abs(xMovement)) > 1){
                if (!collision(abs(xMovement), yMovement)) {
                    this.x += abs(xMovement);
                }
                xMovement -= abs(xMovement);
            } else {
                if (!collision(abs(xMovement), yMovement)) {
                    this.x += xMovement;
                }
                xMovement = 0;
            }
        }

        while(yMovement != 0){
            if((Math.abs(yMovement)) > 1){
                if (!collision(xMovement, abs(yMovement))) {
                    this.y += abs(yMovement);
                }
                yMovement -= abs(yMovement);
            } else {
                if (!collision(xMovement, abs(yMovement))) {
                    this.y += yMovement;
                }
                yMovement = 0;
            }
        }
    }

    /**
     * So we do not move to many pixels at once, only 1 unit movement is allowed at once
     * @param movement
     * @return
     */
    private int abs(double movement) {
        if (movement < 0) return -1;
        return 1;
    }

    public abstract void update();

    public void render(Screen screen) {
        screen.renderMob((int) x - 16, (int) y - 32, sprite, FlipState.None);
    }

    protected void shoot(float x, float y, double direction) {
        Projectile p = new WizardProjectile(x, y, direction, level);
        level.addEntity(p);
    }

    /**
     * Checks if any of the upcoming 4 tiles overlaps with our player which would cause a collision
     * when it would, returns true, making our player not able to walk past it
     * Divides by 4 so it is in tile system not pixel
     *
     * @param xMovement
     * @param yMovement
     * @return
     */
    private boolean collision(double xMovement, double yMovement) {
        boolean solid = false;

        double cornerX = 0, cornerY = 0;
        int vertexAmount = 2;

        for (int cornerIndex = 0; cornerIndex < 4; cornerIndex++) {
            cornerX = ((xMovement + x) - (cornerIndex % vertexAmount) * 15) / 16;
            cornerY = ((yMovement + y) - (cornerIndex / vertexAmount) * 15) / 16;

            int ix = (int) Math.ceil(cornerX);
            int iy = (int) Math.ceil(cornerY);

            if (cornerIndex % 2 == 0) ix = (int) Math.floor(cornerX);
            if (cornerIndex / 2 == 0) iy = (int) Math.floor(cornerY);

            if (level.getTile(ix, iy).solid()) solid = true;
        }
        return solid;
    }

    public Sprite getSprite() {
        return sprite;
    }
}
