package ch.nivisan.rain.game.item;

public class Item {	
	private final String name;
	private final int maxSlotAmount;
	private final int maxInventoryAmount;
	
	public static Item stone = new Item("Stone");
	
	
	private Item(String name) {
		this.name = name;
	}
	
}
