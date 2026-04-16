package ch.nivisan.rain.game.graphics.gui.components;

import java.awt.Color;
import java.awt.Font;
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
        graphics.setColor(new Color(0x272c33));
        graphics.fillRoundRect(x, y, size, size, 10, 10);
        graphics.setColor(new Color(0x4f5862));
        graphics.drawRoundRect(x, y, size, size, 10, 10);

        if (slot != null && !slot.isEmpty()) {
            graphics.setColor(new Color(0x5a6772));
            graphics.fillRoundRect(x + 4, y + 4, size - 8, size - 8, 8, 8);

            String name = slot.getItem().getName();
            graphics.setColor(Color.WHITE);
            graphics.setFont(new Font("Courier New", Font.BOLD, 10));
            graphics.drawString(name, x + 6, y + size / 2);

            if (slot.getCount() > 1) {
                graphics.setColor(new Color(0x202b34));
                graphics.fillOval(x + size - 18, y + size - 18, 16, 16);
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("Courier New", Font.BOLD, 11));
                graphics.drawString(String.valueOf(slot.getCount()), x + size - 14, y + size - 6);
            }
        } else {
            graphics.setColor(new Color(0x1f242a));
            graphics.fillRoundRect(x + 4, y + 4, size - 8, size - 8, 8, 8);
        }
    }

    public InventorySlot getSlot() {
        return slot;
    }

    public void setSlot(InventorySlot slot) {
        this.slot = slot;
    }
}