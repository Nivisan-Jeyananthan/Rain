package ch.nivisan.rain.game.entity.mob;

import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.Sprite;
import ch.nivisan.rain.game.inventory.MerchantInventory;
import ch.nivisan.rain.game.item.Item;
import ch.nivisan.rain.game.level.Level;

public class Merchant extends Mob {
    private String name;
    private MerchantInventory inventory;

    public Merchant(String name, int x, int y, Level level) {
        super(level);
        this.name = name;
        this.x = x;
        this.y = y;
        this.sprite = Sprite.dummy; 
        this.inventory = new MerchantInventory(10);
        inventory.addItem(Item.stone, 5);
    }

    @Override
    public void update() {
        // Merchant doesn't move, just stands there
    }

    @Override
    public void render(Screen screen) {
        screen.renderMob((int) x, (int) y, sprite, FlipState.None);
    }

    public MerchantInventory getInventory() {
        return inventory;
    }

    public String getName() {
        return name;
    }
}