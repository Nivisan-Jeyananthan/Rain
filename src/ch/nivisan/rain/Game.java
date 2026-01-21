package ch.nivisan.rain;

import ch.nivisan.rain.entity.mob.Player;
import ch.nivisan.rain.graphics.Screen;
import ch.nivisan.rain.graphics.SpriteFont;
import ch.nivisan.rain.input.Keyboard;
import ch.nivisan.rain.input.Mouse;
import ch.nivisan.rain.level.Level;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.Serial;

public class Game extends Canvas implements Runnable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final int scale = 3;
    private static final int width = 300;
    private static final int height = width / 16 * 9;
    private static final String title = "Rain";
    private final JFrame frame;
    private final Screen screen;
    private final Keyboard keyboard;
    private final Level level;
    private final Player player;
    private SpriteFont font;

    // creating an image
    private final BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // allowing to draw to the image or accessing the image
    // area in memory where buffer data is located, not a copy of it.
    // that is why we can manipulate pixels and it changes the buffer data itself.
    private final int[] pixels = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();
    private Thread gameThread;
    private boolean isRunning = false;

    public Game() {
        var size = new Dimension(width * scale, height * scale);
        setPreferredSize(size);

        screen = new Screen(width, height);
        frame = new JFrame();

        keyboard = new Keyboard();
        addKeyListener(keyboard);
        Mouse mouse = new Mouse();
        addMouseListener(mouse);
        addMouseMotionListener(mouse);

        level = Level.spawn;
        player = new Player(20 << 4,60 << 4, keyboard, level);
        level.addEntity(player);
        font = new SpriteFont();
    }

    public static int getWindowWidth() {
        return width * scale;
    }

    public static int getWindowHeight() {
        return height * scale;
    }

    static void main(String[] args) {
        var game = new Game();

        game.frame.setResizable(false);
        game.frame.setTitle(Game.title);
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);

        game.start();
    }

    public synchronized void start() {
        isRunning = true;
        gameThread = new Thread(this, "Display");
        gameThread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long timer = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        final float framerate = 60.0f;
        final float nanosecondsInSeconds = 1_000_000_000.0f;
        // calculate how many nanoseconds it takes to update the game once
        // this is so we can calculate how many it would need for 60 frames per second
        // calculate how much time each frame should take
        final float nanoFrame = nanosecondsInSeconds / framerate;
        // delta is used to keep track how much time has passed since last update
        float delta = 0;
        int framesPerSecond = 0;
        int updatesPerSecond = 0;
        requestFocus();


        while (isRunning) {

            long now = System.nanoTime();

            // calculates how much time has passed since the last loop
            // then determines how much this calculates into a fraction of a second (frame)
            // and adds it to delta.
            // when delta reaches 1, a frame is ready to be updated so our game runs at e.g 60 fps
            // check underneath to see if it's > 1.
            //	System.out.println("LastTime :" + lastTime + " now: "+ now);
            // +++ rename to either :
            // ++++ elapsedFrames , timeAccumulated, frameTime , updateTime
            delta += (now - lastTime) / nanoFrame;
            //	System.out.println("Delta is : " + delta);
            //	System.out.println("Time taken to update: " + (now - lastTime));
            lastTime = now;

            // wait until 1/60 of a second has gone by (determinted by framerate)
            // so start the update function of the game which makes games run at specific frames per second
            while (delta >= 1) {
                update();
                updatesPerSecond++;
                delta--;
            }
            render();
            framesPerSecond++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle(title + " | " + updatesPerSecond + " ups, " + framesPerSecond + "fps ");
                framesPerSecond = 0;
                updatesPerSecond = 0;
            }
        }
        stop();
    }

    public void update() {
        keyboard.update();
        level.update();
    }

    public void render() {
        var bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        // updates the values of xScroll and yScroll every render, so we accurately track, which coordinate is
        // the left most pixel (x) and the top most (y) = x,y so we can render the entire screen.
        // how many pixels our character moved so we have adjust the map accordingly
        // when our player moved 2 pixels right, map should move 2 pixel left
        // offset essentially
        int xScroll = (int) (player.getX() - (float) screen.width / 2);
        int yScroll = (int) (player.getY() - (float) screen.height / 2);

        level.render(xScroll, yScroll, screen);
        font.render("game,jam",screen);

        System.arraycopy(screen.pixels, 0, pixels, 0, pixels.length);

        // links the graphics (where on is able to draw on the screen) with the buffer.
        var graphics = bs.getDrawGraphics();
        graphics.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), null);
        graphics.setColor(Color.WHITE);
        graphics.setFont(new Font("Verdana", 0, 30));
        graphics.drawString("Player X: " + (player.getX() / 4) + " Y: " + (player.getY() / 4), 600, 25);
        graphics.drawString("Pixel X: " + (player.getX()) + " Y: " + (player.getY()), 600, 50);
        graphics.drawString("Mouse X: " + (Mouse.getXPosition()) + " Y: " + (Mouse.getYPosition()), 600, 75);

        // release system ressource
        graphics.dispose();
        // changes the buffers which reside in memory
        bs.show();
    }
}