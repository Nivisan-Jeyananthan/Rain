package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.graphics.AnimatedSprite;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.SpriteSheet;
import ch.nivisan.rain.level.Level;

public class Chaser extends Mob {
    private static final AnimatedSprite front = new AnimatedSprite(SpriteSheet.dummyFront, 32, 32, 3);
    private static final AnimatedSprite back = new AnimatedSprite(SpriteSheet.dummyBack, 32, 32, 3);
    private static final AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummyRight, 32, 32, 3);
    private static final AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummyLeft, 32, 32, 3);
    private AnimatedSprite animatedSprite = front;
    private Player targetPlayer;

    private int time = 0;
    private int yAbsolute = 0;
    private int xAbsolute = 0;

    public Chaser(Level level, int x, int y) {
        super(level);
        this.x = x << 4;
        this.y = y << 4;
        sprite = animatedSprite.getSprite();
    }

    @Override
    public void update() {
        move();

        if (walking) animatedSprite.update();
        else animatedSprite.setFrame(0);
        time++;

        if (yAbsolute > 0) {
            animatedSprite = front;
        } else if (yAbsolute < 0) {
            animatedSprite = back;
        } else if (xAbsolute > 0) {
            animatedSprite = right;
        } else if (xAbsolute < 0) {
            animatedSprite = left;
        }
    }

    protected void move() {
        xAbsolute = 0;
        yAbsolute = 0;

        var players = level.getPlayers(this,50);
        if (!players.isEmpty()) {
            targetPlayer = players.getFirst();

            if (x < targetPlayer.getX()) {
                xAbsolute++;
            }
            if (x > targetPlayer.getX()) {
                xAbsolute--;
            }
            if (y < targetPlayer.getY()) {
                yAbsolute++;
            }
            if (y > targetPlayer.getY()) {
                yAbsolute--;
            }
        }

        if (xAbsolute != 0 || yAbsolute != 0) {
            move(xAbsolute, yAbsolute);
            walking = true;
        } else {
            walking = false;
        }
    }

    @Override
    public void render(Screen screen) {
        sprite = animatedSprite.getSprite();
        screen.renderMob(x - 16, y - 16, this);
    }
}
