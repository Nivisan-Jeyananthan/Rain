package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.entity.projectile.Projectile;
import ch.nivisan.rain.entity.projectile.WizardProjectile;
import ch.nivisan.rain.graphics.Sprite;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

// anything that moves and needs to be displayed
public abstract class Mob extends Entity {
    protected Sprite sprite;
    protected Direction facingDirection = Direction.North;
    protected boolean moving = false;
    protected List<Projectile> projectiles = new ArrayList<Projectile>();

    public void move(int xMovement, int yMovement) {
        if (xMovement != 0 && yMovement != 0) {
            move(xMovement, 0);
            move(0, yMovement);
            return;
        }

        if (yMovement < 0) facingDirection = Direction.North;
        if (xMovement > 0) facingDirection = Direction.East;
        if (yMovement > 0) facingDirection = Direction.South;
        if (xMovement < 0) facingDirection = Direction.West;

        if (!collision(xMovement, yMovement)) {
            x += xMovement;
            y += yMovement;
        }
    }

    public void update() {
    }

    public void render() {

    }

    protected void shoot(int x, int y, double direction) {
        direction = Math.toDegrees(direction);
        //  direction *= (180 / Math.PI);

        Projectile p = new WizardProjectile(x,y,direction);
        projectiles.add(p);
        level.addEntity(p);

        System.out.println("Angle: " + direction);
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
    private boolean collision(int xMovement, int yMovement) {
        boolean solid = false;

        int cornerX = 0, cornerY = 0;
        int vertexAmount = 2;
        int collisionWidth = 14;
        int collisionHeight = 12;

        for (int cornerIndex = 0; cornerIndex < 4; cornerIndex++) {
            cornerX = ((xMovement + x) + (cornerIndex % vertexAmount) * collisionWidth - 8) >> 4;
            cornerY = ((yMovement + y) + (cornerIndex / vertexAmount) * collisionHeight + 3) >> 4;

            if (level.getTile(cornerX, cornerY).solid()) return true;
        }
        return solid;

    }
}
