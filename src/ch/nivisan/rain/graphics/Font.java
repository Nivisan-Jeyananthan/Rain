package ch.nivisan.rain.graphics;

public class Font {
    private int size;
    private int color;

    private static SpriteSheet font = new SpriteSheet("../assets/fonts/arial.png", 16);

    private static SpriteSheet fontCharacter = new SpriteSheet(font,0,0,13,2,16,16);
    private static SpriteSheet fontNumbers = new SpriteSheet(font,0,2,13,1,16,16);
    private static SpriteSheet fontSymbols = new SpriteSheet(font,0,3,13,1,16,16);

    public void render(Screen screen){

    }

}
