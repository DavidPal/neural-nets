package com.palenica.david;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class GenerateImages {

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

    public static void saveImages(final byte[][][] images, final byte[] labels, final String directory) throws FileNotFoundException, UnsupportedEncodingException {
        final int counts[] = new int[10];

        for (int i = 0; i < images.length; i++) {
            final String fileName = String.format("%s/digit-%d-example-%04d", directory, labels[i], counts[labels[i]]);
            counts[labels[i]]++;
            System.out.println(String.format("Writing file '%s' ...", fileName));
            PortableGrayMapFormat.saveAsPGM(fileName, images[i]);
        }
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        byte[][][] testImages = readImages("data/t10k-images-idx3-ubyte.gz");
        byte[] testLabels = readLabels("data/t10k-labels-idx1-ubyte.gz");
        saveImages(testImages, testLabels, "output/test/");

        byte[][][] trainImages = readImages("data/train-images-idx3-ubyte.gz");
        byte[] trainLabels = readLabels("data/train-labels-idx1-ubyte.gz");
        saveImages(trainImages, trainLabels, "output/train/");
    }
}
