package org.code.utils;
import java.util.Random;

public class InitMatrix {

    public double[][] initializeSparseMatrix(int N, double percentage) {
        double[][] matrix = new double[N][N];
        Random random = new Random();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == j) {
                    matrix[i][j] = random.nextDouble(500) + 1;
                    continue;
                }
                if (random.nextDouble() < percentage) {
                    matrix[i][j] = 0;
                } else {
                    matrix[i][j] = random.nextDouble(500) + 1;
                }

            }
        }
        return matrix;
    }

    public double[][] initializeMatrix(int N) {
        double[][] matrix = new double[N][N];
        Random random = new Random();

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = random.nextInt(500) + 1;
            }
        }
        return matrix;
    }
}
