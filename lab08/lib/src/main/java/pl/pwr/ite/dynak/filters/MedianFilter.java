package pl.pwr.ite.dynak.filters;

import java.util.Arrays;

public class MedianFilter {
    static {
        System.loadLibrary("median");
    }

    public native int[] applyFilterNative(int[] source, int numPasses, int width, int height, int radius);

    public int[] applyFilter(int[] source, int numPasses, int width, int height, int radius) {
        int[] target = new int[source.length];
        System.arraycopy(source, 0, target, 0, source.length);

        for (int pass = 0; pass < numPasses; pass++) {
            int[] temp = new int[target.length];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    temp[y * width + x] = applyMedianToPixel(target, x, y, width, height, radius);
                }
            }

            System.arraycopy(temp, 0, target, 0, temp.length);
        }

        return target;
    }

    private int applyMedianToPixel(int[] source, int x, int y, int width, int height, int radius) {
        int[] neighborhood = new int[(2 * radius + 1) * (2 * radius + 1)];
        int count = 0;

        for (int dy = -radius; dy <= radius; dy++) {
            for (int dx = -radius; dx <= radius; dx++) {
                int px = Math.min(Math.max(x + dx, 0), width - 1);
                int py = Math.min(Math.max(y + dy, 0), height - 1);

                neighborhood[count++] = source[py * width + px];
            }
        }

        Arrays.sort(neighborhood);

        return neighborhood[neighborhood.length / 2];
    }
}
