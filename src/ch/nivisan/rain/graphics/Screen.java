package ch.nivisan.rain.graphics;

import java.util.Random;

public class Screen {
	public int width , height;
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
		for(int i = 0; i< pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	
	public void render(int widthOffset, int heightOffset) {

		for(int row = 0; row < height; row++) {
			// check negative bounds
			int y = row + heightOffset; 
			
		//	if(row < 0 || row >= height) break;
			
			for(int column = 0; column < width;column++ ) {
				int x = column + widthOffset;
				
				// check negative bounds
			 //	if(column < 0 || column >= width) break;
				
				// (column >> 4) is the same as (column / tileSize)  but faster since it's 
				// 2**4 = 16 we can bitwise shift to make it faster
				// divide by 16 because we need to "rasterize" our grid into tiles of 16
				// the bitwise & operator is for, when we are going outside of 63 meaning to 64 it will return to 0 
				// instead of giving us a exception
				int tileIndex = ((x >> 4) & MapSizeMask) + ((y >> 4) & MapSizeMask) * MapSize;
				
				// multiplication by width because we need to go through all rows.
				// e.g 0*300 + 0 or 1*300 + 20 or 20*300 
				// since this is a single dimension array.
				pixels[column + (row * width)] = tiles[tileIndex];
			}
		}
	}
}

