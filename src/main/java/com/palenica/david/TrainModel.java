package com.palenica.david;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class TrainModel {

    final public static int sizeHiddenLayer = 15;

    final public static double stepSize = 0.01;
    final public static double L2regularization = 0.0;

    final public static int miniBatchSize = 20;
    final public static int numPasses = 1;

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
        byte[][][] trainImages = Utils.readImages("data/train-images-idx3-ubyte.gz");
        byte[] trainLabels = Utils.readLabels("data/train-labels-idx1-ubyte.gz");
        byte[][][] testImages = Utils.readImages("data/t10k-images-idx3-ubyte.gz");
        byte[] testLabels = Utils.readLabels("data/t10k-labels-idx1-ubyte.gz");

        final int N = trainImages.length;
        // final int N = 10;

        Utils.shuffle(trainImages, 47L);
        Utils.shuffle(trainLabels, 47L);

        NeuralNetwork neuralNetwork = new NeuralNetwork(sizeHiddenLayer);
        neuralNetwork.initializeParameters(47L);

        for (int k = 0; k < numPasses; k++) {
            for (int i = 0; i < N / miniBatchSize; i++) {
                // if (i % 500 == 0) {
                System.out.println();
                System.out.println(String.format("Pass %d, minibatch %d ...", k, i));
                // }
                double[][] batchImages = new double[miniBatchSize][];
                double[][] batchLabels = new double[miniBatchSize][];

                int errors = 0;
                for (int j = 0; j < miniBatchSize; j++) {
                    batchImages[j] = Utils.preprocessImage(trainImages[miniBatchSize * i + j]);
                    final int correctLabel = trainLabels[miniBatchSize * i + j];
                    batchLabels[j] = Utils.preprocessLabel(correctLabel);
                    final int predictedLabel = neuralNetwork.predictHardLabel(batchImages[j]);
                    if (predictedLabel != correctLabel) {
                        errors++;
                    }
                }

                System.out.println(String.format("Mini batch error %d", errors));

                double[][] batchPredictions1 = neuralNetwork.predictBatch(batchImages);
                double loss1 = Utils.loss(batchLabels, batchPredictions1);
                System.out.println(String.format("loss = %.10f", loss1));


                neuralNetwork.updateParameters(batchImages, batchLabels, stepSize, L2regularization);


                double[][] batchPredictions2 = neuralNetwork.predictBatch(batchImages);
                double loss2 = Utils.loss(batchLabels, batchPredictions2);
                System.out.println(String.format("loss = %.10f", loss2));
                System.out.println(String.format("delta loss = %.10f", loss1 - loss2));

                //if (i % 10 == 0) {
                    // final int errors = TestModel.testModel(neuralNetwork, testImages, testLabels);
                    // System.out.println(String.format("Error = %d,  %.2f%%", errors, 100.0 * errors / testImages.length));
                //}
            }

        }

        neuralNetwork.saveToFile("model.txt");
    }

}
