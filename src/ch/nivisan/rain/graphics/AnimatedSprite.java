package ch.nivisan.rain.graphics;

public class AnimatedSprite extends Sprite {
    private int frames = 0;
    private int rate = 5;
    private Sprite currentSprite;
    private int animationLength = -1;
    private int time = 0;
    private int[] cycle = new int[]{0, 1, 0, 2};
    private int frameIndex = 0;

    /**
     * Create a new animated sprite
     *
     * @param spriteSheet
     * @param width
     * @param height
     * @param animationLength How many sprites we wish to use of our total. Can be used to crop.
     */
    public AnimatedSprite(SpriteSheet spriteSheet, int width, int height, int animationLength) {
        super(spriteSheet, width, height);

        if (spriteSheet.getSprites().length > 0) {
            this.currentSprite = spriteSheet.getSprites()[0];
        } else {
            System.err.println("Provided Spritesheet with length of 0");
        }

        this.animationLength = animationLength;
        if (animationLength > sheet.getSprites().length) {
            System.out.println("Error length of animation exceeded!");
        }
    }

    public void update() {
        time++;
        if (time % rate == 0) {
            if (frames >= animationLength - 1) {
                frames = 0;
            } else {
                frames++;
            }

            frameIndex++;
            if (frameIndex > animationLength) {
                frameIndex = 0;
            }
            currentSprite = sheet.getSprites()[cycle[frameIndex]];
        }
    }

    public Sprite getSprite() {
        return currentSprite;
    }

    public void setFrameRate(int frameRate) {
        this.rate = frameRate;
    }

    public void setFrame(int frameIndex) {
        currentSprite = sheet.getSprites()[(frameIndex & sheet.getSprites().length - 1)];
    }
}