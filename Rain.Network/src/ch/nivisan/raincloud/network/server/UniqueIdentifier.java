package ch.nivisan.raincloud.network.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueIdentifier {
    private static final List<Integer> ids = new ArrayList<>();
    private static final int range = 10000;

    private static int index = 0;

    static {
        for (int i = 0; i < range; i++) {
            ids.add(i);
        }
        Collections.shuffle(ids);
    }

    private UniqueIdentifier() {
    }

    public static int getIdentifier() {
        if (index > ids.size() - 1)
            index = 0;
        return ids.get(index++);
    }
}
