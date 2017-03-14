package com.palenica.david;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TestModel {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        byte[][][] testImages = Utils.readImages("data/t10k-images-idx3-ubyte.gz");
        byte[] testLabels = Utils.readLabels("data/t10k-labels-idx1-ubyte.gz");

        final int N = testImages.length;
        NeuralNetwork neuralNetwork = NeuralNetwork.loadFromFile("model.txt");
        Utils.printStats("Test: ", neuralNetwork, testImages, testLabels);
    }

}
