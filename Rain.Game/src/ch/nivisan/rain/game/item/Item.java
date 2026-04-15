package ch.nivisan.rain.game.item;

import java.util.concurrent.atomic.AtomicInteger;

import ch.nivisan.rain.game.graphics.Screen;
import ch.nivisan.rain.game.graphics.Sprite;

public class Item {	
	private static final AtomicInteger atomicId = new AtomicInteger();
	
	private final int id = atomicId.incrementAndGet();
	private final String name;
	private final Sprite sprite;
	private final int maxStack;
	public final Gold price;
	
	public static Item stone = new Item("Stone", Sprite.itemStaff, new Gold(50));
	
	
	private Item(String name ,int maxStackSize, Sprite sprite, Gold price) {
		this.name = name;
		this.maxStack = maxStackSize;
		this.sprite = sprite;
		this.price = price;
	}
	
	private Item(String name, Sprite sprite, Gold price) {
		this.name = name;
		this.maxStack = 100;
		this.sprite = sprite;
		this.price = price;
	}
	
	
	public void render(Screen screen) {
		
	}
}
