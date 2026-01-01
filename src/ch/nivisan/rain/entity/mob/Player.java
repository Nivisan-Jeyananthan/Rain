package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.input.Keyboard;

public class Player extends Mob{
    private Keyboard input;

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

        if(input.up)
            yAbsolute--;
        if(input.down)
            yAbsolute++;
        if(input.right)
            xAbsolute++;
        if(input.left)
            xAbsolute--;

        if(xAbsolute != 0 || yAbsolute != 0)
            move(xAbsolute, yAbsolute);
    }

    public void render(Screen screen){
        screen.renderPlayer(x,y, Sprite.playerBack);
    }
}
