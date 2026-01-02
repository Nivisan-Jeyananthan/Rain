package ch.nivisan.rain.graphics;

import ch.nivisan.rain.entity.mob.Player;
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

	public int xOffset, yOffset;

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

	// render square thats why the same size in y and x.
	public void renderTile(int xPosition, int yPosition, Tile tile){
		xPosition -= xOffset;
		yPosition -= yOffset;

		for (int yPixel = 0; yPixel < tile.sprite.size; yPixel++) {
			int absoluteYPosition = yPosition + yPixel;
			for (int xPixel = 0; xPixel < tile.sprite.size; xPixel++) {
				int absoluteXPosition = xPosition + xPixel;

				// so we only render the tiles that are visible on our monitor and nothing else
				if(absoluteXPosition < -tile.sprite.size || absoluteXPosition >= width || absoluteYPosition < 0 || absoluteYPosition >= height) {
					break;
				}

				if(absoluteXPosition < 0)
					absoluteXPosition = 0;

				pixels[absoluteXPosition + absoluteYPosition * width] = tile.sprite.pixels[xPixel + yPixel * tile.sprite.size];
			}
		}
	}

	public void renderPlayer(int xPosition, int yPosition, Sprite sprite){
		xPosition -= xOffset;
		yPosition -= yOffset;

		for (int yPixel = 0; yPixel < sprite.size; yPixel++) {
			int absoluteYPosition = yPosition + yPixel;
			for (int xPixel = 0; xPixel < sprite.size; xPixel++) {
				int absoluteXPosition = xPosition + xPixel;

				// so we only render the tiles that are visible on our monitor and nothing else
				if(absoluteXPosition < -sprite.size || absoluteXPosition >= width || absoluteYPosition < 0 || absoluteYPosition >= height) {
					break;
				}

				if(absoluteXPosition < 0)
					absoluteXPosition = 0;

				int color = sprite.pixels[xPixel + yPixel * 16];
				// Because we are loading the image using RBA and not RGB we need to add another ff in the beginning.
				// so instead of the hex color code only, we also add the alpha channel code at the beginning.
				int transparentColor = 0xffff00ff;

				if(color != transparentColor) {
					pixels[absoluteXPosition + absoluteYPosition * width] = color;
				}
			}
		}
	}

	public void setOffsets(int xOffset, int yOffset){
		this.yOffset = yOffset;
		this.xOffset = xOffset;
	}
}

