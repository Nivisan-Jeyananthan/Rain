package ch.nivisan.rain.game.inventory;

import ch.nivisan.rain.game.item.Item;

public class Inventory {
	private final InventorySlot[] itemSlots;
	private final int size;
	
	public Inventory(int size) {
		itemSlots = new InventorySlot[size];
		this.size = size;
	}
	
	public void addItem(Item item, int count) {

	}
	
	private InventorySlot hasItem(Item item) {
		for(int i = 0; i < itemSlots.length; i++) {
			if(itemSlots[i].getItem() == item) {
				return itemSlots[i];
			}
		}
		return null;
	}
}
