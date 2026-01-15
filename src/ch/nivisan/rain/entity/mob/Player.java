package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.Game;
import ch.nivisan.rain.entity.Entity;
import ch.nivisan.rain.entity.projectile.WizardProjectile;
import ch.nivisan.rain.graphics.AnimatedSprite;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.SpriteSheet;
import ch.nivisan.rain.input.Keyboard;
import ch.nivisan.rain.input.Mouse;
import ch.nivisan.rain.level.Level;

import java.util.List;

public class Player extends Mob {
    private static final AnimatedSprite front = new AnimatedSprite(SpriteSheet.playerFront, 32, 32, 3);
    private static final AnimatedSprite back = new AnimatedSprite(SpriteSheet.playerBack, 32, 32, 3);
    private static final AnimatedSprite right = new AnimatedSprite(SpriteSheet.playerRight, 32, 32, 3);
    private static final AnimatedSprite left = new AnimatedSprite(SpriteSheet.playerLeft, 32, 32, 3);
    private final int walkSpeed = 10;
    private final Keyboard input;
    private float fireRate = 0;
    private AnimatedSprite animatedSprite = front;

    public Player(Keyboard input, Level level) {
        super(level);
        this.input = input;
        animatedSprite = front;
    }

    public Player(int x, int y, Keyboard input, Level level) {
        super(level);

        this.x = x;
        this.y = y;
        this.input = input;
        this.fireRate = WizardProjectile.fireRate;
    }

    @Override
    public void update() {
        // List<Entity> entities = level.getEntities(this, 80);


        if (walking) animatedSprite.update();
        else {
            animatedSprite.setFrame(0);
        }
        if (fireRate > 0) fireRate--;

        int xAbsolute = 0, yAbsolute = 0;

        if (input.up) {
            yAbsolute -= walkSpeed;
            animatedSprite = back;
        } else if (input.down) {
            yAbsolute += walkSpeed;
            animatedSprite = front;
        } else if (input.right) {
            xAbsolute += walkSpeed;
            animatedSprite = right;
        } else if (input.left) {
            xAbsolute -= walkSpeed;
            animatedSprite = left;
        }

        if (xAbsolute != 0 || yAbsolute != 0) {
            move(xAbsolute, yAbsolute);
            walking = true;
        } else {
            walking = false;
        }

        updateShooting();
    }


    /**
     * projectile calculations
     */
    private void updateShooting() {
        if (Mouse.getButton() == 1 && fireRate <= 0) {
            int midpointWidth = Game.getWindowWidth() / 2;
            int midpointHeight = Game.getWindowHeight() / 2;

            float dx = (Mouse.getXPosition() - midpointWidth);
            float dy = (Mouse.getYPosition() - midpointHeight);
            double dir = Math.atan2(dy, dx);

            shoot(x, y, dir);
            fireRate = WizardProjectile.fireRate;
        }
    }

    public void render(Screen screen) {
        FlipState flip = FlipState.None;

        float xCenter = x - 16;
        float yCenter = y - 16;

        screen.renderMob((int) xCenter, (int) yCenter, animatedSprite.getSprite(), flip);
    }
}
