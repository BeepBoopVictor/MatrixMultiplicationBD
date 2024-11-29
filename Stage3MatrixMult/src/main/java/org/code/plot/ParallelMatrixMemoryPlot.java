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

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONObject;

public class ParallelMatrixMemoryPlot extends JFrame {
    private JFreeChart chart;

    public ParallelMatrixMemoryPlot(String title, String jsonFilePath) throws IOException {
        super(title);

        XYSeriesCollection dataset = createDataset(jsonFilePath);

        // Crear el gráfico
        chart = ChartFactory.createXYLineChart(
                "Parallel Matrix Multiplication Memory Usage",
                "Matrix size (N)",
                "Memory Usage (bytes)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        customizeChartAppearance();

        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);
    }

    private XYSeriesCollection createDataset(String jsonFilePath) throws IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();

        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONObject jsonObject = new JSONObject(jsonData);

        XYSeries strassenSeries = new XYSeries("Strassen Multiplication");
        XYSeries blockSeries = new XYSeries("Block Multiplication");
        XYSeries naiveSeries = new XYSeries("Naive Multiplication");

        for (String key : jsonObject.keySet()) {
            int size = Integer.parseInt(key);
            JSONObject methodData = jsonObject.getJSONObject(key);

            // Agregar datos a las series
            strassenSeries.add(size, methodData.getDouble("Strassen"));
            blockSeries.add(size, methodData.getDouble("Block"));
            naiveSeries.add(size, methodData.getDouble("Naive"));
        }

        // Añadir las series al dataset
        dataset.addSeries(strassenSeries);
        dataset.addSeries(blockSeries);
        dataset.addSeries(naiveSeries);

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
                ParallelMatrixMemoryPlot example = new ParallelMatrixMemoryPlot(
                        "Benchmark Comparison", jsonFilePath
                );
                example.setSize(800, 600);
                example.setLocationRelativeTo(null);
                example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                example.setVisible(true);

                example.saveChartAsImage("ParallelPlot.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
