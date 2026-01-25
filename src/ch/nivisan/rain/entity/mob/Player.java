package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.Game;
import ch.nivisan.rain.entity.projectile.ShurikenProjectile;
import ch.nivisan.rain.entity.projectile.WizardProjectile;
import ch.nivisan.rain.graphics.AnimatedSprite;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.SpriteSheet;
import ch.nivisan.rain.graphics.gui.UILabel;
import ch.nivisan.rain.graphics.gui.UIManager;
import ch.nivisan.rain.graphics.gui.UIPanel;
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
    private UIManager uiManager = UIManager.getInstance();
    private final Keyboard input;
    private float fireRate = 0;
    private AnimatedSprite animatedSprite = front;
    private final String name;


    public Player(String name, Keyboard input, Level level) {
        super(level);
        this.name = name;
        this.input = input;
        animatedSprite = front;
        walkSpeed = 1.4f;
    }

    public Player(String name,int x, int y, Keyboard input, Level level) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.input = input;
        this.fireRate = ShurikenProjectile.fireRate;
        walkSpeed = 1.4f;

        createGUI();
    }

    private void createGUI(){
        UIPanel panel = new UIPanel(new Vector2((300 - 80) * 3,0),new Vector2((300 - 80) * 3,Game.getScaledWindowHeight()), 0x4f4f4f);
        uiManager.addPanel(panel);
        UILabel nameLabel = new UILabel(new Vector2(40,200),"Nivisan");
        nameLabel.setColor(0xbbbbbbbb);
        nameLabel.setFont(new Font("Courier New",Font.BOLD,25));
        nameLabel.setShadow(true);

        panel.addComponent(nameLabel);
    }

    public String getName() {
        return name;
    }


    @Override
    public void update() {
        if (walking) animatedSprite.update();
        else { animatedSprite.setFrame(0); }
        if (fireRate > 0) fireRate--;

        float xAbsolute = 0, yAbsolute = 0;

        if (input.up) {
            yAbsolute -= walkSpeed;
            animatedSprite = back;
        } else if (input.down) {
            yAbsolute += walkSpeed;
            animatedSprite = front;
        } if (input.right) {
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
            int midpointWidth = Game.getScaledWindowWidth() / 2;
            int midpointHeight = Game.getScaledWindowHeight() / 2;

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

        Debug.drawRectangle(screen, 20 << 4,60 << 4, 100,40,0xff000,true);
        screen.renderMob((int) xCenter, (int) yCenter, animatedSprite.getSprite(), FlipState.None);
    }
}
