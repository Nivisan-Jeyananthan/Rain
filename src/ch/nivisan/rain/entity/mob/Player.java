package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.entity.projectile.ShurikenProjectile;
import ch.nivisan.rain.entity.projectile.WizardProjectile;
import ch.nivisan.rain.graphics.*;
import ch.nivisan.rain.graphics.gui.*;
import ch.nivisan.rain.input.Keyboard;
import ch.nivisan.rain.input.Mouse;
import ch.nivisan.rain.level.Level;
import ch.nivisan.rain.utils.Debug;
import ch.nivisan.rain.utils.Vector2;

import java.awt.*;

public class Player extends Mob {
    private static final AnimatedSprite front = new AnimatedSprite(SpriteSheet.playerFront, 32, 32, 3);
    private static final AnimatedSprite back = new AnimatedSprite(SpriteSheet.playerBack, 32, 32, 3);
    private static final AnimatedSprite right = new AnimatedSprite(SpriteSheet.playerRight, 32, 32, 3);
    private static final AnimatedSprite left = new AnimatedSprite(SpriteSheet.playerLeft, 32, 32, 3);
    private final Keyboard input;
    private final String name;
    private final UIManager uiManager = UIManager.getInstance();
    private float fireRate = 0;
    private AnimatedSprite animatedSprite = front;
    // todo: refactor creation of ui to playerUI
    private static PlayerUI playerUI;
    private UILabeledProgressbar uiHealthBar;

    public Player(String name, int x, int y, Keyboard input, Level level) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.input = input;
        this.fireRate = ShurikenProjectile.fireRate;
        walkSpeed = 1.4f;
        health = 100;
        maxHealth = 100;

        createGUI();
    }

    private void createGUI() {
        final int panelStart = (WindowManager.getScaledWindowWidth() - WindowManager.getScaledGUIWidth());
        final int divisor = 100;
        final int componentPositionX = (WindowManager.getScaledGUIWidth() / divisor);
        final int maxComponentWidth = (WindowManager.getScaledGUIWidth() / divisor) * (divisor -1);
        final int widthOffset =  (maxComponentWidth / divisor);
        final int height = (WindowManager.getScaledWindowHeight()  / 5)  * 2;
        final int offsetHeight = (height / 10);

        UIPanel panel = new UIPanel(new Vector2(panelStart, 0), new Vector2(WindowManager.getScaledGUIWidth(), WindowManager.getScaledWindowHeight()), 0x4f4f4f);
        uiManager.addPanel(panel);
        UILabel nameLabel = new UILabel(new Vector2(componentPositionX, height), "Nivisan");
        nameLabel.setColor(0xbbbbbbbb);
        nameLabel.setFont(new Font("Courier New", Font.BOLD, 25));
        nameLabel.setShadow(true);

        Vector2 startPosition = new Vector2(componentPositionX ,height + offsetHeight);
        Vector2 barSize = new Vector2(maxComponentWidth - widthOffset,20);
        uiHealthBar = new UILabeledProgressbar(startPosition,barSize, 0xee3030,0xffffffff,"HP");
        uiHealthBar.setColor(0x6a6a6a);
        uiHealthBar.setFont(new Font("Courier New", Font.BOLD, 20));
        uiHealthBar.setShadow(true);

        var pos =  new Vector2(componentPositionX * 25 ,height + (offsetHeight * 2));
        UIButton button = new UIButton(pos, new Vector2(barSize.getX() / 2, barSize.getY() + 20), "Button text", new IUIActionListener() {
            @Override
            public void performAction() {
                System.out.println("Button pressed");
            }
        });
        button.setColor(new Color(0x64A108));
        button.setTextColor(Color.BLACK);

        panel.addComponent(nameLabel);
        panel.addComponent(uiHealthBar);
        panel.addComponent(button);
    }

    public String getName() {
        return name;
    }


    @Override
    public void update() {
        uiHealthBar.setProgress(health / maxHealth);
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
