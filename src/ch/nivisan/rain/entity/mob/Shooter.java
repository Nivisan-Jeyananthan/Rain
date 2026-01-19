package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;

public class Shooter extends DummyMob{
    public Shooter(Level level, int x, int y) {
        super(level,x,y);
        this.x = x << 4;
        this.y = y << 4;
        sprite = Sprite.dummy;
    }

    @Override
    public void update() {
        super.update();

        Player player = level.getClientPlayer();
        float dx = player.getX() - x;
        float dy = player.getY() - y;
        float dir = (float) Math.atan2(dy,dx);

        shoot(x + 8, y+ 13, dir);
    }

    public void render(Screen screen) {
        screen.renderMob((int) x , (int) y , sprite, FlipState.None);
    }
}
