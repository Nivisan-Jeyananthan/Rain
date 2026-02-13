package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.entity.projectile.ShurikenProjectile;
import ch.nivisan.rain.entity.projectile.WizardProjectile;
import ch.nivisan.rain.events.Event;
import ch.nivisan.rain.events.EventDispatcher;
import ch.nivisan.rain.events.EventType;
import ch.nivisan.rain.events.IEventListener;
import ch.nivisan.rain.events.types.MousePressedEvent;
import ch.nivisan.rain.events.types.MouseReleasedEvent;
import ch.nivisan.rain.graphics.AnimatedSprite;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.SpriteSheet;
import ch.nivisan.rain.graphics.WindowManager;
import ch.nivisan.rain.graphics.gui.PlayerUI;
import ch.nivisan.rain.graphics.gui.UILabeledProgressbar;
import ch.nivisan.rain.input.Keyboard;
import ch.nivisan.rain.input.Mouse;
import ch.nivisan.rain.input.MouseButton;
import ch.nivisan.rain.level.Level;
import ch.nivisan.rain.utils.Debug;

import java.awt.event.MouseEvent;

public class Player extends Mob implements IEventListener {
    private static final AnimatedSprite front = new AnimatedSprite(SpriteSheet.playerFront, 32, 32, 3);
    private static final AnimatedSprite back = new AnimatedSprite(SpriteSheet.playerBack, 32, 32, 3);
    private static final AnimatedSprite right = new AnimatedSprite(SpriteSheet.playerRight, 32, 32, 3);
    private static final AnimatedSprite left = new AnimatedSprite(SpriteSheet.playerLeft, 32, 32, 3);
    private final Keyboard input;
    private final String name;
    private final PlayerUI playerUI;
    int time = 0;
    private float fireRate = 0;
    private boolean shooting = false;
    private AnimatedSprite animatedSprite = front;

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
    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(EventType.MOUSE_PRESSED,(e) -> onMousePressed((MousePressedEvent) e));
        dispatcher.dispatch(EventType.MOUSE_RELEASED,(e) -> onMouseReleased((MouseReleasedEvent) e));
    }

    @Override
    public void update() {
        playerUI.update();

        time++;
        if (time % 20 == 0) {
            if (health < maxHealth)
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

        if (shooting) {
            updateShooting();
        }

    }

    public boolean onMousePressed(MousePressedEvent e) {
        if(e.getX() > (WindowManager.getScaledWindowWidth() - WindowManager.getScaledGUIWidth()) || e.getY() > WindowManager.getScaledWindowHeight() || e.getY() < 0 || e.getX() < 0)
            return false;

        if (e.getButton() == MouseButton.Left.getNumValue() && fireRate <= 0) {
            shooting = true;
            return true;
        }
        return false;
    }

    public boolean onMouseReleased(MouseReleasedEvent e) {
        if(e.getButton() == MouseButton.Left.getNumValue() && fireRate <= 0) {
            shooting = false;
            return true;
        }
        return false;
    }

    /**
     * projectile calculations
     */
    private void updateShooting() {
        int midpointWidth = WindowManager.getScaledGameWidth() / 2;
        int midpointHeight = WindowManager.getScaledWindowHeight() / 2;

        float dx = (Mouse.getXPosition() - midpointWidth);
        float dy = (Mouse.getYPosition() - midpointHeight);
        float dir = (float) Math.atan2(dy, dx);

        shoot(x, y, dir);
        fireRate = WizardProjectile.fireRate;
    }

    public void render(Screen screen) {
        float xCenter = x - 16;
        float yCenter = y - 30;

        Debug.drawRectangle(screen, 20 << 4, 60 << 4, 100, 40, 0xff000, true);
        screen.renderMob((int) xCenter, (int) yCenter, animatedSprite.getSprite(), FlipState.None);
    }
}
