package ch.nivisan.rain.graphics;

public class AnimatedSprite extends Sprite{
    private int frames = 0;
    private int rate = 5;
    private Sprite sprite;
    private int animationLength = -1;
    private int time = 0;

    /**
     * Create a new animated sprite
     * @param spriteSheet
     * @param width
     * @param height
     * @param animationLength How many sprites we wish to use of our total. Can be used to crop.
     */
    public AnimatedSprite(SpriteSheet spriteSheet, int width,int height, int animationLength){
        super(spriteSheet, width,height);
        this.sprite = spriteSheet.getSprites()[0];
        this.animationLength = animationLength;
        if(animationLength > sheet.getSprites().length){
            System.out.println("Error length of animation exceeded!");
        }
    }

    public void update(){
        time++;
        if((time % rate) == 0) {
            if (frames >= animationLength -1) frames = 0;
            else frames++;
            sprite = sheet.getSprites()[frames];
        }
        System.out.println(sprite + ", frame: " + frames);
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setFrameRate(int frameRate){
        this.rate = frameRate;
    }
}