package Binary;

import ch.nivisan.raincloud.serialization.SerializationWriter;

public class Main {
    private static void printHex(int value)
    {
        System.out.printf("%x\n", value);
    }

    private static void printBin(int value)
    {
        System.out.println(Integer.toBinaryString(value));
    }


    public static void main(String[] args)
    {
        int color = 0xFF00FF;

        int r = (color & 0xFF0000) >> 16;
        int g = (color & 0x00FF00) >> 8;
        int b = (color & 0x0000FF);

        printHex(r);
        printHex(g);
        printHex(b);

        // decrease brightness
        r -= 50;
        g -= 0;
        b -= 50;

        int result = r << 16 | g << 8 | b << 0;

        printHex(result);

        int value = 0x1234;

        int a = (value & 0xff00) >> 8;

        printHex(a);
        
        SerializationWriter.test();
    }
}