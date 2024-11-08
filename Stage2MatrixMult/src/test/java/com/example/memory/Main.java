package com.example.memory;

import org.code.algorithms.*;
import org.code.utils.InitMatrix;
import org.jfree.data.json.impl.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static final InitMatrix initialize = new InitMatrix();
    private static final NaiveMatrixMultiplication naiveMatrix = new NaiveMatrixMultiplication();
    private static final BlockMatrixMultiplication blockMatrix = new BlockMatrixMultiplication();
    private static final StrassenMatrixMultiplication strassenMatrix = new StrassenMatrixMultiplication();
    private static double[][] A;
    private static double[][] B;
    private static double[][] C;


    public static void main(String[] args) {
        int[] sizes = {128, 256, 512, 640}; // Ejemplo de tamaños N

        JSONObject results = new JSONObject();

        for (int N : sizes) {
            long memoryUsed = testNaiveMatrixMultiplicationMemory(N);
            results.put(String.valueOf(N), memoryUsed);
        }

        try (FileWriter file = new FileWriter("naive-matrix-memory.json")) {
            file.write(results.toString());
            System.out.println("Resultados guardados en 'naive_matrix_memory.json'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int N : sizes) {
            long memoryUsed = testBlockMatrixMultiplicationMemory(N);
            results.put(String.valueOf(N), memoryUsed);
        }

        try (FileWriter file = new FileWriter("block-matrix-memory.json")) {
            file.write(results.toString());
            System.out.println("Resultados guardados en 'block_matrix_memory.json'");

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int N : sizes) {
            long memoryUsed = testStrassenMatrixMultiplicationMemory(N);
            results.put(String.valueOf(N), memoryUsed);
        }

        try (FileWriter file = new FileWriter("strassen-matrix-memory.json")) {
            file.write(results.toString());
            System.out.println("Resultados guardados en 'strassen_matrix_memory.json'");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long testNaiveMatrixMultiplicationMemory(int N) {
        A = initialize.initializeMatrix(N);
        B = initialize.initializeMatrix(N);
        C = new double[N][N];

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();


        long totalMemoryUsed = 0;
        int numTests = 5;

        for (int i = 0; i < numTests; i++) {
            System.out.println(N);
            System.out.println(i);
            long beforeUsedMemory = runtime.totalMemory() - runtime.freeMemory();
            naiveMatrix.multiply(A,B,C,N);
            long afterUsedMemory = runtime.totalMemory() - runtime.freeMemory();
            totalMemoryUsed += (afterUsedMemory - beforeUsedMemory);
        }

        return totalMemoryUsed / numTests;
    }

    public static long testBlockMatrixMultiplicationMemory(int N) {
        A = initialize.initializeMatrix(N);
        B = initialize.initializeMatrix(N);
        C = new double[N][N];

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();


        long totalMemoryUsed = 0;
        int numTests = 5;

        for (int i = 0; i < numTests; i++) {
            System.out.println(N);
            System.out.println(i);
            long beforeUsedMemory = runtime.totalMemory() - runtime.freeMemory();
            blockMatrix.multiply(A,B,C,N);
            long afterUsedMemory = runtime.totalMemory() - runtime.freeMemory();
            totalMemoryUsed += (afterUsedMemory - beforeUsedMemory);
        }

        return totalMemoryUsed / numTests;
    }

    public static long testStrassenMatrixMultiplicationMemory(int N) {
        A = initialize.initializeMatrix(N);
        B = initialize.initializeMatrix(N);
        C = new double[N][N];

        Runtime runtime = Runtime.getRuntime();
        runtime.gc();


        long totalMemoryUsed = 0;
        int numTests = 5;

        for (int i = 0; i < numTests; i++) {
            System.out.println(N);
            System.out.println(i);
            long beforeUsedMemory = runtime.totalMemory() - runtime.freeMemory();
            C = strassenMatrix.multiply(A,B);
            long afterUsedMemory = runtime.totalMemory() - runtime.freeMemory();
            totalMemoryUsed += (afterUsedMemory - beforeUsedMemory);
        }

        return totalMemoryUsed / numTests;
    }
}
