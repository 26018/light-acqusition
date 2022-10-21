package top.fixyou.utils;

import java.util.HashMap;
import java.util.Map;

public class ComputeUtil {
    private static final Map<Integer, Integer> Rmap = new HashMap<>();
    private static final Map<Integer, Integer> Gmap = new HashMap<>();
    private static final Map<Integer, Integer> Bmap = new HashMap<>();

    private static int computeGrayValue(int R, int G, int B) {
        return (R * 30 + G * 59 + B * 11) / 100;
    }

    public static void addPixel(int r, int g, int b) {
        Rmap.put(r, Rmap.getOrDefault(r, 0) + 1);
        Gmap.put(g, Gmap.getOrDefault(g, 0) + 1);
        Bmap.put(b, Bmap.getOrDefault(b, 0) + 1);
    }

    private static int avg(Map<Integer, Integer> map) {
        int size = 0; int total = 0;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            Integer time = entry.getValue();
            if (time != 0) {
                total = total + entry.getKey() * time;
            }
            size += time;
        }
        return total / size;
    }

    private static RGB rgbAvg() {
        RGB rgb = new RGB(avg(Rmap), avg(Gmap), avg(Bmap));
        // 计算完就可以清空了
        clearMap();
        return rgb;
    }

    public static int grayValue() {
        RGB rgb = rgbAvg();
        return computeGrayValue(rgb.r, rgb.g, rgb.b);
    }

    private static void clearMap() {
        Rmap.clear();
        Gmap.clear();
        Bmap.clear();
    }

    public static class RGB {
        public int r;
        public int g;
        public int b;

        RGB(int r, int g, int b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }
}
