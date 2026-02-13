package ch.nivisan.rain.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import static ch.nivisan.rain.utils.MathUtils.clamp;

public class ImageUtils {
    private ImageUtils() {
    }


    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public static BufferedImage changeBrightness(int amount, Image original) {
        return changeBrightness(toBufferedImage(original), amount);
    }

    /**
     * Changes brightness of a given image and returns a new one.
     * Runs stable in terms of fps.
     *
     * @param original the original image as bufferedImage
     * @param amount   the amount of points by which the image gets increased/decreased overall
     * @return new image with given change in brightness
     */
    public static BufferedImage changeBrightness(BufferedImage original, int amount) {
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        int[] pixels = new int[original.getWidth() * original.getHeight()];
        int[] resultPixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();
        original.getRGB(0, 0, original.getWidth(), original.getHeight(), pixels, 0, original.getWidth());


        for (int pixelY = 0; pixelY < original.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < original.getWidth(); pixelX++) {
                int color = pixels[pixelY * original.getWidth() + pixelX];
                int red = (color & 0xff0000) >> 16;
                int green = (color & 0xff00) >> 8;
                int blue = (color & 0xff);

                red = clamp(red + amount, 0, 255);
                green = clamp(green + amount, 0, 255);
                blue = clamp(blue + amount, 0, 255);
                color &= 0xff000000;
                int brightenedColor = (color & 0xff000000) | red << 16 | green << 8 | blue;
                resultPixels[pixelX + pixelY * original.getWidth()] = brightenedColor;
            }
        }
        return result;
    }

    /**
     * Same as changeBrightness but as byte array instead of int array
     * Seems to have some performance impact on Linux based systems, when idle therefor not used.
     * Upon hover, seems to stabilize fps counter somehow.
     * - Not used.
     * <p>
     * Changes brightness of a given image and returns a new one based on amount of brightness change
     *
     * @param original the original image as bufferedImage
     * @param amount   the amount of points by which the image gets increased/decreased overall
     * @return new image with given change in brightness
     */
    public static BufferedImage changeBrightnessByte(BufferedImage original, int amount) {
        BufferedImage result = new BufferedImage(original.getWidth(), original.getHeight(), BufferedImage.TYPE_INT_ARGB);
        byte[] pixels = ((DataBufferByte) original.getRaster().getDataBuffer()).getData();
        int[] resultPixels = ((DataBufferInt) result.getRaster().getDataBuffer()).getData();


        int offset = 0;
        for (int pixelY = 0; pixelY < original.getHeight(); pixelY++) {
            for (int pixelX = 0; pixelX < original.getWidth(); pixelX++) {
                int alpha = pixels[offset++];
                int red = pixels[offset++];
                int green = pixels[offset++];
                int blue = pixels[offset++];

                red = clamp(red + amount, 0, 255);
                green = clamp(green + amount, 0, 255);
                blue = clamp(blue + amount, 0, 255);
                int brightenedColor = alpha << 24 | red << 16 | green << 8 | blue;
                resultPixels[pixelX + pixelY * original.getWidth()] = brightenedColor;
            }
        }
        return result;
    }
}
