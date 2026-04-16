package ch.nivisan.rain.game.graphics.gui;

import java.awt.Color;
import java.awt.Font;

import ch.nivisan.rain.game.graphics.gui.components.InventorySlotUI;
import ch.nivisan.rain.game.graphics.gui.components.UIPanel;
import ch.nivisan.rain.game.graphics.gui.components.UILabel;
import ch.nivisan.rain.game.inventory.Inventory;
import ch.nivisan.rain.game.utils.Vector2;

public class InventoryGUI extends UIPanel {
    private final Inventory inventory;
    private InventorySlotUI[] slotUIs;
    private final int slotSize = 40;
    private final int slotsPerRow = 5;
    private final Vector2 panelSize;

    public InventoryGUI(Vector2 position, Vector2 size, Inventory inventory) {
        super(position, size, 0x2e343a);
        this.inventory = inventory;
        this.panelSize = size;
        initializeSlots();
    }

    private void initializeSlots() {
        UILabel title = new UILabel(new Vector2(10, 12), "Inventar");
        title.setColor(new Color(0xF2F2F2));
        title.setFont(new Font("Courier New", Font.BOLD, 16));
        title.setShadow(true);
        addComponent(title);

        int numSlots = inventory.getSize();
        slotUIs = new InventorySlotUI[numSlots];
        for (int i = 0; i < numSlots; i++) {
            int row = i / slotsPerRow;
            int col = i % slotsPerRow;
            Vector2 slotPos = new Vector2(10 + col * (slotSize + 8), 42 + row * (slotSize + 10));
            slotUIs[i] = new InventorySlotUI(slotPos, slotSize, inventory.getSlot(i));
            addComponent(slotUIs[i]);
        }
    }

    public void updateSlots() {
        for (int i = 0; i < slotUIs.length; i++) {
            slotUIs[i].setSlot(inventory.getSlot(i));
        }
    }

    @Override
    public void render(java.awt.Graphics graphics) {
        super.render(graphics);
        Vector2 absolute = getAbsolutePosition();
        graphics.setColor(new java.awt.Color(0x5a6a79));
        graphics.drawRect(absolute.getX(), absolute.getY(), panelSize.getX() - 1, panelSize.getY() - 1);
        graphics.drawLine(absolute.getX() + 10, absolute.getY() + 36, absolute.getX() + panelSize.getX() - 10,
                absolute.getY() + 36);
    }
}