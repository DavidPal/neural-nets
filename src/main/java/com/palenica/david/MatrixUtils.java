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

    public static double[] sigmoid(double[] data) {
        double output[] = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            output[i] = sigmoid(data[i]);
        }
        return output;
    }

    public static double sigmaPrime(final double z) {
        double s = sigmoid(z);
        return s * (1.0 - s);
    }

    public static void applySigmaPrime(double[] data) {
        for (int i = 0; i < data.length; i++) {
            data[i] = sigmaPrime(data[i]);
        }
    }

    // Vector addition
    public static void addTo(final double[] a, final double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vector sizes do not match.");
        }
        for (int i = 0; i < a.length; i++) {
            a[i] += b[i];
        }
    }

    // Vector addition
    public static void addTo(final double[] a, final double b) {
        for (int i = 0; i < a.length; i++) {
            a[i] += b;
        }
    }


    // Vector addition
    public static double[] add(final double[] a, final double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vector sizes do not match.");
        }
        double[] output = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            output[i] = a[i] + b[i];
        }
        return output;
    }

    // Matrix addition
    public static void addTo(final double[][] A, final double[][] B) {
        if (A.length != B.length) {
            throw new IllegalArgumentException("Number of rows in matrices do not match.");
        }
        for (int i = 0; i < A.length; i++) {
            addTo(A[i], B[i]);
        }
    }

    // Matrix addition
    public static void addTo(final double[][] A, final double b) {
        for (int i = 0; i < A.length; i++) {
            addTo(A[i], b);
        }
    }

    // Matrix addition
    public static double[][] add(final double[][] A, final double[][] B) {
        if (A.length != B.length) {
            throw new IllegalArgumentException("Number of rows in matrices do not match.");
        }
        double[][] output = new double[A.length][];
        for (int i = 0; i < A.length; i++) {
            output[i] = add(A[i], B[i]);
        }
        return output;
    }

    // Dot product
    public static double dot(final double[] a, final double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vector sizes do not match.");
        }
        double output = 0.0;
        for (int i = 0; i < a.length; i++) {
            output += a[i] * b[i];
        }
        return output;
    }

    // Matrix column-vector multiplication
    public static double[] multiply(final double[][] A, final double[] b) {
        double[] output = new double[A.length];
        for (int i = 0; i < A.length; i++) {
            output[i] = dot(A[i], b);
        }
        return output;
    }

    // Row-vector matrix multiplication
    public static double[] multiply(final double[] a, final double[][] B) {
        if (a.length != B.length) {
            throw new IllegalArgumentException("Vector size does not match number of rows of a matrix.");
        }
        double[] output = new double[B[0].length];
        for (int j = 0; j < B[0].length; j++) {
            output[j] = 0.0;
            for (int i = 0; i < a.length; i++) {
                output[j] += a[i] * B[i][j];
            }
        }
        return output;
    }

    // Vector multiplication
    public static void multiply(final double a, final double[] b) {
        for (int i = 0; i < b.length; i++) {
            b[i] *= a;
        }
    }

    // Matrix multiplication
    public static void multiply(final double a, final double[][] B) {
        for (int i = 0; i < B.length; i++) {
            multiply(a, B[i]);
        }
    }

    // Componentwise multiplication of vector
    public static double[] componentMultiply(final double a[], final double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Vector sizes do not match.");
        }
        double[] output = new double[a.length];
        for (int i = 0; i < a.length; i++) {
            output[i] = a[i] * b[i];
        }
        return output;
    }

}
