package com.example.benchmark;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.results.format.ResultFormatType;
import java.io.File;

public class executable {
    private final mainMatrixBenchmark matrixBenchmark = new mainMatrixBenchmark();
    private final mainSparseBenchmark sparseBenchmark = new mainSparseBenchmark();

    public static void main(String[] args) {
        /*
        try {
            Options options = new OptionsBuilder()
                    .include(mainSparseBenchmark.class.getSimpleName() + ".testStrassen")
                    .forks(1)
                    .warmupIterations(5)
                    .measurementIterations(10)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Strassen-Sparse-results.json")
                    .build();

            new Runner(options).run();
            System.out.println("Benchmarks completed. Results saved to benchmark-results.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        try {
            Options options = new OptionsBuilder()
                    .include(ParallelMainMatrixBenchmark.class.getSimpleName())
                    .forks(1)
                    .warmupIterations(3)
                    .measurementIterations(7)
                    .resultFormat(ResultFormatType.JSON)
                    .result("Parallel-MatrixMultiplication-results.json")
                    .build();

            new Runner(options).run();
            System.out.println("Benchmarks completed. Results saved to Parallel-MatrixMultiplication-results.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
