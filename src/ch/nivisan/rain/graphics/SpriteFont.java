package ch.nivisan.rain.graphics;

public class SpriteFont {
    private static final int defaultColor = 0xfffccfff;
    private static final String charactes = "ABCDEFGHIJKLM" + //
            "NOPQRSTUVWXYZ" + //
            "abcdefghijklm" + //
            "nopqrstuvwxyz" + //
            "0123456789.,'" + //
            "\"();:!@$%-+";
    private static final SpriteSheet fontSheet = new SpriteSheet("../assets/fonts/arial.png", 208, 96);
    private static final Sprite[] fontCharacter = Sprite.split(fontSheet, 16, 16);
    private int spacing = 0;
    private int color = 0;
    private int x = 0;
    private int y = 0;

    public SpriteFont setSpacing(int spacing) {
        this.spacing = spacing;
        return this;
    }

    public SpriteFont setY(int y) {
        this.y = y;
        return this;
    }

    public SpriteFont setX(int x) {
        this.x = x;
        return this;
    }

    public SpriteFont setColor(int color) {
        this.color = color;
        return this;
    }

    public void render(String text, Screen screen) {
        int yOffset = 0;
        int xOffset = 0;
        int currentLine = 0;

        for (int i = 0; i < text.length(); i++) {
            yOffset = 0;
            xOffset += 16 + spacing;
            var currentCharacter = text.charAt(i);
            if (currentCharacter == '\n') {
                currentLine++;
                xOffset = 0;
            }
            if (currentCharacter == 'g' || currentCharacter == 'y' || currentCharacter == 'q' || currentCharacter == 'p' || currentCharacter == 'j' || currentCharacter == ',') {
                yOffset += 4;
            }
            var index = charactes.indexOf(currentCharacter);
            if (index == -1) continue;
            screen.renderSprite(x + xOffset, y + (currentLine * 20) + yOffset, fontCharacter[index], false, true, color != 0 ? color : defaultColor);
        }
    }

}
