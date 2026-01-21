package ch.nivisan.rain.entity.mob;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;
import ch.nivisan.rain.level.Level;
import ch.nivisan.rain.utils.Vector2;
import java.util.List;

public class Shooter extends Walker {
    private float fireRate = 0;
    Mob target = null;

    public Shooter(Level level, int x, int y) {
        super(level,x,y);
        this.x = x << 4;
        this.y = y << 4;
        sprite = Sprite.dummy;
    }

    @Override
    public void update() {
        super.update();

        fireRate--;

        shootRandomMob();
    }

    private void shootRandomMob() {
        if((time & (30 + random.nextInt(91))) == 0) {
            List<Mob> mobs = level.getMobs(this, 500);
            mobs.add(level.getClientPlayer());
            mobs.remove(this);

            int index = random.nextInt(mobs.size());
            target = mobs.get(index);
        }

        if(target == null) return;

        float dx = target.getX() - x;
        float dy = target.getY() - y;
        float dir = (float) Math.atan2(dy,dx);

        shoot(x , y, dir);
    }

    private void shootClosestMob() {
        List<Mob> mobs = level.getMobs(this,500);
        mobs.add(level.getClientPlayer());
        mobs.remove(this);

        float min = 0;
        Mob closestMob = null;
        for (int i = 0; i < mobs.size(); i++) {
            Vector2 vector = new Vector2((int)x,(int)y);
            Mob target = mobs.get(i);
            Vector2 targetVector = new Vector2((int)target.getX(),(int)target.getY());

            float distance = vector.getDistance(targetVector);
            if(i == 0 || distance < min) {
                min = distance;
                closestMob = target;
            }
        }

        if (closestMob == null) return;

        float dx = closestMob.getX() - x;
        float dy = closestMob.getY() - y;
        float dir = (float) Math.atan2(dy,dx);

        shoot(x , y, dir);
    }

    public void render(Screen screen) {
        int bodyColorGreen = 0xff00FF3A;
        screen.renderMob((int) x - 16, (int) y - 16, this, bodyColorGreen);
    }
}
