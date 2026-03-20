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
	
	public static Item stone = new Item("Stone", Sprite.itemStaff);
	
	
	private Item(String name ,int maxStackSize, Sprite sprite) {
		this.name = name;
		this.maxStack = maxStackSize;
		this.sprite = sprite;
	}
	
	private Item(String name, Sprite sprite) {
		this.name = name;
		this.maxStack = 100;
		this.sprite = sprite;
	}
	
	
	public void render(Screen screen) {
		
	}
}
