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
        int xCenter = x - 16;
        int yCenter = y - 16;

        screen.renderPlayer(xCenter,yCenter, Sprite.playerBack1);
        screen.renderPlayer(xCenter + 16,yCenter, Sprite.playerBack2);
        screen.renderPlayer(xCenter,yCenter + 16, Sprite.playerBack3);
        screen.renderPlayer(xCenter + 16,yCenter + 16, Sprite.playerBack4);

    }
}
