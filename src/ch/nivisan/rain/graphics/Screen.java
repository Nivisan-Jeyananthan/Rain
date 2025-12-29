package ch.nivisan.rain.graphics;

import ch.nivisan.rain.level.tile.Tile;

import java.util.Arrays;
import java.util.Random;

public class Screen {
	public int width , height;
	// which pixels of the screen get rendered
	public int[] pixels;
	public final int MapSize = 8;
	public final int MapSizeMask = MapSize -1;
	public int[] tiles = new int[MapSize * MapSize];

	private Random random = new Random();


	public Screen(int width, int height) {
		this.width = width;
		this.height = height;
		
		pixels = new int[width * height];
		
		for(int i = 0; i< tiles.length; i++) {
			tiles[i] = random.nextInt(0xffffff);
			tiles[0] = 0;
		}
	}
	
	public void clear() {
        Arrays.fill(pixels, 0);
	}
	
	public void render(int widthOffset, int heightOffset) {

		for(int row = 0; row < height; row++) {
			int y = row + heightOffset;
			if(y < 0 || y >= height) continue;
			for(int column = 0; column < width;column++ ) {
				int x = column + widthOffset;
				if(x < 0  || x >= width) continue;
				pixels[x + y * width] = Sprite.grass.pixels[(column & (Sprite.grass.size -1)) + (row & (Sprite.grass.size -1)) * Sprite.grass.size];
			}
		}
	}

	// render square thats why the same size in y and x.
	public void renderTile(int xPixel, int yPixel, Tile tile){
		for (int y = 0; y < tile.sprite.size; y++) {
			int absoluteYPosition = yPixel + y;
			for (int x = 0; x < tile.sprite.size; x++) {
				int absoluteXPosition = xPixel + x;

				// so we only render the tiles that are visible on our monitor and nothing else
				if(absoluteXPosition < 0 || absoluteXPosition >= width || absoluteYPosition < 0 || absoluteYPosition >= width) {
					break;
				}
				pixels[absoluteXPosition * width] = tile.sprite.pixels[x + y * tile.sprite.size];
			}
		}
	}
}

