package ch.nivisan.rain.graphics;

public class Sprite {
    public final int size;
    private int x,y;
    public int[] pixels;
    private SpriteSheet sheet;

    public static Sprite grass = new Sprite(16 ,0 ,0, SpriteSheet.tiles);

    public Sprite(int size , int x, int y,SpriteSheet sheet)
    {
        this.size = size;
        this.x = x * size;
        this.y = y * size;
        this.sheet = sheet;
        pixels = new int[size * size];

       loadSpriteFromSheet();
    }

    private void loadSpriteFromSheet()
    {
        for (int y = 0; y < size; y++)
        {
            for (int x = 0; x < size; x++)
            {
                pixels[x + y * size] = sheet.pixels[(x + this.x) + (y + this.y) * sheet.size];
            }
        }

    }

}
