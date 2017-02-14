package com.palenica.david;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class SaveModel {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        NeuralNetwork neuralNetwork = new NeuralNetwork(15);
        neuralNetwork.saveToFile("model.txt");

        byte[][][] trainImages = Utils.readImages("data/train-images-idx3-ubyte.gz");
        byte[] trainLabels = Utils.readLabels("data/train-labels-idx1-ubyte.gz");

        double[] output = neuralNetwork.predict(Utils.preprocessImage(trainImages[0]));
        for (double v : output) {
            System.out.print(v + " ");
        }
        System.out.println();
    }

}
