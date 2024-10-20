package org.example.Utils;

public class InitializeMatrix {

    public static void initializeMatrix(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Math.random() * 10 + 1;
            }
        }
    }
}