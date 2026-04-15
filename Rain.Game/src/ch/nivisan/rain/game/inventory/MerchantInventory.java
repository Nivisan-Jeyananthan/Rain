package ch.nivisan.rain.game.inventory;

import ch.nivisan.rain.game.item.Gold;
import ch.nivisan.rain.game.item.Item;

public class MerchantInventory extends Inventory {

	public MerchantInventory(int size) {
		super(size);
	}

	public Item exchange(Gold gold, Item selectedItem) {
		if (selectedItem.price.getValue() <= gold.getValue())
			return selectedItem;
		return null;
	}

}
