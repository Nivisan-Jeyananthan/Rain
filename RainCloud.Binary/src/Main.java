import java.util.Random;

import ch.nivisan.raincloud.serialization.fields.*;
import ch.nivisan.raincloud.serialization.arrays.*;

public class Main {
    private static void printHex(int value) {
        System.out.printf("%x\n", value);
    }

    private static void printBin(int value) {
        System.out.println(Integer.toBinaryString(value));
    }

    static void printBytes(byte[] data) {
        for (int i = 0; i < data.length; i++) {
            System.out.printf("0x%x ", data[i]);
        }
    }

    public static void main(String[] args) {
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

        byte[] data = new byte[16];
        var number = new boolean[4];
        for (int i = 0; i < number.length - 1; i++) {
            number[i] = new Random().nextBoolean();
        }

        int pointer = SerializationWriter.writeBitfield(data, 0, number);
        pointer = SerializationWriter.writeBytes(data, pointer, false);
        pointer = SerializationWriter.writeBytes(data, pointer, false);
        pointer = SerializationWriter.writeBytes(data, pointer, true);

        // SerializationWriter.writeBytes(null, 0, 1.1f);

        byte[] names = new byte[] { 0x0, 0x0, 0x27, 0x18 };
        short name = 15000;
        int beginPointer = pointer;
        pointer = SerializationWriter.writeBytes(data, pointer, name);

        short end = SerializationWriter.readShort(data, beginPointer);
        System.out.println("my gage: " + end);
        printBytes(data);

        System.out.println("");
        System.out.println("Data comes: ");
        
        var test = new int[] { 400, 20040,3844};
        ch.nivisan.raincloud.serialization.arrays.Array field = new IntegerArray("test", test);

        byte[] dataNew = new byte[24];
        field.getBytes(dataNew, 0);
        printBytes(dataNew);
        
        
    }
}
