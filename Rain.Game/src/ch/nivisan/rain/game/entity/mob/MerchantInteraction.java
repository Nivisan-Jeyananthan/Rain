package ch.nivisan.rain.game.entity.mob;

import ch.nivisan.rain.game.graphics.gui.ShopGUI;
import ch.nivisan.rain.game.graphics.gui.UIManager;
import ch.nivisan.rain.game.graphics.WindowManager;
import ch.nivisan.rain.game.utils.Vector2;

public class MerchantInteraction {
    private Merchant merchant;
    private Player player;
    private ShopGUI shopGUI;
    private static final float INTERACTION_RANGE = 50f;

    public MerchantInteraction(Merchant merchant, Player player) {
        this.merchant = merchant;
        this.player = player;
    }

    public boolean canInteract() {
        float dx = merchant.getX() - player.getX();
        float dy = merchant.getY() - player.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance <= INTERACTION_RANGE;
    }

    public void openShop() {
        if (shopGUI == null) {
            Vector2 position = new Vector2(100, 50);
            Vector2 size = new Vector2(WindowManager.getScaledWindowWidth() - 200, WindowManager.getScaledWindowHeight() - 100);
            shopGUI = new ShopGUI(position, size, merchant, player);
            UIManager.getInstance().addPanel(shopGUI);
        }
    }

    public void closeShop() {
        if (shopGUI != null) {
            UIManager.getInstance().removePanel(shopGUI);
            shopGUI = null;
        }
    }

    public boolean isShopOpen() {
        return shopGUI != null;
    }
}