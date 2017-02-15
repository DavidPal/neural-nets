package com.palenica.david;

public class MatrixUtils {

    public static double sigmoid(final double z) {
        return 1.0 / (1.0 + Math.exp(-z));
    }

    public static void applySigmoid(double[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = sigmoid(data[i]);
        }
    }

    // Vector addition
    public static void addTo(final double[] a, final double[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i] += b[i];
        }
    }

    // Vector addition
    public static double[] add(final double[] a, final double[] b) {
        double[] output = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            output[i] = a[i] + b[i];
        }
        return output;
    }

    // Matrix addition
    public static void addTo(final double[][] A, final double[][] B) {
        for (int i = 0; i < A.length; i++) {
            addTo(A[i], B[i]);
        }
    }

    // Matrix addition
    public static double[][] add(final double[][] A, final double[][] B) {
        double[][] output = new double[A.length][];
        for (int i = 0; i < A.length; i++) {
            output[i] = add(A[i], B[i]);
        }
        return output;
    }

    // Dot product
    public static double dot(final double[] a, final double[] b) {
        double output = 0.0;
        for (int i = 0; i < a.length; i++) {
            output += a[i] * b[i];
        }
        return output;
    }

    // Matrix-vector multiplication
    public static double[] multiply(final double[][] A, final double[] b) {
        double[] output = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            output[i] = dot(A[i], b);
        }
        return output;
    }

}
