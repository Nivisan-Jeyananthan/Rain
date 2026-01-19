package ch.nivisan.rain.utils;

import ch.nivisan.rain.graphics.Screen;

public class Debug {
    private Debug(){}

    public static void drawRectangle(Screen screen, int x, int y, int width, int height , boolean fixed){
        screen.drawRectangle(x,y,width,height,0xff000,fixed);
    }

    public static void drawRectangle(Screen screen, int x, int y, int width, int height,int color , boolean fixed){
        screen.drawRectangle(x,y,width,height,color,fixed);
    }
}
