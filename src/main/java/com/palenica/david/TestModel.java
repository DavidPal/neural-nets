package com.palenica.david;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TestModel {

    public static int testModel(final NeuralNetwork neuralNetwork, byte[][][] testImages, byte[] testLabels) {
        int errors = 0;
        for (int i = 0; i < testImages.length; i++) {
            final double[] labelVector = neuralNetwork.predict(Utils.preprocessImage(testImages[i]));
            final int label = Utils.decodeLabel(labelVector);
            if (label != testLabels[i]) {
                errors++;
            }
        }
        return errors;
    }

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        byte[][][] testImages = Utils.readImages("data/t10k-images-idx3-ubyte.gz");
        byte[] testLabels = Utils.readLabels("data/t10k-labels-idx1-ubyte.gz");

        final int N = testImages.length;
        NeuralNetwork neuralNetwork = NeuralNetwork.loadFromFile("model.txt");
        final int errors = testModel(neuralNetwork, testImages, testLabels);

        System.out.println(String.format("Error = %d,  %.2f%%", errors, 100.0 * errors / N));
    }

}
