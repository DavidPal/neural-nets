package com.palenica.david;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class Utils {

    public static byte[][][] readImages(String filename) {
        System.out.println(String.format("Loading images from '%s'...", filename));
        byte[][][] output = null;
        try {
            InputStream fileStream = new FileInputStream(filename);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            output = IdxFormatDecoder.decodeBytes3D(gzipStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Done");
        return output;
    }

    public static byte[] readLabels(String filename) {
        System.out.println(String.format("Loading labels from '%s'...", filename));
        byte[] output = null;
        try {
            InputStream fileStream = new FileInputStream(filename);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            output = IdxFormatDecoder.decodeBytes1D(gzipStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        System.out.println("Done");
        return output;
    }

    public static double[] preprocessImage(final byte[][] image) {
        int totalSize = 0;
        for (int i = 0; i < image.length; i++) {
            totalSize += image[i].length;
        }

        double[] output = new double[totalSize];
        int count = 0;
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image.length; j++) {
                output[count] = (double)image[i][j] / 255.0;
                count++;
            }
        }
        return output;
    }

    public static double[] preprocessLabel(final int label) {
        double[] output = new double[10];
        output[label] = 1.0;
        return output;
    }

    public static int decodeLabel(final double[] label) {
        int best = 0;
        for (int i = 0; i < 10; i++) {
            if (label[i] > label[best]) {
                best = i;
            }
        }
        return best;
    }

}
