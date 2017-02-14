package com.palenica.david;

import java.io.*;

public class GenerateImages {

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
        byte[][][] testImages = Utils.readImages("data/t10k-images-idx3-ubyte.gz");
        byte[] testLabels = Utils.readLabels("data/t10k-labels-idx1-ubyte.gz");
        saveImages(testImages, testLabels, "output/test/");

        byte[][][] trainImages = Utils.readImages("data/train-images-idx3-ubyte.gz");
        byte[] trainLabels = Utils.readLabels("data/train-labels-idx1-ubyte.gz");
        saveImages(trainImages, trainLabels, "output/train/");
    }
}
