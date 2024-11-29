package com.example.benchmark;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.code.algorithms.ParallelStrassenMatrixMultiplication;
import org.code.algorithms.ParallelStreamBlockMatrixMultiplication;
import org.code.algorithms.ParallelThreadedNaiveMatrixMultiplication;
import org.code.utils.InitMatrix;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ParallelMainMatrixMemoryBenchmark {
    public static void main(String[] args) throws Exception {
        InitMatrix initMatrix = new InitMatrix();
        ParallelThreadedNaiveMatrixMultiplication naive = new ParallelThreadedNaiveMatrixMultiplication();
        ParallelStreamBlockMatrixMultiplication block = new ParallelStreamBlockMatrixMultiplication();
        ParallelStrassenMatrixMultiplication strassen = new ParallelStrassenMatrixMultiplication();

        int[] sizes = {128, 256, 512, 640};
        Map<String, Object> results = new HashMap<>();

        for (int size : sizes) {
            double[][] A = initMatrix.initializeMatrix(size);
            double[][] B = initMatrix.initializeMatrix(size);
            double[][] C = new double[size][size];

            results.put(String.valueOf(size), testMethods(A, B, C, size, naive, block, strassen));
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("memory_usage.json"), results);
    }

    private static Map<String, Long> testMethods(double[][] A, double[][] B, double[][] C, int size,
                                                 ParallelThreadedNaiveMatrixMultiplication naive,
                                                 ParallelStreamBlockMatrixMultiplication block,
                                                 ParallelStrassenMatrixMultiplication strassen) {
        Map<String, Long> memoryUsage = new HashMap<>();

        memoryUsage.put("Naive", measureMemory(() -> naive.multiply(A, B, C, size)));
        memoryUsage.put("Block", measureMemory(() -> block.multiply(A, B, C, size)));
        memoryUsage.put("Strassen", measureMemory(() -> strassen.multiply(A, B)));

        return memoryUsage;
    }

    private static long measureMemory(Runnable operation) {
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Suggest garbage collection
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();

        operation.run();

        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        return afterMemory - beforeMemory;
    }
}
