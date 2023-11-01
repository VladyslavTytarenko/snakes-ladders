package org.walter.server.map;

import java.util.HashMap;
import java.util.Map;

public class SnakesAndLaddersMap {

    private static final Map<Integer, Integer> map = new HashMap<>();

    static {
        // Ladders
        map.put(1, 38);
        map.put(4, 14);
        map.put(8, 30);
        map.put(21, 42);
        map.put(28, 76);
        map.put(50, 67);
        map.put(71, 92);
        map.put(80, 99);

        // Snakes
        map.put(31, 10);
        map.put(36, 6);
        map.put(48, 26);
        map.put(62, 18);
        map.put(88, 24);
        map.put(95, 56);
        map.put(97, 78);
    }

    public static int getPosition(int position) {
        return map.getOrDefault(position, position);
    }
}
