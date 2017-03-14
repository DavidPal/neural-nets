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

    private Parameters parameters;

    public static class Parameters {
        final public double[][] firstWeights;
        final public double[] firstBias;
        final public double[][] secondWeights;
        final public double[] secondBias;

        private Parameters(final double[][] firstWeights, final double[] firstBias,
                           final double[][] secondWeights, final double[] secondBias) {
            this.firstWeights = firstWeights;
            this.firstBias = firstBias;
            this.secondWeights = secondWeights;
            this.secondBias = secondBias;
        }

        private Parameters(final int sizeHiddenLayer) {
            this(new double[sizeHiddenLayer][inputSize],
                 new double[sizeHiddenLayer],
                 new double[outputSize][sizeHiddenLayer],
                 new double[outputSize]);
        }

        public static Parameters createDeepCopy(final Parameters parameters) {
            final int sizeHiddenLayer = parameters.firstBias.length;
            final double[][] firstWeights = new double[sizeHiddenLayer][inputSize];
            final double[] firstBias = new double[sizeHiddenLayer];
            final double[][] secondWeights = new double[outputSize][sizeHiddenLayer];
            final double[] secondBias = new double[outputSize];

            for (int i = 0; i < sizeHiddenLayer ; i++) {
                for (int j = 0; j < inputSize; j++) {
                    firstWeights[i][j] = parameters.firstWeights[i][j];
                }
            }

            for (int i = 0; i < sizeHiddenLayer; i++) {
                firstBias[i] = parameters.firstBias[i];
            }

            for (int i = 0; i < outputSize; i++) {
                for (int j = 0; j < sizeHiddenLayer; j++) {
                    secondWeights[i][j] = parameters.secondWeights[i][j];
                }
            }

            for (int i = 0; i < outputSize; i++) {
                secondBias[i] = parameters.secondBias[i];
            }

            return new Parameters(firstWeights, firstBias, secondWeights, secondBias);
        }

        public static Parameters loadParametersFromFile(final String fileName) throws IOException {
            Path filePath = Paths.get(fileName);
            Scanner scanner = new Scanner(filePath);

            final int sizeHiddenLayer = scanner.nextInt();

            final double[][] firstWeights = new double[sizeHiddenLayer][inputSize];
            final double[] firstBias = new double[sizeHiddenLayer];
            final double[][] secondWeights = new double[outputSize][sizeHiddenLayer];
            final double[] secondBias = new double[outputSize];

            for (int i = 0; i < sizeHiddenLayer ; i++) {
                for (int j = 0; j < inputSize; j++) {
                    firstWeights[i][j] = scanner.nextDouble();
                }
            }

            for (int i = 0; i < sizeHiddenLayer; i++) {
                firstBias[i] = scanner.nextDouble();
            }

            for (int i = 0; i < outputSize; i++) {
                for (int j = 0; j < sizeHiddenLayer; j++) {
                    secondWeights[i][j] = scanner.nextDouble();
                }
            }

            for (int i = 0; i < outputSize; i++) {
                secondBias[i] = scanner.nextDouble();
            }

            return new Parameters(firstWeights, firstBias, secondWeights, secondBias);
        }

        private void add(final Parameters other) {
            MatrixUtils.addTo(firstWeights, other.firstWeights);
            MatrixUtils.addTo(firstBias, other.firstBias);
            MatrixUtils.addTo(secondWeights, other.secondWeights);
            MatrixUtils.addTo(secondBias, other.secondBias);
        }

        public void saveToFile(final String fileName) throws FileNotFoundException, UnsupportedEncodingException {
            final int sizeHiddenLayer = firstBias.length;
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
    }

    public NeuralNetwork(final int sizeHiddenLayer) {
        this.sizeHiddenLayer = sizeHiddenLayer;
        this.parameters = new Parameters(sizeHiddenLayer);
    }

    private NeuralNetwork(final int sizeHiddenLayer, Parameters parameters) {
        this.sizeHiddenLayer = sizeHiddenLayer;
        this.parameters = parameters;
    }

    public static NeuralNetwork loadFromFile(final String fileName) throws IOException {
        Parameters parameters = Parameters.loadParametersFromFile(fileName);
        return new NeuralNetwork(parameters.firstBias.length, parameters);
    }

    public void saveToFile(final String fileName) throws FileNotFoundException, UnsupportedEncodingException {
        parameters.saveToFile(fileName);
    }

    private double[] computeHidden(final double[] input) {
        final double[] hidden = MatrixUtils.multiply(parameters.firstWeights, input);
        MatrixUtils.addTo(hidden, parameters.firstBias);
        MatrixUtils.applySigmoid(hidden);
        return hidden;
    }

    private double[] computeOutput(final double[] hidden) {
        final double[] output = MatrixUtils.multiply(parameters.secondWeights, hidden);
        MatrixUtils.addTo(output, parameters.secondBias);
        MatrixUtils.applySigmoid(output);
        return output;
    }

    public double[] predict(final double[] input) {
        double[] hidden = computeHidden(input);
        return computeOutput(hidden);
    }

    public int predictHardLabel(final double[] input) {
        double[] prediction = predict(input);
        return Utils.decodeLabel(prediction);
    }

    public double[][] predictBatch(final double[][] inputs) {
        double[][] predictions = new double[inputs.length][];
        for (int i = 0; i < inputs.length; i++) {
            double[] hidden = computeHidden(inputs[i]);
            predictions[i] = computeOutput(hidden);
        }
        return predictions;
    }

    public void initializeParameters(final long seed) {
        Random random = new Random(seed);
        for (int i = 0; i < sizeHiddenLayer; i++) {
            for (int j = 0; j < inputSize; j++) {
                parameters.firstWeights[i][j] = random.nextGaussian() / Math.sqrt(inputSize);
            }
        }

        for (int i = 0; i < sizeHiddenLayer; i++) {
            parameters.firstBias[i] = random.nextGaussian();
        }

        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < sizeHiddenLayer; j++) {
                parameters.secondWeights[i][j] = random.nextGaussian() / Math.sqrt(sizeHiddenLayer);
            }
        }

        for (int i = 0; i < outputSize; i++) {
            parameters.secondBias[i] = random.nextGaussian();
        }
    }

    // See documentation in "doc/" directory.
    public Parameters getGradient(final double[] input, final double[] label) {
        final double[] activation = MatrixUtils.multiply(parameters.firstWeights, input);
        MatrixUtils.addTo(activation, parameters.firstBias);
        final double[] hidden = MatrixUtils.sigmoid(activation);
        double[] output = computeOutput(hidden);

        // final double loss = Utils.loss(label, output);
        // System.out.println(String.format("loss = %f", loss));

        // This overwrites up both output and label. Beware!
        final double[] negativeLabel = MatrixUtils.copy(label);
        MatrixUtils.multiply(-1.0, negativeLabel);
        final double[] error = MatrixUtils.add(output, negativeLabel);
        final double[] gradientSecondBias = error;

        double[][] gradientSecondWeights = new double[outputSize][sizeHiddenLayer];
        for (int i = 0; i < outputSize; i++) {
            for (int j = 0; j < sizeHiddenLayer; j++) {
                gradientSecondWeights[i][j] = error[i] * hidden[j];
            }
        }

        MatrixUtils.applySigmaPrime(activation);
        double[] hiddenError = MatrixUtils.multiply(error, parameters.secondWeights);
        double[] gradientFirstBias = MatrixUtils.componentMultiply(hiddenError, activation);

        double[][] gradientFirstWeights = new double[sizeHiddenLayer][inputSize];
        for (int i = 0; i < sizeHiddenLayer; i++) {
            for (int j = 0; j < inputSize; j++) {
                gradientFirstWeights[i][j] = gradientFirstBias[i] * input[j];
            }
        }

        // double[] gradientFirstBias = new double[sizeHiddenLayer];
        // double[][] gradientFirstWeights = new double[sizeHiddenLayer][inputSize];

        return new Parameters(gradientFirstWeights, gradientFirstBias, gradientSecondWeights, gradientSecondBias);
    }

    public void updateParameters(final double[][] inputs, final double[][] labels, final double stepSize, final double L2regularization) {
        Parameters updatedParameters = Parameters.createDeepCopy(this.parameters);

        final double[][] firstWeights = new double[sizeHiddenLayer][inputSize];
        final double[] firstBias = new double[sizeHiddenLayer];
        final double[][] secondWeights = new double[outputSize][sizeHiddenLayer];
        final double[] secondBias = new double[outputSize];

        MatrixUtils.addTo(firstWeights, -L2regularization);
        MatrixUtils.addTo(firstBias, -L2regularization);
        MatrixUtils.addTo(secondWeights, -L2regularization);
        MatrixUtils.addTo(secondBias, -L2regularization);

        updatedParameters.add(new Parameters(firstWeights, firstBias, secondWeights, secondBias));

        for (int i = 0; i < inputs.length; i++) {
            Parameters gradient = getGradient(inputs[i], labels[i]);
            try {
                gradient.saveToFile("gradient.txt");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            MatrixUtils.multiplyBy(-stepSize, gradient.firstWeights);
            MatrixUtils.multiply(-stepSize, gradient.firstBias);
            MatrixUtils.multiplyBy(-stepSize, gradient.secondWeights);
            MatrixUtils.multiply(-stepSize, gradient.secondBias);
            updatedParameters.add(gradient);
        }

        this.parameters = updatedParameters;
    }

}
