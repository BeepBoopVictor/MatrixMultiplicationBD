package org.code.algorithms;

import java.util.stream.IntStream;

public class ParallelStreamBlockMatrixMultiplication {
    private static final int BLOCK_SIZE = 64;

    public void multiply(double[][] A, double[][] B, double[][] C, int N) {
        IntStream.range(0, N / BLOCK_SIZE).parallel().forEach(i -> {
            for (int j = 0; j < N / BLOCK_SIZE; j++) {
                for (int k = 0; k < N / BLOCK_SIZE; k++) {
                    multiplyBlock(A, B, C, i * BLOCK_SIZE, j * BLOCK_SIZE, k * BLOCK_SIZE, N);
                }
            }
        });
    }

    private void multiplyBlock(double[][] A, double[][] B, double[][] C, int rowBlock, int colBlock, int kBlock, int N) {
        for (int i = rowBlock; i < Math.min(rowBlock + BLOCK_SIZE, N); i++) {
            for (int j = colBlock; j < Math.min(colBlock + BLOCK_SIZE, N); j++) {
                double sum = 0;
                for (int k = kBlock; k < Math.min(kBlock + BLOCK_SIZE, N); k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] += sum;
            }
        }
    }
}
