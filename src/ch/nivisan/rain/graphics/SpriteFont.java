package ch.nivisan.rain.graphics;

public class SpriteFont {
    private static SpriteSheet fontSheet = new SpriteSheet("../assets/fonts/arial.png", 208, 96);
    private static Sprite[] fontCharacter = Sprite.split(fontSheet, 16, 16);
    private int size;
    private int color;

    public void render(Screen screen) {
        screen.renderSprite(50, 50, fontCharacter[4], false);
    }

}
