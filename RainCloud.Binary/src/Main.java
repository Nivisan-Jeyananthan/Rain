import java.util.Random;

import ch.nivisan.raincloud.serialization.fields.*;
import ch.nivisan.raincloud.serialization.DbDeserializer;
import ch.nivisan.raincloud.serialization.FileService;
import ch.nivisan.raincloud.serialization.RCDatabase;
import ch.nivisan.raincloud.serialization.RCObject;
import ch.nivisan.raincloud.serialization.RCString;
import ch.nivisan.raincloud.serialization.SerializationReader;
import ch.nivisan.raincloud.serialization.SerializationWriter;
import ch.nivisan.raincloud.serialization.arrays.*;

public class Main {
    private static String filePath = "./data.rain";

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

    static void colorStuff() {

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

        short end = SerializationReader.readShort(data, beginPointer);
        System.out.println("my gage: " + end);
        printBytes(data);
    }

    static void serializationTest() {
        RCDatabase database = new RCDatabase("DB");
        System.out.println("Size : " + database.getSize());
        RCObject obj = new RCObject("Entity");
        RCField field = new IntField("def", Integer.MAX_VALUE - 1);
        RCField positionX = new ShortField("X", (short) 20);
        RCField positionY = new ShortField("Y", (short) 9);
        RCString str = new RCString("names", "Léo,Nadia");
        obj.addField(field);
        obj.addField(positionX);
        obj.addField(positionY);
        obj.addString(str);

        var test = new long[] { -14, 2, -3, 12 };
        LongArray array = new LongArray("abc", test);
        var values = array.getValues();
        obj.addArray(array);

        System.out.println("Obj Size : " + obj.getSize());

        //database.addObject(obj);
        database.addObject(new RCObject("Nivisan"));
        System.out.println("Size : " + database.getSize());
        byte[] dataNew = new byte[database.getSize()];
        database.getBytes(dataNew, 0);
        //FileService.saveToFile(filePath, dataNew);
        FileService.saveOptimized(filePath, dataNew);

        printBytes(dataNew);

        IO.println("");

        IO.println("Written to file");
    }

    static void deserializationTest() {
        byte[] dbData = FileService.getFromFile(filePath);
        RCDatabase database = DbDeserializer.Deserialize(dbData);
        System.out.println(database.getName());

    }

    public static void main(String[] args) {
        System.out.println("");
        System.out.println("Data comes: ");

        serializationTest();

        deserializationTest();

        Sandbox sandbox = new Sandbox();
        sandbox.play();
    }
}
