package com.example.benchmark;

import org.openjdk.jmh.annotations.Benchmark;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.*;

import org.code.algorithms.*;
import org.code.utils.InitMatrix;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class ParallelMainMatrixBenchmark {
    @Param({"128", "256", "512", "640"})
    private int N;

    InitMatrix initMatrix = new InitMatrix();
    ParallelThreadedNaiveMatrixMultiplication naive = new ParallelThreadedNaiveMatrixMultiplication();
    ParallelStreamBlockMatrixMultiplication block = new ParallelStreamBlockMatrixMultiplication();
    ParallelStrassenMatrixMultiplication strassen = new ParallelStrassenMatrixMultiplication();
    double[][] A;
    double[][] B;
    double[][] C;

    @Setup
    public void setup(){
        A = initMatrix.initializeMatrix(N);
        B = initMatrix.initializeMatrix(N);
        C = new double[N][N];
    }

    @Benchmark
    public void testNaive() {
        naive.multiply(A, B, C, N);
    }

    @Benchmark
    public void testBlock() {
        block.multiply(A, B, C, N);
    }

    @Benchmark
    public double[][] testStrassen() {
        return strassen.multiply(A, B);
    }

}
