package pl.pwr.ite.dynak.filters;

public class GaussianFilter {
    static {
        System.loadLibrary("gaussian");
    }

    public native int[] applyFilterNative(int[] source, int numPasses, int width, int height, int radius);

    private static void horizontalBoxBlur(int[] source, int[] target, int width, int height, int radius) {
        double scale = 1.0 / (radius * 2 + 1);
        for (int y = 0; y < height; y++) {
            int windowSum = 0;
            int offset = y * width;
            for (int x = -radius; x <= radius; x++) {
                int safeX = Math.min(Math.max(x, 0), width - 1);
                windowSum += source[offset + safeX];
            }
            for (int x = 0; x < width; x++) {
                target[offset + x] = (int) Math.round(windowSum * scale);
                int leftX = Math.max(x - radius, 0);
                int rightX = Math.min(x + radius + 1, width - 1);
                windowSum -= source[offset + leftX];
                windowSum += source[offset + rightX];
            }
        }
    }

    private static void verticalBoxBlur(int[] source, int[] target, int width, int height, int radius) {
        double scale = 1.0 / (radius * 2 + 1);
        for (int x = 0; x < width; x++) {
            int windowSum = 0;
            for (int y = -radius; y <= radius; y++) {
                int safeY = Math.min(Math.max(y, 0), height - 1);
                windowSum += source[safeY * width + x];
            }
            for (int y = 0; y < height; y++) {
                target[y * width + x] = (int) Math.round(windowSum * scale);
                int topY = Math.max(y - radius, 0);
                int bottomY = Math.min(y + radius + 1, height - 1);
                windowSum -= source[topY * width + x];
                windowSum += source[bottomY * width + x];
            }
        }
    }

    public int[] applyFilter(int[] source, int numPasses, int width, int height, int radius) {
        int[] target = new int[source.length];
        int[] temp = new int[source.length];
        System.arraycopy(source, 0, target, 0, source.length);
        for (int i = 0; i < numPasses; i++) {
            horizontalBoxBlur(target, temp, width, height, radius);
            verticalBoxBlur(temp, target, width, height, radius);
        }
        return target;
    }
}
