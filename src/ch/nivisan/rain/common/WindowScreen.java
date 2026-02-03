package ch.nivisan.rain.common;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class WindowScreen extends Canvas {
    private BufferStrategy bufferStrategy;
    private Graphics graphics;

    public WindowScreen(int width, int height){
        setPreferredSize(new Dimension(width, height));
    }

    public void init(){
        createBufferStrategy(3);

    }

    public void beginRendering(){
        bufferStrategy = getBufferStrategy();
        graphics = bufferStrategy.getDrawGraphics();
    }

    public void clear(){
        graphics.setColor(Color.white);
        graphics.fillRect(0,0,getWidth(),getHeight());
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    public void endRendering(){
        graphics.dispose();
        bufferStrategy.show();
    }
}
