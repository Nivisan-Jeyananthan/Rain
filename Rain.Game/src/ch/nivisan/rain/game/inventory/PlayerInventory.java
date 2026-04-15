package ch.nivisan.rain.game.inventory;

import ch.nivisan.rain.game.item.Item;

public class PlayerInventory extends Inventory {

	public PlayerInventory(int size) {
		super(size);
	}

	public void sellItem(Item item, int count, MerchantInventory inventory) {
		inventory.addItem(item, count);
	}

	public void discardItem(){
	}
}
