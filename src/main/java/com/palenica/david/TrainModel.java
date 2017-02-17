package com.palenica.david;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class TrainModel {

    final public static int sizeHiddenLayer = 15;

    final public static double stepSize = 1.0;
    final public static double L2regularization = 0.0;

    final public static int miniBatchSize = 10;
    final public static int numPasses = 100;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        byte[][][] trainImages = Utils.readImages("data/train-images-idx3-ubyte.gz");
        byte[] trainLabels = Utils.readLabels("data/train-labels-idx1-ubyte.gz");
        byte[][][] testImages = Utils.readImages("data/t10k-images-idx3-ubyte.gz");
        byte[] testLabels = Utils.readLabels("data/t10k-labels-idx1-ubyte.gz");

        final int N = trainImages.length;

        NeuralNetwork neuralNetwork = new NeuralNetwork(sizeHiddenLayer);
        neuralNetwork.initializeParameters(47L);


        for (int k = 0; k < numPasses; k++) {
            for (int i = 0; i < N / miniBatchSize; i++) {
                if (i % 500 == 0) {
                    System.out.println(String.format("Pass %d, minibatch %d ...", k, i));
                }
                double[][] batchImages = new double[miniBatchSize][];
                double[][] batchLabels = new double[miniBatchSize][];

                for (int j = 0; j < miniBatchSize; j++) {
                    batchImages[j] = Utils.preprocessImage(trainImages[miniBatchSize * i + j]);
                    batchLabels[j] = Utils.preprocessLabel(trainLabels[miniBatchSize * i + j]);
                }

                neuralNetwork.updateParameters(batchImages, batchLabels, stepSize, L2regularization);
            }

            final int errors = TestModel.testModel(neuralNetwork, testImages, testLabels);
            System.out.println(String.format("Error = %d,  %.2f%%", errors, 100.0 * errors / testImages.length));
        }

        neuralNetwork.saveToFile("model.txt");
    }

}
