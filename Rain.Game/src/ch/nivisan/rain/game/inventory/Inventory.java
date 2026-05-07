package ch.nivisan.rain.game.inventory;

import ch.nivisan.rain.game.item.Item;

/**
 * Inventory is an abstract class that represents a collection of InventorySlots.
 * It provides methods to add and remove items from the inventory, as well as to access individual slots and get the size of the inventory.
 */
public abstract class Inventory {
	protected final InventorySlot[] itemSlots;
	protected final int size;
	
	public Inventory(int size) {
		this.size = size;
		itemSlots = new InventorySlot[size];
		for (int i = 0; i < size; i++) {
			itemSlots[i] = new InventorySlot();
		}
	}
	
	/**
	 * Tries to add item to inventory by going through bunch of steps: 
	 * 1. Try to add to existing slot with same item, if it can returns true
	 * 2. Find empty slot, try to add the Item there.
	 * when even that fails it will return false
	 * 
	 * @param item
	 * @param count
	 * @return status of if the item was added successfully - true for success
	 */
	public boolean addItem(Item item, int count) {
		
		for (InventorySlot slot : itemSlots) {
			if (!slot.isEmpty() && slot.getItem() == item) {
				slot.addItem(item, count);
				return true;
			}
		}
		
		for (InventorySlot slot : itemSlots) {
			if (slot.isEmpty()) {
				slot.addItem(item, count);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Tries to remove item from inventory by going through bunch of steps: 
	 * 1. Try to find slot with the item, if it can remove the specified count of items and returns true
	 * 2. If the removeAll flag is set to true, it will remove all items of that type from the inventory and return true.
	 * when even that fails it will return false
	 * 
	 * @param item
	 * @param count
	 * @return status of if the item was removed successfully - true for success
	 */
	public boolean removeItem(Item item, int count) {
		for (InventorySlot slot : itemSlots) {
			if (!slot.isEmpty() && slot.getItem() == item) {
				return slot.removeItem(count);
			}
		}
		return false;
	}
	
	/**
	 * Tries to remove all items of the specified type from the inventory. 
	 * It will return true if it successfully removed the items, or false if it failed to find the item in the inventory.
	 * @param index
	 * @return
	 */
	public InventorySlot getSlot(int index) {
		if (index >= 0 && index < size) {
			return itemSlots[index];
		}
		return null;
	}
	
	public int getSize() {
		return size;
	}
}
