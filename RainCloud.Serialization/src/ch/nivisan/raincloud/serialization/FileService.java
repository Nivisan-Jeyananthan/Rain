package ch.nivisan.raincloud.serialization;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileService {

    public static void saveToFile(String path, byte[] data) {
        try {
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
            stream.write(data);
            stream.close();
        } catch (IOException e) {
            System.out.println("Not found file");
            e.printStackTrace();
        }
    }

    public static void saveOptimized(String path, byte[] data) {
        try (FileChannel rwChannel = new RandomAccessFile(path, "rw").getChannel()) {
            ByteBuffer wrBuf = rwChannel.map(FileChannel.MapMode.READ_WRITE, 0, data.length);
            wrBuf.put(data);
            rwChannel.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static byte[] getFromFile(String path) {
        byte[] buffer;
        try {
            BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
            buffer = new byte[stream.available()];
            stream.read(buffer);
            stream.close();
        } catch (IOException e) {
            System.out.println("Not found file");
            e.printStackTrace();
            return new byte[0];
        }
        return buffer;
    }

}
