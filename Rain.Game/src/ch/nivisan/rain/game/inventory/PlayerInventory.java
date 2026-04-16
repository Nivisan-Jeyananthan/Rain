package ch.nivisan.rain.game.inventory;

import ch.nivisan.rain.game.item.Gold;
import ch.nivisan.rain.game.item.Item;

public class PlayerInventory extends Inventory {
	private Gold gold;

	public PlayerInventory(int size) {
		super(size);
		this.gold = new Gold(0);
	}

	public boolean sellItem(Item item, int count, MerchantInventory merchant) {
		if (removeItem(item, count)) {
			merchant.addItem(item, count);
			gold.increaseValue(item.price.getValue() * count);
			return true;
		}
		return false;
	}

	public boolean buyItem(Item item, int count, MerchantInventory merchant) {
		int cost = item.price.getValue() * count;
		if (gold.getValue() >= cost && merchant.removeItem(item, count)) {
			addItem(item, count);
			gold.decreaseValue(cost);
			return true;
		}
		return false;
	}

	public void discardItem(Item item, int count) {
		removeItem(item, count);
	}

	public Gold getGold() {
		return gold;
	}
}
