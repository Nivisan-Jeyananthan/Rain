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
        // Draw slot background
        graphics.setColor(new java.awt.Color(0x333333));
        graphics.fillRect((int) position.x, (int) position.y, size, size);
        graphics.setColor(new java.awt.Color(0x666666));
        graphics.drawRect((int) position.x, (int) position.y, size, size);

        if (slot != null && !slot.isEmpty()) {
            // Render item sprite (simplified, assuming sprite has render method)
            // For now, just draw a placeholder
            graphics.setColor(new java.awt.Color(0xAAAAAA));
            graphics.fillRect((int) position.x + 2, (int) position.y + 2, size - 4, size - 4);

            // Draw count if >1
            if (slot.getCount() > 1) {
                graphics.setColor(java.awt.Color.WHITE);
                graphics.drawString(String.valueOf(slot.getCount()), (int) position.x + size - 15, (int) position.y + size - 5);
            }
        }
    }

    public InventorySlot getSlot() {
        return slot;
    }
}