package com.palenica.david;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class Utils {

    public static byte[][][] readImages(String filename) {
        byte[][][] output = null;
        try {
            InputStream fileStream = new FileInputStream(filename);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            output = IdxFormatDecoder.decodeBytes3D(gzipStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return output;
    }

    public static byte[] readLabels(String filename) {
        byte[] output = null;
        try {
            InputStream fileStream = new FileInputStream(filename);
            InputStream gzipStream = new GZIPInputStream(fileStream);
            output = IdxFormatDecoder.decodeBytes1D(gzipStream);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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

}
