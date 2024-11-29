package org.code.plot;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ParallelMatrixPlot extends JFrame {
    private JFreeChart chart;

    public ParallelMatrixPlot(String title, String jsonFilePath) throws JSONException, IOException {
        super(title);

        XYSeriesCollection dataset = createDataset(jsonFilePath);

        // Crear el gráfico
        chart = ChartFactory.createXYLineChart(
                "Parallel Matrix Multiplication",
                "Matrix size (N)",
                "Score (ms/op)",
                dataset,
                PlotOrientation.VERTICAL,
                true, // Mostrar leyenda
                true, // Mostrar tooltips
                false // No generar URLs
        );

        // Personalizar la apariencia del gráfico
        customizeChartAppearance();

        // Añadir el gráfico al panel principal
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);
    }

    private XYSeriesCollection createDataset(String jsonFilePath) throws JSONException, IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();

        // Leer los datos del archivo JSON
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONArray jsonArray = new JSONArray(jsonData);

        XYSeries function1Series = new XYSeries("Block Multiplication");
        XYSeries function2Series = new XYSeries("Naive Multiplication");
        XYSeries function3Series = new XYSeries("Strassen Multiplication");

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            String benchmark = obj.getString("benchmark");
            int size = obj.getJSONObject("params").getInt("N");
            double score = obj.getJSONObject("primaryMetric").getDouble("score");

            // Agregar los datos a las series correspondientes
            if (benchmark.equals("com.example.benchmark.ParallelMainMatrixBenchmark.testBlock")) {
                function1Series.add(size, score);
            } else if (benchmark.equals("com.example.benchmark.ParallelMainMatrixBenchmark.testNaive")) {
                function2Series.add(size, score);
            } else if (benchmark.equals("com.example.benchmark.ParallelMainMatrixBenchmark.testStrassen")) {
                function3Series.add(size, score);
            }
        }

        // Añadir las series al dataset
        dataset.addSeries(function1Series);
        dataset.addSeries(function2Series);
        dataset.addSeries(function3Series);

        return dataset;
    }

    private void customizeChartAppearance() {
        XYPlot plot = chart.getXYPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.BLUE);
        renderer.setSeriesPaint(2, Color.GREEN);

        plot.setRenderer(renderer);
    }

    public void saveChartAsImage(String outputPath) throws IOException {
        File imageFile = new File(outputPath);
        ChartUtils.saveChartAsPNG(imageFile, chart, 800, 600);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                String jsonFilePath = args[0];
                ParallelMatrixPlot example = new ParallelMatrixPlot(
                        "Benchmark Comparison", jsonFilePath
                );
                example.setSize(800, 600);
                example.setLocationRelativeTo(null);
                example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                example.setVisible(true);

                example.saveChartAsImage("ParallelPlot.png");
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        });
    }
}
