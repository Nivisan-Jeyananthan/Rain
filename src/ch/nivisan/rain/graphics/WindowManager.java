package ch.nivisan.rain.graphics;

public class WindowManager {
    private static final int scale = 3;
    private static final int gameWidth = 300;
    private static final int guiWidth = (gameWidth / 3);
    private static final int windowHeight = (gameWidth + guiWidth) / 16 * 9;

    private WindowManager() {
    }

    public static int getScale() {
        return scale;
    }

    public static int getWindowWidth() {
        return gameWidth + guiWidth;
    }

    public static int getGameWidth() {
        return gameWidth;
    }

    public static int getGUIWidth() {
        return guiWidth;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static int getScaledWindowWidth() {
        return (gameWidth * scale) + (guiWidth * scale);
    }

    public static int getScaledGUIWidth() {
        return guiWidth * scale;
    }

    public static int getScaledGameWidth() {
        return gameWidth * scale;
    }

    public static int getScaledWindowHeight() {
        return windowHeight * scale;
    }
}
