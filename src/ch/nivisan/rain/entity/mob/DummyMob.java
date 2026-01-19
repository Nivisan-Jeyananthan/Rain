package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.graphics.AnimatedSprite;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.SpriteSheet;
import ch.nivisan.rain.level.Level;

public class DummyMob extends Mob {
    private static final AnimatedSprite front = new AnimatedSprite(SpriteSheet.dummyFront, 32, 32, 3);
    private static final AnimatedSprite back = new AnimatedSprite(SpriteSheet.dummyBack, 32, 32, 3);
    private static final AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummyRight, 32, 32, 3);
    private static final AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummyLeft, 32, 32, 3);

    private AnimatedSprite animatedSprite = front;
    private int time = 0;
    private int yAbsolute = 0;
    private int xAbsolute = 0;

    public DummyMob(Level level, int x, int y) {
        super(level);
        this.x = x << 4;
        this.y = y << 4;
    }

    @Override
    public void update() {
        if (walking) animatedSprite.update();
        else animatedSprite.setFrame(0);

        time++;
        if (time % (random.nextInt(50) + 30) == 0) {
            xAbsolute = random.nextInt(3) - 1;
            yAbsolute = random.nextInt(3) - 1;
            time = 0;

            if (random.nextInt(5) == 0) {
                xAbsolute = 0;
                yAbsolute = 0;
            }
        }

        if (yAbsolute > 0) {
            animatedSprite = front;
        } else if (yAbsolute < 0) {
            animatedSprite = back;
        } else if (xAbsolute > 0) {
            animatedSprite = right;
        } else if (xAbsolute < 0) {
            animatedSprite = left;
        }

        if (xAbsolute != 0 || yAbsolute != 0) {
            move(xAbsolute, yAbsolute);
            walking = true;
        } else {
            walking = false;
        }
        sprite = animatedSprite.getSprite();
    }

    @Override
    public void render(Screen screen) {
        screen.renderMob((int) x - 16, (int) y - 16, sprite, FlipState.None);
    }
}
