package pl.pwr.ite.dynak.utils;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import pl.pwr.ite.dynak.filters.GaussianFilter;
import pl.pwr.ite.dynak.filters.MedianFilter;

import javax.print.attribute.standard.Media;

public class ImageFilterer {
    public Image applyFilterToImage(Image input, int numPasses, int radius, String filterName) {
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();

        PixelReader reader = input.getPixelReader();

        int[] a = new int[width * height];
        int[] r = new int[width * height];
        int[] g = new int[width * height];
        int[] b = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);

                int i = y * width + x;
                a[i] = (argb >> 24) & 0xff;
                r[i] = (argb >> 16) & 0xff;
                g[i] = (argb >> 8) & 0xff;
                b[i] = argb & 0xff;
            }
        }


        if (filterName.equals("Gaussian")) {
            GaussianFilter gaussianFilter = new GaussianFilter();
            r = gaussianFilter.applyFilter(r, numPasses, width, height, radius);
            g = gaussianFilter.applyFilter(g, numPasses, width, height, radius);
            b = gaussianFilter.applyFilter(b, numPasses, width, height, radius);
        }
        else if (filterName.equals("Median")) {
            MedianFilter medianFilter = new MedianFilter();
            r = medianFilter.applyFilter(r, numPasses, width, height, radius);
            g = medianFilter.applyFilter(g, numPasses, width, height, radius);
            b = medianFilter.applyFilter(b, numPasses, width, height, radius);
        }


        WritableImage output = new WritableImage(width, height);
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;

                int argb = (a[i] << 24) |
                        (r[i] << 16) |
                        (g[i] << 8)  |
                        b[i];

                writer.setArgb(x, y, argb);
            }
        }

        return output;
    }
    public Image applyFilterToImageNative(Image input, int numPasses, int radius, String filterName) {
        int width = (int) input.getWidth();
        int height = (int) input.getHeight();

        PixelReader reader = input.getPixelReader();

        int[] a = new int[width * height];
        int[] r = new int[width * height];
        int[] g = new int[width * height];
        int[] b = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = reader.getArgb(x, y);

                int i = y * width + x;
                a[i] = (argb >> 24) & 0xff;
                r[i] = (argb >> 16) & 0xff;
                g[i] = (argb >> 8) & 0xff;
                b[i] = argb & 0xff;
            }
        }


        if (filterName.equals("Gaussian")) {
            GaussianFilter gaussianFilter = new GaussianFilter();
            r = gaussianFilter.applyFilterNative(r, numPasses, width, height, radius);
            g = gaussianFilter.applyFilterNative(g, numPasses, width, height, radius);
            b = gaussianFilter.applyFilterNative(b, numPasses, width, height, radius);
        }
        else if (filterName.equals("Median")) {
            MedianFilter medianFilter = new MedianFilter();
            r = medianFilter.applyFilterNative(r, numPasses, width, height, radius);
            g = medianFilter.applyFilterNative(g, numPasses, width, height, radius);
            b = medianFilter.applyFilterNative(b, numPasses, width, height, radius);
        }


        WritableImage output = new WritableImage(width, height);
        PixelWriter writer = output.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int i = y * width + x;

                int argb = (a[i] << 24) |
                        (r[i] << 16) |
                        (g[i] << 8)  |
                        b[i];

                writer.setArgb(x, y, argb);
            }
        }

        return output;
    }
}
