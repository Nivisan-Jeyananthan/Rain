package ch.nivisan.rain.game.inventory;

import ch.nivisan.rain.game.item.Gold;
import ch.nivisan.rain.game.item.Item;

public class MerchantInventory extends Inventory {

	public MerchantInventory(int size) {
		super(size);
	}

	public boolean canSell(Item item, int count) {
		for (InventorySlot slot : itemSlots) {
			if (isInStock(slot, item, count)) {
				return true;
			}
		}
		return false;
	}

	private boolean isInStock(InventorySlot slot, Item item, int count) {
		return !slot.isEmpty() && slot.getItem() == item && slot.getCount() >= count;
	}

	// TODO: add method for exchange with questline
}
