package org.code.algorithms;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class ParallelStrassenMatrixMultiplication {

    private static final int THRESHOLD = 64;

    public double[][] multiply(double[][] A, double[][] B) {
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new StrassenTask(A, B));
    }

    private static class StrassenTask extends RecursiveTask<double[][]> {
        private final double[][] A;
        private final double[][] B;

        public StrassenTask(double[][] A, double[][] B) {
            this.A = A;
            this.B = B;
        }

        @Override
        protected double[][] compute() {
            int n = A.length;

            if (n <= THRESHOLD) {
                return naiveMultiply(A, B);
            }

            int newSize = n / 2;
            double[][] A11 = new double[newSize][newSize];
            double[][] A12 = new double[newSize][newSize];
            double[][] A21 = new double[newSize][newSize];
            double[][] A22 = new double[newSize][newSize];
            double[][] B11 = new double[newSize][newSize];
            double[][] B12 = new double[newSize][newSize];
            double[][] B21 = new double[newSize][newSize];
            double[][] B22 = new double[newSize][newSize];

            splitMatrix(A, A11, 0, 0);
            splitMatrix(A, A12, 0, newSize);
            splitMatrix(A, A21, newSize, 0);
            splitMatrix(A, A22, newSize, newSize);

            splitMatrix(B, B11, 0, 0);
            splitMatrix(B, B12, 0, newSize);
            splitMatrix(B, B21, newSize, 0);
            splitMatrix(B, B22, newSize, newSize);

            StrassenTask M1Task = new StrassenTask(add(A11, A22), add(B11, B22));
            StrassenTask M2Task = new StrassenTask(add(A21, A22), B11);
            StrassenTask M3Task = new StrassenTask(A11, subtract(B12, B22));
            StrassenTask M4Task = new StrassenTask(A22, subtract(B21, B11));
            StrassenTask M5Task = new StrassenTask(add(A11, A12), B22);
            StrassenTask M6Task = new StrassenTask(subtract(A21, A11), add(B11, B12));
            StrassenTask M7Task = new StrassenTask(subtract(A12, A22), add(B21, B22));

            invokeAll(M1Task, M2Task, M3Task, M4Task, M5Task, M6Task, M7Task);

            double[][] M1 = M1Task.join();
            double[][] M2 = M2Task.join();
            double[][] M3 = M3Task.join();
            double[][] M4 = M4Task.join();
            double[][] M5 = M5Task.join();
            double[][] M6 = M6Task.join();
            double[][] M7 = M7Task.join();

            double[][] C11 = add(subtract(add(M1, M4), M5), M7);
            double[][] C12 = add(M3, M5);
            double[][] C21 = add(M2, M4);
            double[][] C22 = add(subtract(add(M1, M3), M2), M6);

            double[][] C = new double[n][n];
            joinMatrix(C11, C, 0, 0);
            joinMatrix(C12, C, 0, newSize);
            joinMatrix(C21, C, newSize, 0);
            joinMatrix(C22, C, newSize, newSize);

            return C;
        }
    }

    private static double[][] naiveMultiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }

    private static double[][] add(double[][] A, double[][] B) {
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }
        return result;
    }

    private static double[][] subtract(double[][] A, double[][] B) {
        int n = A.length;
        double[][] result = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }
        return result;
    }

    private static void splitMatrix(double[][] parent, double[][] child, int row, int col) {
        for (int i = 0; i < child.length; i++) {
            for (int j = 0; j < child.length; j++) {
                child[i][j] = parent[i + row][j + col];
            }
        }
    }

    private static void joinMatrix(double[][] child, double[][] parent, int row, int col) {
        for (int i = 0; i < child.length; i++) {
            for (int j = 0; j < child.length; j++) {
                parent[i + row][j + col] = child[i][j];
            }
        }
    }
}
