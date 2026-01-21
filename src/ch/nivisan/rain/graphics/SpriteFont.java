package ch.nivisan.rain.graphics;

public class SpriteFont {
    private static SpriteSheet fontSheet = new SpriteSheet("../assets/fonts/arial.png", 208, 96);
    private static Sprite[] fontCharacter = Sprite.split(fontSheet, 16, 16);
    private static final String charactes = "ABCDEFGHIJKLM" + //
                                            "NOPQRSTUVWXYZ" + //
                                            "abcdefghijklm" + //
                                            "nopqrstuvwxyz" + //
                                            "0123456789.,'" + //
                                            "\"();:!@$%-+";



    private int size;
    private int color;

    public void render(String text,Screen screen) {
        int x = 50;
        int y = 50;
        int yOffset = 0;
        int xOffset = 0;

        for (int i = 0; i < text.length(); i++) {
            yOffset = 0;
            var currentCharacter = text.charAt(i);
            if(currentCharacter == 'g'|| currentCharacter == 'y' || currentCharacter == 'q' || currentCharacter == 'p'  || currentCharacter == 'j' || currentCharacter == ',') {
                yOffset = 4;
            }
            var index = charactes.indexOf(currentCharacter);
            if(index == -1) continue;
            screen.renderSprite( x + i * 16,y + yOffset, fontCharacter[index], false,true);
        }
    }

}
