package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.Game;
import ch.nivisan.rain.entity.projectile.WizardProjectile;
import ch.nivisan.rain.graphics.AnimatedSprite;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.graphics.SpriteSheet;
import ch.nivisan.rain.input.Keyboard;
import ch.nivisan.rain.input.Mouse;
import ch.nivisan.rain.level.Level;

public class Player extends Mob {
    private final Keyboard input;
    private Sprite sprite;
    private boolean walking = false;
    private int anim = 0;
    private double fireRate = 0;
    private static AnimatedSprite front = new AnimatedSprite(SpriteSheet.playerFront,32,32,3);
    private static AnimatedSprite back = new AnimatedSprite(SpriteSheet.playerBack,32,32,3);
    private static AnimatedSprite right = new AnimatedSprite(SpriteSheet.playerRight,32,32,3);
    private static AnimatedSprite left = new AnimatedSprite(SpriteSheet.playerLeft,32,32,3);

    private AnimatedSprite animatedSprite = front;

    public Player(Keyboard input, Level level) {
        super(level);
        this.input = input;
        sprite = Sprite.playerFront;
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
        if(walking) animatedSprite.update();
        else{ animatedSprite.setFrame(0);}
        if (fireRate > 0) fireRate--;

        int xAbsolute = 0, yAbsolute = 0;
        if (anim < 7_500) anim++;
        else anim = 0;


        if (input.up) {
            yAbsolute--;
            animatedSprite = back;
        }
        else if (input.down) {
            yAbsolute++;
            animatedSprite = front;
        }
        else if (input.right) {
            xAbsolute++;
            animatedSprite = right;
        }
        else if (input.left) {
            xAbsolute--;
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

            double dx = (Mouse.getXPosition() - midpointWidth);
            double dy = (Mouse.getYPosition() - midpointHeight);
            double dir = Math.atan2(dy, dx);

            shoot(x, y, dir);
            fireRate = WizardProjectile.fireRate;
        }
    }

    public void render(Screen screen) {
        FlipState flip = FlipState.None;
        if (facingDirection == Direction.North) {
            sprite = Sprite.playerBack;
            if (walking) {
                if (anim % 20 > 10) {
                    sprite = Sprite.playerBack1;
                }
                if (anim % 40 > 30) {
                    sprite = Sprite.playerBack2;
                }
            }
        }
        if (facingDirection == Direction.East) {
            sprite = Sprite.playerRight;
            if (walking) {
                if (anim % 20 > 10) {
                    sprite = Sprite.playerRight1;
                }
                if (anim % 40 > 30) {
                    sprite = Sprite.playerRight2;
                }
            }
        }
        if (facingDirection == Direction.South) {
            sprite = Sprite.playerFront;
            if (walking) {
                if (anim % 20 > 10) {
                    sprite = Sprite.playerFront1;
                }
                if (anim % 40 > 30) {
                    sprite = Sprite.playerFront2;
                }
            }
        }
        if (facingDirection == Direction.West) {
            flip = FlipState.None;
            sprite = Sprite.playerRight;
            if (walking) {
                if (anim % 20 > 10) {
                    sprite = Sprite.playerRight1;
                }
                if (anim % 40 > 30) {
                    sprite = Sprite.playerRight2;
                }
            }
        }

        int xCenter = x - 16;
        int yCenter = y - 16;

        sprite = animatedSprite.getSprite();
        screen.renderPlayer(xCenter, yCenter, sprite, flip);
    }
}
