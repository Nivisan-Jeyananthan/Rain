package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.graphics.AnimatedSprite;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.SpriteSheet;
import ch.nivisan.rain.level.Level;
import ch.nivisan.rain.utils.Node;
import ch.nivisan.rain.utils.Vector2;

import java.util.List;

public class Star extends Mob {
    private static final AnimatedSprite front = new AnimatedSprite(SpriteSheet.dummyFront, 32, 32, 3);
    private static final AnimatedSprite back = new AnimatedSprite(SpriteSheet.dummyBack, 32, 32, 3);
    private static final AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummyRight, 32, 32, 3);
    private static final AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummyLeft, 32, 32, 3);
    private AnimatedSprite animatedSprite = front;
    private Player targetPlayer;
    private List<Node> path = null;

    private int time = 0;
    private float yAbsolute = 0f;
    private float xAbsolute = 0f;

    public Star(Level level, int x, int y) {
        super(level);
        this.x = x << 4;
        this.y = y << 4;
        sprite = animatedSprite.getSprite();
        walkSpeed = 1.5f;
    }

    @Override
    public void update() {
        time++;
        move();

        if (walking) animatedSprite.update();
        else animatedSprite.setFrame(0);

        if (yAbsolute > 0) {
            animatedSprite = front;
        } else if (yAbsolute < 0) {
            animatedSprite = back;
        } else if (xAbsolute > 0) {
            animatedSprite = right;
        } else if (xAbsolute < 0) {
            animatedSprite = left;
        }
        sprite = animatedSprite.getSprite();
    }

    protected void move() {
        xAbsolute = 0;
        yAbsolute = 0;
        int px = (int) level.getPlayer(0).getX();
        int py = (int) level.getPlayer(0).getY();
        Vector2 start = new Vector2((int) x >> 4, (int) y >> 4);
        Vector2 destination = new Vector2(px >> 4, py >> 4);
        if (time % 60 == 0) path = level.getPath(start, destination);

        if (path != null && !path.isEmpty()) {
            Vector2 vec = path.getLast().tile;
            if (x < vec.getX() << 4) xAbsolute++;
            else if (x > vec.getX() << 4) xAbsolute--;
            if (y < vec.getY() << 4) yAbsolute++;
            else if (y > vec.getY() << 4) yAbsolute--;
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
        int bodyColor = 0xffE8E83A;
        screen.renderMob((int) x - 16, (int) y - 16, this, bodyColor);
    }
}
