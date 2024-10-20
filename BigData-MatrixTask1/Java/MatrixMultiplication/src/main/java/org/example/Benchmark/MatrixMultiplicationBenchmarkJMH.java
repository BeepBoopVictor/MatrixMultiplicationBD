package org.example.Benchmark;
import org.example.Algorithms.*;
import org.example.Utils.InitializeMatrix;
import org.openjdk.jmh.annotations.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@State(Scope.Thread)
public class MatrixMultiplicationBenchmarkJMH {

    private List<BenchmarkResult> benchmarkResults = new ArrayList<>();
    private final MatrixMultInterface MatrixMultiplication = new BasicMultiplicationAlgorithm();

    private double[][] A;
    private double[][] B;


    private void runMultiplication(MatrixMultInterface multiplication, int n) {
        double[][] result = new double[n][n];
        long startTime = System.nanoTime();
        multiplication.multiply(A, B, result, n);
        long endTime = System.nanoTime();
        double executionTime = (endTime - startTime) / 1_000_000.0;

        double memoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        double cpuUsage = 50.0;

        BenchmarkResult resultData = new BenchmarkResult(n, executionTime, memoryUsage, cpuUsage);
        benchmarkResults.add(resultData);

    }

    public void execute(int n) {
        A = new double[n][n];
        B = new double[n][n];
        InitializeMatrix.initializeMatrix(A);
        InitializeMatrix.initializeMatrix(B);
        runMultiplication(MatrixMultiplication, n);
        saveResultsToJson("Java_metrics.json");
    }

    private void saveResultsToJson(String fileName) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(benchmarkResults, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static class BenchmarkResult {
        int size;
        double execution_time;
        double memory_usage;
        double cpu_usage;

        public BenchmarkResult(int size, double execution_time, double memory_usage, double cpu_usage) {
            this.size = size;
            this.execution_time = execution_time;
            this.memory_usage = memory_usage;
            this.cpu_usage = cpu_usage;
        }
    }
}