package com.palenica.david;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
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

    public static double loss(final double label, final double prediction) {
        if (label < 0.0 || label > 1.0) {
            throw new IllegalArgumentException("Label outside if [0,1] interval: " + label);
        }
        if (prediction < 0.0 || prediction > 1.0) {
            throw new IllegalArgumentException("Prediction outside if [0,1] interval: " + prediction);
        }
        double loss = 0.0;
        if (prediction > 0.0) {
            loss -= label * Math.log(prediction);
        } else if (label > 0.0) {
            return Double.POSITIVE_INFINITY;
        }
        if (prediction < 1.0) {
            loss -= (1.0 - label) * Math.log(1.0 - prediction);
        } else if (label < 1.0) {
            return Double.POSITIVE_INFINITY;
        }
        return loss;
    }

    public static double loss(final double[] label, final double[] prediction) {
        double sum = 0.0;
        for (int i = 0; i < label.length; i++) {
            sum += loss(label[i], prediction[i]);
        }
        return sum;
    }

    public static double loss(final double[][] labels, final double[][] predictions) {
        double sum = 0.0;
        for (int i = 0; i < labels.length; i++) {
            sum += loss(labels[i], predictions[i]);
        }
        return sum;
    }

    public static void shuffle(byte[] data, final long seed) {
        Random random = new Random(seed);
        for (int i = data.length - 1; i > 0; i--) {
            final int index = random.nextInt(i + 1);

            // Swap data[index] and data[i]
            final byte tmp = data[index];
            data[index] = data[i];
            data[i] = tmp;
        }
    }

    public static void shuffle(Object[] data, final long seed) {
        Random random = new Random(seed);
        for (int i = data.length - 1; i > 0; i--) {
            final int index = random.nextInt(i + 1);

            // Swap data[index] and data[i]
            final Object tmp = data[index];
            data[index] = data[i];
            data[i] = tmp;
        }
    }


}
