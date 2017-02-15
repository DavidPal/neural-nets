package com.palenica.david;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

public class NeuralNetwork {

    final public static int inputSize = 28 * 28;
    final public static int outputSize = 10;

    public int sizeHiddenLayer;

    public double[][] firstWeights;
    public double[] firstBias;

    public double[][] secondWeights;
    public double[] secondBias;

    public static class Gradient {
        final public double[][] firstWeights;
        final public double[] firstBias;

        final public double[][] secondWeights;
        final public double[] secondBias;

        private Gradient(final double[][] firstWeights, final double[] firstBias,
                         final double[][] secondWeights, final double[] secondBias) {
            this.firstWeights = firstWeights;
            this.firstBias = firstBias;
            this.secondWeights = secondWeights;
            this.secondBias = secondBias;
        }

        private Gradient(final int sizeHiddenLayer) {
            this(new double[inputSize][sizeHiddenLayer], new double[sizeHiddenLayer],
                    new double[sizeHiddenLayer][outputSize], new double[outputSize]);
        }

        private void add(final Gradient other) {
            MatrixUtils.addTo(firstWeights, other.firstWeights);
            MatrixUtils.addTo(firstBias, other.firstBias);

            MatrixUtils.addTo(secondWeights, other.secondWeights);
            MatrixUtils.addTo(secondBias, other.secondBias);
        }
    }

    public NeuralNetwork(final int sizeHiddenLayer) {
        this.sizeHiddenLayer = sizeHiddenLayer;
        this.firstWeights = new double[sizeHiddenLayer][inputSize];
        this.firstBias = new double[sizeHiddenLayer];
        this.secondWeights = new double[outputSize][sizeHiddenLayer];
        this.secondBias = new double[outputSize];
    }

    public void InitializeParameters(final long seed) {
        Random random = new Random(seed);
        for (int i = 0; i < sizeHiddenLayer; i++) {
            for (int j = 0; j < inputSize; j++) {
                firstWeights[i][j] = random.nextGaussian() / Math.sqrt(inputSize);
            }
        }

        for (int i = 0; i < sizeHiddenLayer; i++) {
            firstBias[i] = random.nextGaussian();
        }

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < sizeHiddenLayer; j++) {
                secondWeights[i][j] = random.nextGaussian() / Math.sqrt(sizeHiddenLayer);
            }
        }

        for (int i = 0; i < outputSize; i++) {
            secondBias[i] = random.nextGaussian();
        }
    }

    public void saveToFile(final String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(fileName, "UTF-8");
        writer.println(sizeHiddenLayer);

        for (int i = 0; i < sizeHiddenLayer; i++) {
            for (int j = 0; j < inputSize; j++) {
                writer.print(String.format(java.util.Locale.US, "%15.9f ", firstWeights[i][j]));
            }
            writer.println();
        }
        writer.println();

        for (int k = 0; k < sizeHiddenLayer; k++) {
            writer.print(String.format(java.util.Locale.US, "%15.9f ", firstBias[k]));
        }
        writer.println();
        writer.println();

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < sizeHiddenLayer; j++) {
                writer.print(String.format(java.util.Locale.US, "%15.9f ", secondWeights[i][j]));
            }
            writer.println();
        }
        writer.println();


        for (int j = 0; j < outputSize; j++) {
            writer.print(String.format(java.util.Locale.US, "%15.9f ", secondBias[j]));
        }
        writer.println();
        writer.println();
        writer.close();
    }

    public void loadFromFile(final String fileName) throws IOException {
        Path filePath = Paths.get(fileName);
        Scanner scanner = new Scanner(filePath);

        this.sizeHiddenLayer = scanner.nextInt();

        for (int i = 0; i < sizeHiddenLayer ; i++) {
            for (int j = 0; j < inputSize; j++) {
                firstWeights[i][j] = scanner.nextDouble();
            }
        }

        for (int k = 0; k < sizeHiddenLayer; k++) {
            firstBias[k] = scanner.nextDouble();
        }

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < sizeHiddenLayer; j++) {
                secondWeights[i][j] = scanner.nextDouble();
            }
        }

        for (int j = 0; j < outputSize; j++) {
            secondBias[j] = scanner.nextDouble();
        }
    }

    public double[] predict(final double[] input) {
        double[] result1 = MatrixUtils.multiply(firstWeights, input);
        MatrixUtils.addTo(result1, firstBias);
        MatrixUtils.applySigmoid(result1);

        double[] result2 = MatrixUtils.multiply(secondWeights, input);
        MatrixUtils.addTo(result2, secondBias);
        MatrixUtils.applySigmoid(result2);

        return result2;
    }

    public Gradient getGradient(final double[][] image, final double L2regularization) {
        return new Gradient(sizeHiddenLayer);
    }

}
