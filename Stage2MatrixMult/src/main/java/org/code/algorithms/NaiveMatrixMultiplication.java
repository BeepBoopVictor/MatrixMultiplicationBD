package org.code.algorithms;

public class NaiveMatrixMultiplication {

    public void multiply(double[][] A, double[][] B, double[][] C, int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                double sum = 0;
                for (int k = 0; k < N; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }
    }
}