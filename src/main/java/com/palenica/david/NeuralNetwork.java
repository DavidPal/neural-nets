package com.palenica.david;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class NeuralNetwork {

    final public static int inputSize = 28;
    final public static int outputSize = 10;

    final public int sizeHiddenLayer;

    public double[][][] firstWeights;
    public double[] firstBias;

    public double[][] secondWeights;
    public double[] secondBias;

    public NeuralNetwork(final int sizeHiddenLayer) {
        this.sizeHiddenLayer = sizeHiddenLayer;
        this.firstWeights = new double[inputSize][inputSize][sizeHiddenLayer];
        this.firstBias = new double[sizeHiddenLayer];
        this.secondWeights = new double[sizeHiddenLayer][outputSize];
        this.secondBias = new double[outputSize];
    }

    public void saveToFile(final String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println(sizeHiddenLayer);
        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                for (int k = 0; k < sizeHiddenLayer; k++) {
                    writer.print(String.format("%f ", firstWeights[i][j][k]));
                }
                writer.println();
            }
        }
        writer.println();

        for (int k = 0; k < sizeHiddenLayer; k++) {
            writer.print(String.format("%f ", firstBias[k]));
        }
        writer.println();
        writer.println();

        for (int i = 0; i < sizeHiddenLayer; i++) {
            for (int j = 0; j < outputSize; j++) {
                writer.print(String.format("%f ", secondWeights[i][j]));
            }
            writer.println();
        }
        writer.println();


        for (int j = 0; j < outputSize; j++) {
            writer.print(String.format("%f ", secondBias[j]));
        }
        writer.println();
        writer.println();
        writer.close();
    }

    public void loadFromFile(final String fileName) throws IOException {
        Path filePath = Paths.get(fileName);
        Scanner scanner = new Scanner(filePath);

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                for (int k = 0; k < sizeHiddenLayer; k++) {
                    firstWeights[i][j][k] = scanner.nextDouble();
                }
            }
        }

        for (int k = 0; k < sizeHiddenLayer; k++) {
            firstBias[k] = scanner.nextDouble();
        }

        for (int i = 0; i < sizeHiddenLayer; i++) {
            for (int j = 0; j < outputSize; j++) {
                secondWeights[i][j] = scanner.nextDouble();
            }
        }

        for (int j = 0; j < outputSize; j++) {
            secondBias[j] = scanner.nextDouble();
        }
    }

    public double[] predict(final double[][] image) {
        final double[] hiddenLayer = predictHiddenLayer(image);
        final double[] predictions = predictSecondLayer(hiddenLayer);
        return predictions;
    }

    private double[] predictHiddenLayer(final double[][] image) {
        final double[] result = new double[sizeHiddenLayer];

        for (int k = 0; k < sizeHiddenLayer; k++) {
            result[k] = firstBias[k];
        }

        for (int i = 0; i < inputSize; i++) {
            for (int j = 0; j < inputSize; j++) {
                for (int k = 0; k < sizeHiddenLayer; k++) {
                    result[k] += image[i][j] * firstWeights[i][j][k];
                }
            }
        }

        return sigmoid(result);
    }

    private double[] predictSecondLayer(final double[] hiddenLayer) {
        final double[] result = new double[outputSize];

        for (int k = 0; k < outputSize; k++) {
            result[k] = secondBias[k];
        }

        for (int i = 0; i < sizeHiddenLayer; i++) {
            for (int j = 0; j < outputSize; j++) {
                result[j] += hiddenLayer[i] * secondWeights[i][j];
            }
        }
        return sigmoid(result);
    }

    public static double sigmoid(final double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    public static double[] sigmoid(final double[] input) {
        final double[] output = new double[input.length];
        for (int i = 0; i < input.length; i++) {
            output[i] = sigmoid(input[i]);
        }
        return output;
    }

}
