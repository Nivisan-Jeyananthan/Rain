package ch.nivisan.rain.net.player;

import ch.nivisan.rain.entity.mob.Mob;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.level.Level;

public class NetPlayer extends Mob {
    protected int x;
    protected int y;

    public NetPlayer(Level level, int x, int y ) {
        super(level);
        this.x = x;
        this.y = y;
    }

    @Override
    public void update() {

    }

    public void render(Screen screen) {
        screen.fillRectangle(x,y ,32,32,0x2030cc, true);
    }
}
