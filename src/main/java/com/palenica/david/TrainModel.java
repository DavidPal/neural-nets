package com.palenica.david;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class TrainModel {

    final public static int sizeHiddenLayer = 30;

    final public static double stepSize = 0.1;
    final public static double L2regularization = 0.0001;

    final public static int miniBatchSize = 20;
    final public static int numPasses = 200;


    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        byte[][][] trainImages = Utils.readImages("data/train-images-idx3-ubyte.gz");
        byte[] trainLabels = Utils.readLabels("data/train-labels-idx1-ubyte.gz");
        byte[][][] testImages = Utils.readImages("data/t10k-images-idx3-ubyte.gz");
        byte[] testLabels = Utils.readLabels("data/t10k-labels-idx1-ubyte.gz");

        final int N = trainImages.length;

        NeuralNetwork neuralNetwork = new NeuralNetwork(sizeHiddenLayer);
        neuralNetwork.initializeParameters(47L);

        for (int k = 0; k < numPasses; k++) {
            System.out.println(String.format("Pass %d", k));

            Utils.shuffle(trainImages, k);
            Utils.shuffle(trainLabels, k);

            for (int i = 0; i < N / miniBatchSize; i++) {

                if (i % 500 == 0) {
                    System.out.println(String.format("Mini batch %d", i));
                }

                double[][] batchImages = new double[miniBatchSize][];
                double[][] batchLabels = new double[miniBatchSize][];

                for (int j = 0; j < miniBatchSize; j++) {
                    batchImages[j] = Utils.preprocessImage(trainImages[miniBatchSize * i + j]);
                    batchLabels[j] = Utils.preprocessLabel(trainLabels[miniBatchSize * i + j]);
                }

                neuralNetwork.updateParameters(batchImages, batchLabels, stepSize, L2regularization);
            }

            Utils.printStats("Train:", neuralNetwork, trainImages, trainLabels);
            Utils.printStats("Test: ", neuralNetwork, testImages, testLabels);
        }

        neuralNetwork.saveToFile("model.txt");
    }

}
