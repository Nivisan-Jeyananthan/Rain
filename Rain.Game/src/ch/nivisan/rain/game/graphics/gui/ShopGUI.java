package ch.nivisan.rain.game.graphics.gui;

import ch.nivisan.rain.game.entity.mob.Merchant;
import ch.nivisan.rain.game.entity.mob.Player;
import ch.nivisan.rain.game.graphics.gui.components.InventorySlotUI;
import ch.nivisan.rain.game.graphics.gui.components.UIPanel;
import ch.nivisan.rain.game.graphics.gui.components.UILabel;
import ch.nivisan.rain.game.graphics.gui.components.button.UIButton;
import ch.nivisan.rain.game.graphics.gui.components.button.UIButtonActionListener;
import ch.nivisan.rain.game.inventory.InventorySlot;
import ch.nivisan.rain.game.item.Item;
import ch.nivisan.rain.game.utils.Vector2;

public class ShopGUI extends UIPanel {
    private Merchant merchant;
    private Player player;
    private InventorySlotUI[] merchantSlotUIs;
    private InventorySlotUI[] playerSlotUIs;
    private int slotSize = 40;
    private int slotsPerRow = 5;

    public ShopGUI(Vector2 position, Vector2 size, Merchant merchant, Player player) {
        super(position, size, 0x4f4f4f);
        this.merchant = merchant;
        this.player = player;
        initializeShop();
    }

    private void initializeShop() {
        UILabel nameLabel = new UILabel(new Vector2(position.getX() + 10, position.getY() + 10),
                "Shop: " + merchant.getName());
        addComponent(nameLabel);

        int numMerchantSlots = merchant.getInventory().getSize();
        merchantSlotUIs = new InventorySlotUI[numMerchantSlots];
        for (int i = 0; i < numMerchantSlots; i++) {
            int row = i / slotsPerRow;
            int col = i % slotsPerRow;
            Vector2 slotPos = new Vector2(position.getX() + 10 + col * (slotSize + 5),
                    position.getY() + 40 + row * (slotSize + 5));
            merchantSlotUIs[i] = new InventorySlotUI(slotPos, slotSize, merchant.getInventory().getSlot(i));
            addComponent(merchantSlotUIs[i]);

            // Add buy button for each slot
            if (!merchant.getInventory().getSlot(i).isEmpty()) {
                UIButton buyButton = new UIButton(new Vector2(slotPos.getX(), slotPos.getY() + slotSize),
                        new Vector2(slotSize, 20), "Buy",
                        new BuyActionListener(merchant.getInventory().getSlot(i).getItem()));
                addComponent(buyButton);
            }
        }

        // Player inventory slots for selling
        int numPlayerSlots = player.getInventory().getSize();
        playerSlotUIs = new InventorySlotUI[numPlayerSlots];
        int startY = position.getY() + 40 + (numMerchantSlots / slotsPerRow + 1) * (slotSize + 5) + 20;
        for (int i = 0; i < numPlayerSlots; i++) {
            int row = i / slotsPerRow;
            int col = i % slotsPerRow;
            Vector2 slotPos = new Vector2(position.getX() + 10 + col * (slotSize + 5), startY + row * (slotSize + 5));
            playerSlotUIs[i] = new InventorySlotUI(slotPos, slotSize, player.getInventory().getSlot(i));
            addComponent(playerSlotUIs[i]);

            // Add sell button for each slot
            if (!player.getInventory().getSlot(i).isEmpty()) {
                UIButton sellButton = new UIButton(new Vector2(slotPos.getX(), slotPos.getY() + slotSize),
                        new Vector2(slotSize, 20), "Sell",
                        new SellActionListener(player.getInventory().getSlot(i).getItem()));

                addComponent(sellButton);
            }
        }
    }

    private class BuyActionListener implements IUIActionListener {
        private Item item;

        public BuyActionListener(Item item) {
            this.item = item;
        }

        public void performAction() {
            player.getInventory().buyItem(item, 1, merchant.getInventory());
            updateSlots();
        }
    }

    private class SellActionListener implements IUIActionListener {
        private Item item;

        public SellActionListener(Item item) {
            this.item = item;
        }

        @Override
        public void performAction() {
            player.getInventory().sellItem(item, 1, merchant.getInventory());
            updateSlots();
        }
    }

    public void updateSlots() {
        // Update merchant slots
        for (int i = 0; i < merchantSlotUIs.length; i++) {
            merchantSlotUIs[i] = new InventorySlotUI(merchantSlotUIs[i].getAbsolutePosition(), slotSize,
                    merchant.getInventory().getSlot(i));
        }
        // Update player slots
        for (int i = 0; i < playerSlotUIs.length; i++) {
            playerSlotUIs[i] = new InventorySlotUI(playerSlotUIs[i].getAbsolutePosition(), slotSize,
                    player.getInventory().getSlot(i));
        }
    }
}