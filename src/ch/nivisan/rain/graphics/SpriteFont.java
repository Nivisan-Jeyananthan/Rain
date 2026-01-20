package ch.nivisan.rain.graphics;

public class SpriteFont {
    private int size;
    private int color;

    private static SpriteSheet fontSheet = new SpriteSheet("../assets/fonts/arial.png",  208, 96);
    private static SpriteSheet fontCharacter = new SpriteSheet(fontSheet,0,0,13,2,16,16);
    private Sprite[] sprites = fontCharacter.getSprites();

    public void render(Screen screen){
        screen.renderSprite(50,50,sprites[4],false);
    }

}
