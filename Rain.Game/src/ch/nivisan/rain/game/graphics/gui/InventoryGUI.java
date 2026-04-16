package ch.nivisan.rain.game.graphics.gui;

import ch.nivisan.rain.game.graphics.gui.components.InventorySlotUI;
import ch.nivisan.rain.game.graphics.gui.components.UIPanel;
import ch.nivisan.rain.game.inventory.Inventory;
import ch.nivisan.rain.game.utils.Vector2;

public class InventoryGUI extends UIPanel {
    private Inventory inventory;
    private InventorySlotUI[] slotUIs;
    private int slotSize = 40;
    private int slotsPerRow = 5;

    public InventoryGUI(Vector2 position, Vector2 size, Inventory inventory) {
        super(position, size, 0x4f4f4f);
        this.inventory = inventory;
        initializeSlots();
    }

    private void initializeSlots() {
        int numSlots = inventory.getSize();
        slotUIs = new InventorySlotUI[numSlots];
        for (int i = 0; i < numSlots; i++) {
            int row = i / slotsPerRow;
            int col = i % slotsPerRow;
            Vector2 slotPos = new Vector2(position.x + 10 + col * (slotSize + 5), position.y + 10 + row * (slotSize + 5));
            slotUIs[i] = new InventorySlotUI(slotPos, slotSize, inventory.getSlot(i));
            addComponent(slotUIs[i]);
        }
    }

    public void updateSlots() {
        for (int i = 0; i < slotUIs.length; i++) {
            slotUIs[i] = new InventorySlotUI(slotUIs[i].getPosition(), slotSize, inventory.getSlot(i));
        }
    }
}