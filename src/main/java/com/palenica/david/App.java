package com.palenica.david;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class App {

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

    public static void main(String[] args) {
        byte[][][] images = readImages("data/t10k-images-idx3-ubyte.gz");
        if (images == null) {
            System.out.println(images);
        } else {
            System.out.println("Length = " + images.length);
        }

        byte[] labels = readLabels("data/t10k-labels-idx1-ubyte.gz");
        int counts[] = new int[10];
        if (labels == null) {
            System.out.println(labels);
        } else {
            System.out.println("Length = " + labels.length);
            for (int i = 0; i < labels.length; i++) {
                // System.out.println(labels[i]);
                counts[labels[i]]++;
            }
            for (int i = 0; i < counts.length; i++) {
                System.out.println(i + " : " + counts[i]);
            }
        }

    }
}
