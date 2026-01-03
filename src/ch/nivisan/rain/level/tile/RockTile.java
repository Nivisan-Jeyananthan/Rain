package ch.nivisan.rain.level.tile;

import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.Sprite;

public class RockTile extends Tile {
    public RockTile(Sprite sprite){
        super(sprite);
    }

    public void render(int x, int y, Screen screen){
        screen.renderTile(x << 4,y << 4,this);
    }

    @Override
    public boolean solid(){
        return true;
    }
}
