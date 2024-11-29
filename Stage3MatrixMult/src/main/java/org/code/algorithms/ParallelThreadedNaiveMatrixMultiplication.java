package org.code.algorithms;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ParallelThreadedNaiveMatrixMultiplication {

    public void multiply(double[][] A, double[][] B, double[][] C, int N) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (int i = 0; i < N; i++) {
            final int row = i;
            executor.submit(() -> {
                for (int j = 0; j < N; j++) {
                    double sum = 0;
                    for (int k = 0; k < N; k++) {
                        sum += A[row][k] * B[k][j];
                    }
                    C[row][j] = sum;
                }
            });
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
    }
}
