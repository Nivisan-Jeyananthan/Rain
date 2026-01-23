package ch.nivisan.rain.graphics;

import ch.nivisan.rain.entity.mob.*;
import ch.nivisan.rain.entity.projectile.Projectile;
import ch.nivisan.rain.level.tile.Tile;

import java.util.Arrays;
import java.util.Random;

public class Screen {
    public final int MapSize = 8;
    public final int MapSizeMask = MapSize - 1;
    private final Random random = new Random();
    public int width, height;
    // which pixels of the screen get rendered
    private int[] pixels;
    public int[] tiles = new int[MapSize * MapSize];
    // offests to keep our player centered as main focus
    private int xOffset, yOffset;
    private static final int alphaColor = 0xffff00ff;

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

    public void renderSheet(int xTilePosition, int yTilePosition, SpriteSheet spriteSheet, boolean fixed) {
        if (fixed) {
            xTilePosition -= xOffset;
            yTilePosition -= yOffset;
        }

        for (int yPixel = 0; yPixel < spriteSheet.getHeight(); yPixel++) {
            int absoluteYPosition = yPixel + yTilePosition;
            for (int xPixel = 0; xPixel < spriteSheet.getWidth(); xPixel++) {
                int absoluteXPosition = xPixel + xTilePosition;

                if (absoluteXPosition < 0 || absoluteXPosition >= width || absoluteYPosition < 0
                        || absoluteYPosition >= height) {
                    continue;
                }

                int index = absoluteXPosition + absoluteYPosition * width;
                int spriteIndex = xPixel + yPixel * spriteSheet.getWidth();
                pixels[index] = spriteSheet.getPixels()[spriteIndex];
            }
        }
    }

    public void renderSprite(int xTilePosition, int yTilePosition, Sprite sprite, boolean fixed, boolean transparent,
            int newColor) {
        if (fixed) {
            xTilePosition -= xOffset;
            yTilePosition -= yOffset;
        }

        for (int yPixel = 0; yPixel < sprite.getHeight(); yPixel++) {
            int absoluteYPosition = yPixel + yTilePosition;
            for (int xPixel = 0; xPixel < sprite.getWidth(); xPixel++) {
                int absoluteXPosition = xPixel + xTilePosition;

                if (absoluteXPosition < 0 || absoluteXPosition >= width || absoluteYPosition < 0
                        || absoluteYPosition >= height) {
                    continue;
                }

                int index = absoluteXPosition + absoluteYPosition * width;
                int spriteIndex = xPixel + yPixel * sprite.getWidth();
                int color = sprite.getPixels()[spriteIndex];
                if (!transparent || color != alphaColor) {
                    pixels[index] = newColor;
                }

            }
        }
    }

    // when the player moves we need to move our tiles accordingly
    // thats why we have offsets calculated with them
    public void renderTile(int xTilePosition, int yTilePosition, Tile tile) {
        int pixelIndex;
        int spriteIndex;
        xTilePosition -= xOffset;
        yTilePosition -= yOffset;

        // iterate through each pixel in our tile and set it to main pixels
        for (int yPixel = 0; yPixel < tile.sprite.getHeight(); yPixel++) {
            int absoluteYPosition = yTilePosition + yPixel;
            for (int xPixel = 0; xPixel < tile.sprite.getWidth(); xPixel++) {
                int absoluteXPosition = xTilePosition + xPixel;

                // so we only render the tiles that are visible on our monitor and nothing else
                if (absoluteXPosition < -tile.sprite.getSize() || absoluteXPosition >= width || absoluteYPosition < 0
                        || absoluteYPosition >= height) {
                    break;
                }

                if (absoluteXPosition < 0)
                    absoluteXPosition = 0;

                pixelIndex = absoluteXPosition + absoluteYPosition * width;
                spriteIndex = xPixel + yPixel * tile.sprite.getSize();
                pixels[pixelIndex] = tile.sprite.getPixels()[spriteIndex];
            }
        }
    }

    public void renderProjectile(int xTilePosition, int yTilePosition, Projectile projectile) {
        int index;
        int spriteIndex;
        int color;
        xTilePosition -= xOffset;
        yTilePosition -= yOffset;
        var sprite = projectile.getSprite();

        // iterate through each pixel in our tile and set it to main pixels
        for (int yPixel = 0; yPixel < sprite.getSize(); yPixel++) {
            int absoluteYPosition = yTilePosition + yPixel;
            for (int xPixel = 0; xPixel < sprite.getSize(); xPixel++) {
                int absoluteXPosition = xTilePosition + xPixel;

                // so we only render the tiles that are visible on our monitor and nothing else
                if (absoluteXPosition < -sprite.getSize() || absoluteXPosition >= width || absoluteYPosition < 0
                        || absoluteYPosition >= height) {
                    break;
                }

                if (absoluteXPosition < 0)
                    absoluteXPosition = 0;

                index = absoluteXPosition + absoluteYPosition * width;
                spriteIndex = xPixel + yPixel * sprite.getSize();
                color = sprite.getPixels()[spriteIndex];

                if (color != alphaColor) {
                    pixels[index] = color;
                }
            }
        }
    }

    public void renderMob(int xPosition, int yPosition, Sprite sprite, FlipState flip) {
        int color;
        int absoluteYPosition;
        int yPixelFlipped;
        xPosition -= xOffset;
        yPosition -= yOffset;

        for (int yPixel = 0; yPixel < sprite.getSize(); yPixel++) {
            absoluteYPosition = yPosition + yPixel;
            yPixelFlipped = yPixel;

            if (flip == FlipState.YFlipped || flip == FlipState.XYFlipped) {
                yPixelFlipped = (sprite.getSize() - 1) - yPixel;
            }
            for (int xPixel = 0; xPixel < sprite.getSize(); xPixel++) {
                int absoluteXPosition = xPosition + xPixel;
                int xPixelFlipped = xPixel;
                if (flip == FlipState.XFlipped || flip == FlipState.XYFlipped) {
                    xPixelFlipped = (sprite.getSize() - 1) - xPixel;
                }

                // so we only render the tiles that are visible on our monitor and nothing else
                if (absoluteXPosition < -sprite.getSize() || absoluteXPosition >= width || absoluteYPosition < 0
                        || absoluteYPosition >= height) {
                    break;
                }

                if (absoluteXPosition < 0)
                    absoluteXPosition = 0;

                color = sprite.getPixels()[xPixelFlipped + yPixelFlipped * sprite.getSize()];

                // Because we are loading the image using RBA and not RGB we need to add another
                // ff in the beginning.
                // so instead of the hex color code only, we also add the alpha channel code at
                // the beginning.

                if (color != alphaColor) {
                    pixels[absoluteXPosition + absoluteYPosition * width] = color;
                }
            }
        }
    }

    public void renderMob(int xPosition, int yPosition, Mob mob, int swapColor) {
        Sprite sprite = mob.getSprite();
        int bodyColor = 0xff472BBF;
        int color;

        xPosition -= xOffset;
        yPosition -= yOffset;

        for (int yPixel = 0; yPixel < sprite.getSize(); yPixel++) {
            int absoluteYPosition = yPosition + yPixel;

            for (int xPixel = 0; xPixel < sprite.getSize(); xPixel++) {
                int absoluteXPosition = xPosition + xPixel;
                // so we only render the tiles that are visible on our monitor and nothing else
                if (absoluteXPosition < -sprite.getSize() || absoluteXPosition >= width || absoluteYPosition < 0
                        || absoluteYPosition >= height) {
                    break;
                }

                if (absoluteXPosition < 0)
                    absoluteXPosition = 0;

                color = sprite.getPixels()[xPixel + yPixel * sprite.getSize()];

                if (swapColor != 0 && color == bodyColor)
                    color = swapColor;

                // Because we are loading the image using RBA and not RGB we need to add another
                // ff in the beginning.
                // so instead of the hex color code only, we also add the alpha channel code at
                // the beginning.
                if (color != alphaColor) {
                    pixels[absoluteXPosition + absoluteYPosition * width] = color;
                }
            }
        }
    }

    /**
     * Renders a rectangle if it can, otherwise parts of it as only bottom part,
     * top, left or right.
     * Depending on if it will fit on the screen
     * 
     * @param xStart
     * @param yStart
     * @param width
     * @param height
     * @param color
     * @param fixed
     */
    public void drawRectangle(int xStart, int yStart, int width, int height, int color, boolean fixed) {
        if (fixed) {
            xStart -= xOffset;
            yStart -= yOffset;
        }

        for (int x = xStart; x <= xStart + width; x++) {
            if (x < 0 || x >= this.width || yStart >= this.height)
                continue;

            if (yStart > 0)
                pixels[x + yStart * this.width] = color;
            if (yStart + height >= this.height || yStart + height < 0)
                continue;
            pixels[x + (yStart + height) * this.width] = color;
        }

        for (int y = yStart; y <= yStart + height; y++) {
            if (xStart >= this.width || y < 0 || y >= this.height)
                continue;

            if (xStart > 0)
                pixels[xStart + y * this.width] = color;
            if (xStart + width >= this.width || xStart + width < 0)
                continue;
            pixels[(xStart + width) + (y * this.width)] = color;
        }
    }

    public void setOffsets(int xOffset, int yOffset) {
        this.yOffset = yOffset;
        this.xOffset = xOffset;
    }

    public int[] getPixels() { return pixels; }
}
