package org.example;

import org.example.Benchmark.MatrixMultiplicationBenchmarkJMH;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws Exception {

        int[] sizes = {10, 25, 50, 75, 100, 250};
        MatrixMultiplicationBenchmarkJMH matrixMultiplicationBenchmarkJMH = new MatrixMultiplicationBenchmarkJMH();
        for (int n: sizes){
            matrixMultiplicationBenchmarkJMH.execute(n);
        }

    }
}