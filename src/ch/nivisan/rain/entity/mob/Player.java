package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.input.Keyboard;

public class Player extends Mob{
    private Keyboard input;
    private Sprite sprite;
    private boolean walking = false;
    private int anim = 0;

    public Player(Keyboard input){
        this.input = input;
    }

    public Player(int x, int y, Keyboard input){
        this.x = x;
        this.y = y;
        this.input = input;
    }

    @Override
    public void update(){
        int xAbsolute = 0, yAbsolute = 0;
        if(anim < 7_500) anim++;
        else anim = 0;


        if(input.up)
            yAbsolute--;
        if(input.down)
            yAbsolute++;
        if(input.right)
            xAbsolute++;
        if(input.left)
            xAbsolute--;

        if(xAbsolute != 0 || yAbsolute != 0) {
            move(xAbsolute, yAbsolute);
            walking = true;
        }
        else{
            walking = false;
        }
    }

    public void render(Screen screen){
        FlipState flip = FlipState.None;
        if(facingDirection == Direction.North) {
            sprite = Sprite.playerBack;
            if(walking){
                if (anim % 20 > 10) {
                    sprite = Sprite.playerBack1;
                }
                if (anim % 40 > 30){
                    sprite = Sprite.playerBack2;
                }
            }
        }
        if(facingDirection == Direction.East) sprite = Sprite.playerRight;
        if(facingDirection == Direction.South) sprite = Sprite.playerFront;
        if(facingDirection == Direction.West){
            flip = FlipState.XFlipped;
        }

        int xCenter = x - 16;
        int yCenter = y - 16;

        screen.renderPlayer(xCenter,yCenter, sprite, flip);
    }
}
