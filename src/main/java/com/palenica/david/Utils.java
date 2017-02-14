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

    public static double[][] preprocessImage(final byte[][] image) {
        double[][] output = new double[image.length][];
        for (int i = 0; i < image.length; i++) {
            output[i] = new double[image[i].length];
            for (int j = 0; j < image.length; j++) {
                output[i][j] = image[i][j] / 255;
            }
        }
        return output;
    }

}
