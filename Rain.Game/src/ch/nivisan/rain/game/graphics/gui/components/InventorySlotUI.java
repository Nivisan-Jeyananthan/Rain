package ch.nivisan.rain.game.graphics.gui.components;

import java.awt.Graphics;

import ch.nivisan.rain.game.inventory.InventorySlot;
import ch.nivisan.rain.game.utils.Vector2;

public class InventorySlotUI extends UIComponent {
    private InventorySlot slot;
    private int size;

    public InventorySlotUI(Vector2 position, int size, InventorySlot slot) {
        super(position, new Vector2(size, size));
        this.slot = slot;
        this.size = size;
    }

    @Override
    public void render(Graphics graphics) {
        Vector2 absolutePosition = getAbsolutePosition();
        int x = absolutePosition.getX();
        int y = absolutePosition.getY();

        // Draw slot background
        graphics.setColor(new java.awt.Color(0x333333));
        graphics.fillRect(x, y, size, size);
        graphics.setColor(new java.awt.Color(0x666666));
        graphics.drawRect(x, y, size, size);

        if (slot != null && !slot.isEmpty()) {
            graphics.setColor(new java.awt.Color(0xAAAAAA));
            graphics.fillRect(x + 2, y + 2, size - 4, size - 4);

            // Draw count if >1
            if (slot.getCount() > 1) {
                graphics.setColor(java.awt.Color.WHITE);
                graphics.drawString(String.valueOf(slot.getCount()), x + size - 15, y + size - 5);
            }
        }
    }

    public InventorySlot getSlot() {
        return slot;
    }

    public void setSlot(InventorySlot slot) {
        this.slot = slot;
    }
}