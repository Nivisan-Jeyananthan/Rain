package ch.nivisan.rain.graphics;

import ch.nivisan.rain.entity.mob.FlipState;
import ch.nivisan.rain.level.tile.Tile;

import java.util.Arrays;
import java.util.Random;

public class Screen {
    public final int MapSize = 8;
    public final int MapSizeMask = MapSize - 1;
    private final Random random = new Random();
    public int width, height;
    // which pixels of the screen get rendered
    public int[] pixels;
    public int[] tiles = new int[MapSize * MapSize];
    // offests to keep our player centered as main focus
    private int xOffset, yOffset;


    public Screen(int width, int height) {
        this.width = width;
        this.height = height;

        pixels = new int[width * height];

        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = random.nextInt(0xffffff);
            tiles[0] = 0;
        }
    }

    public void clear() {
        Arrays.fill(pixels, 0);
    }

    // when the player moves we need to move our tiles accordingly
    // thats why we have offsets calculated with them
    public void renderTile(int xTilePosition, int yTilePosition, Tile tile) {
        xTilePosition -= xOffset;
        yTilePosition -= yOffset;

        // iterate through each pixel in our tile and set it to main pixels
        for (int yPixel = 0; yPixel < tile.sprite.size; yPixel++) {
            int absoluteYPosition = yTilePosition + yPixel;
            for (int xPixel = 0; xPixel < tile.sprite.size; xPixel++) {
                int absoluteXPosition = xTilePosition + xPixel;

                // so we only render the tiles that are visible on our monitor and nothing else
                if (absoluteXPosition < -tile.sprite.size || absoluteXPosition >= width || absoluteYPosition < 0 || absoluteYPosition >= height) {
                    break;
                }

                if (absoluteXPosition < 0)
                    absoluteXPosition = 0;

                int index = absoluteXPosition + absoluteYPosition * width;
                int spriteIndex = xPixel + yPixel * tile.sprite.size;
                pixels[index] = tile.sprite.pixels[spriteIndex];
            }
        }
    }

    public void renderSprite(int xTilePosition, int yTilePosition, Sprite sprite) {
        xTilePosition -= xOffset;
        yTilePosition -= yOffset;

        // iterate through each pixel in our tile and set it to main pixels
        for (int yPixel = 0; yPixel < sprite.size; yPixel++) {
            int absoluteYPosition = yTilePosition + yPixel;
            for (int xPixel = 0; xPixel < sprite.size; xPixel++) {
                int absoluteXPosition = xTilePosition + xPixel;

                // so we only render the tiles that are visible on our monitor and nothing else
                if (absoluteXPosition < -sprite.size || absoluteXPosition >= width || absoluteYPosition < 0 || absoluteYPosition >= height) {
                    break;
                }

                if (absoluteXPosition < 0)
                    absoluteXPosition = 0;

                int index = absoluteXPosition + absoluteYPosition * width;
                int spriteIndex = xPixel + yPixel * sprite.size;
                pixels[index] = sprite.pixels[spriteIndex];
            }
        }
    }

    public void renderPlayer(int xPosition, int yPosition, Sprite sprite, FlipState flip) {
        xPosition -= xOffset;
        yPosition -= yOffset;

        for (int yPixel = 0; yPixel < sprite.size; yPixel++) {
            int absoluteYPosition = yPosition + yPixel;
            int yPixelFlipped = yPixel;

            if (flip == FlipState.YFlipped || flip == FlipState.XYFlipped) {
                yPixelFlipped = (sprite.size - 1) - yPixel;
            }
            for (int xPixel = 0; xPixel < sprite.size; xPixel++) {
                int absoluteXPosition = xPosition + xPixel;
                int xPixelFlipped = xPixel;
                if (flip == FlipState.XFlipped || flip == FlipState.XYFlipped) {
                    xPixelFlipped = (sprite.size - 1) - xPixel;
                }

                // so we only render the tiles that are visible on our monitor and nothing else
                if (absoluteXPosition < -sprite.size || absoluteXPosition >= width || absoluteYPosition < 0 || absoluteYPosition >= height) {
                    break;
                }

                if (absoluteXPosition < 0)
                    absoluteXPosition = 0;

                int color = sprite.pixels[xPixelFlipped + yPixelFlipped * sprite.size];
                // Because we are loading the image using RBA and not RGB we need to add another ff in the beginning.
                // so instead of the hex color code only, we also add the alpha channel code at the beginning.
                int transparentColor = 0xffff00ff;

                if (color != transparentColor) {
                    pixels[absoluteXPosition + absoluteYPosition * width] = color;
                }
            }
        }
    }

    public void setOffsets(int xOffset, int yOffset) {
        this.yOffset = yOffset;
        this.xOffset = xOffset;
    }
}

