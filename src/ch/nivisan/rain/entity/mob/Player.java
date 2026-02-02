package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.entity.projectile.ShurikenProjectile;
import ch.nivisan.rain.entity.projectile.WizardProjectile;
import ch.nivisan.rain.graphics.*;
import ch.nivisan.rain.graphics.gui.UILabeledProgressbar;
import ch.nivisan.rain.input.Keyboard;
import ch.nivisan.rain.input.Mouse;
import ch.nivisan.rain.level.Level;
import ch.nivisan.rain.utils.Debug;

public class Player extends Mob {
    private static final AnimatedSprite front = new AnimatedSprite(SpriteSheet.playerFront, 32, 32, 3);
    private static final AnimatedSprite back = new AnimatedSprite(SpriteSheet.playerBack, 32, 32, 3);
    private static final AnimatedSprite right = new AnimatedSprite(SpriteSheet.playerRight, 32, 32, 3);
    private static final AnimatedSprite left = new AnimatedSprite(SpriteSheet.playerLeft, 32, 32, 3);
    private final Keyboard input;
    private final String name;
    // todo: refactor creation of ui to playerUI
    private final PlayerUI playerUI;
    int time = 0;
    private float fireRate = 0;
    private AnimatedSprite animatedSprite = front;
    private UILabeledProgressbar uiHealthBar;

    public Player(String name, int x, int y, Keyboard input, Level level) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.input = input;
        this.fireRate = ShurikenProjectile.fireRate;
        walkSpeed = 1.4f;
        health = 0;
        maxHealth = 100;

        playerUI = new PlayerUI(this);
    }

    public String getName() {
        return name;
    }

    @Override
    public void update() {
        playerUI.update();

        time++;
        if (time % 20 == 0) {
            if(health < maxHealth)
                health++;
            time = 0;
        }

        if (walking) animatedSprite.update();
        else {
            animatedSprite.setFrame(0);
        }
        if (fireRate > 0) fireRate--;

        float xAbsolute = 0, yAbsolute = 0;

        if (input.up) {
            yAbsolute -= walkSpeed;
            animatedSprite = back;
        } else if (input.down) {
            yAbsolute += walkSpeed;
            animatedSprite = front;
        }
        if (input.right) {
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
            int midpointWidth = WindowManager.getScaledGameWidth() / 2;
            int midpointHeight = WindowManager.getScaledWindowHeight() / 2;

            float dx = (Mouse.getXPosition() - midpointWidth);
            float dy = (Mouse.getYPosition() - midpointHeight);
            float dir = (float) Math.atan2(dy, dx);

            shoot(x, y, dir);
            fireRate = WizardProjectile.fireRate;
        }
    }

    public void render(Screen screen) {
        float xCenter = x - 16;
        float yCenter = y - 30;

        Debug.drawRectangle(screen, 20 << 4, 60 << 4, 100, 40, 0xff000, true);
        screen.renderMob((int) xCenter, (int) yCenter, animatedSprite.getSprite(), FlipState.None);
    }
}
