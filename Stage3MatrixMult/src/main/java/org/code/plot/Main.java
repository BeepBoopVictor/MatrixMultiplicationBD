package org.code.plot;
import org.json.JSONException;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws JSONException, IOException {
        /*
        MatrixPlot matrixPlot;
        String[] jsonFiles = {"Block-Matrix-results.json", "Naive-Matrix-results.json",
                "Strassen-Matrix-results.json"};
        matrixPlot = new MatrixPlot("Grafica1", jsonFiles, "Matrix multiplication");
        matrixPlot.setSize(800, 600);
        matrixPlot.setLocationRelativeTo(null);
        matrixPlot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //matrixPlot.setVisible(true);
        matrixPlot.saveChartAsImage("src/main/java/org/code/plot/plot/Matrix.png");


        SparseMatrixPlot sparseMatrixPlot;
        sparseMatrixPlot = new SparseMatrixPlot("Grafica1", "Block-Sparse-results.json", "Block Sparse");
        sparseMatrixPlot.setSize(800, 600);
        sparseMatrixPlot.setLocationRelativeTo(null);
        sparseMatrixPlot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //sparseMatrixPlot.setVisible(true);
        sparseMatrixPlot.saveChartAsImage("src/main/java/org/code/plot/plot/Block-Sparse.png");

        sparseMatrixPlot = new SparseMatrixPlot("Grafica1", "Naive-Sparse-results.json", "Naive Sparse");
        sparseMatrixPlot.setSize(800, 600);
        sparseMatrixPlot.setLocationRelativeTo(null);
        sparseMatrixPlot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //sparseMatrixPlot.setVisible(true);
        sparseMatrixPlot.saveChartAsImage("src/main/java/org/code/plot/plot/Naive-Sparse.png");

        sparseMatrixPlot = new SparseMatrixPlot("Grafica1", "Strassen-Sparse-results.json", "Strassen Sparse");
        sparseMatrixPlot.setSize(800, 600);
        sparseMatrixPlot.setLocationRelativeTo(null);
        sparseMatrixPlot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //sparseMatrixPlot.setVisible(true);
        sparseMatrixPlot.saveChartAsImage("src/main/java/org/code/plot/plot/Strassen-Sparse.png");

        ParallelMatrixPlot parallelMatrixPlot;
        parallelMatrixPlot = new ParallelMatrixPlot("Grafica1", "src/main/java/org/code/plot/json/Parallel-MatrixMultiplication-results.json");
        parallelMatrixPlot.setSize(800, 600);
        parallelMatrixPlot.setLocationRelativeTo(null);
        parallelMatrixPlot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //parallelMatrixPlot.setVisible(true);
        parallelMatrixPlot.saveChartAsImage("src/main/java/org/code/plot/plot/ParallelPlot.png");
        */

        ParallelMatrixMemoryPlot parallelMatrixMemoryPlot;
        parallelMatrixMemoryPlot = new ParallelMatrixMemoryPlot("Grafica1", "src/main/java/org/code/plot/json/memory_usage.json");
        parallelMatrixMemoryPlot.setSize(800, 600);
        parallelMatrixMemoryPlot.setLocationRelativeTo(null);
        parallelMatrixMemoryPlot.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //parallelMatrixMemoryPlot.setVisible(true);
        parallelMatrixMemoryPlot.saveChartAsImage("src/main/java/org/code/plot/plot/ParallelMemoryPlot.png");

    }
}
