package ch.nivisan.rain.game.inventory;

import ch.nivisan.rain.game.item.Item;

/**
 * InventorySlot represents a single slot in the inventory, it can hold a certain amount of a specific item.
 * It provides methods to add and remove items, as well as to check if the slot is empty and to get the current item and count.
 */
public class InventorySlot {
	private int count;
	private Item item;
	
	public InventorySlot() {
		this.count = 0;
		this.item = null;
	}
	
	public void addItem(Item item, int amount) {
		if (this.item == null) {
			this.item = item;
			this.count = amount;
		} else if (this.item.equals(item) && this.item.getMaxStackSize() > this.count) {
			while(amount-- + this.count < this.item.getMaxStackSize() && amount != 0) {
				this.count++;
			}
		}
	}
	
	public boolean removeItem(boolean removeAll) {
		if(removeAll) {
			this.item  = null;
			this.count = 0;
			return true;
		}
		return false;
	}
	
	public boolean removeItem(int amount) {
		if (this.item != null && this.count >= amount) {
			this.count -= amount;
			if (this.count == 0) {
				this.item = null;
			}
			return true;
		}
		return false;
	}
	
	public int getCount() {
		return count;
	}
	
	public Item getItem() {
		return item;
	}
	
	public boolean isEmpty() {
		return item == null || count == 0;
	}
}
