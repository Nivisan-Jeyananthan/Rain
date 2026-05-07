package ch.nivisan.rain.game.inventory;

import ch.nivisan.rain.game.item.Gold;
import ch.nivisan.rain.game.item.Item;

/**
 * MerchantInventory is a subclass of Inventory that represents the inventory of a merchant. 
 * It provides additional methods to check if the merchant can sell a specific item and to handle item exchanges with questlines.
 */
public class MerchantInventory extends Inventory {

	public MerchantInventory(int size) {
		super(size);
	}

	/**
	 * Checks if the merchant can sell a specific item by going through the inventory slots and checking if the item is in stock with the required count.
	 * It will return true if the item is in stock, or false if it is not available in the inventory.
	 * @param item
	 * @param count
	 * @return
	 */
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
