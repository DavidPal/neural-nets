package com.palenica.david;

import java.io.IOException;

public class TrainModel {

    final public static int sizeHiddenLayer = 30;

    final public static double L2regularization = 0.00005;

    final public static int miniBatchSize = 10;
    final public static int numPasses = 200;

    public static void main(String[] args) throws IOException {

        byte[][][] trainImages = new byte[50000][][];
        byte[] trainLabels = new byte[50000];

        byte[][][] testImages = new byte[10000][][];
        byte[] testLabels = new byte[10000];

        {
            byte[][][] images = Utils.readImages("data/train-images-idx3-ubyte.gz");
            byte[] labels = Utils.readLabels("data/train-labels-idx1-ubyte.gz");

            Utils.shuffle(images, 123456789L);
            Utils.shuffle(labels, 123456789L);


            for (int i = 0; i < 50000; i++) {
                trainImages[i] = images[i];
                trainLabels[i] = labels[i];
            }

            for (int i = 0; i < 10000; i++) {
                testImages[i] = images[i + 50000];
                testLabels[i] = labels[i + 50000];
            }
        }


        // byte[][][] testImages = Utils.readImages("data/t10k-images-idx3-ubyte.gz");
        // byte[] testLabels = Utils.readLabels("data/t10k-labels-idx1-ubyte.gz");

        final int N = trainImages.length;

        NeuralNetwork neuralNetwork;

        if (args.length >= 1) {
            System.out.println(String.format("Loading model from file %s ...", args[0]));
            neuralNetwork = NeuralNetwork.loadFromFile(args[0]);
        } else {
            neuralNetwork = new NeuralNetwork(sizeHiddenLayer);
            neuralNetwork.initializeParameters(47L);
        }

        for (int pass = 0; pass < numPasses; pass++) {

            // final double stepSize = 1.0 / Math.sqrt(1 + pass);
            // final double stepSize = 1.0 / (1 + pass);
            final double stepSize = 0.5;

            System.out.println(String.format("Pass %d, step size = %.10f", pass, stepSize));

            Utils.shuffle(trainImages, pass);
            Utils.shuffle(trainLabels, pass);

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

            neuralNetwork.saveToFile(String.format("model-%05d.txt", pass));
        }

    }

}
