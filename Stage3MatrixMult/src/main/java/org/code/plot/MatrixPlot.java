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

public class MatrixPlot extends JFrame {
    private JFreeChart chart;

    public MatrixPlot(String title, String[] jsonFilePaths, String algorithm) throws JSONException, IOException {
        super(title);

        XYSeriesCollection dataset = createDataset(jsonFilePaths);

        chart = ChartFactory.createScatterPlot(
                algorithm,
                "Matrix size",
                "Score (ms/op)",
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

    private XYSeriesCollection createDataset(String[] jsonFilePaths) throws JSONException, IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();

        for (String filePath : jsonFilePaths) {
            String jsonData = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(jsonData);

            XYSeries series = new XYSeries(filePath);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                int size = obj.getJSONObject("params").getInt("N");
                double score = obj.getJSONObject("primaryMetric").getDouble("score");
                series.add(size, score);
            }

            dataset.addSeries(series);
        }

        return dataset;
    }

    private void customizeChartAppearance() {
        XYPlot plot = chart.getXYPlot();

        plot.setBackgroundPaint(Color.WHITE);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);  // Líneas apagadas, solo puntos
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
                // Los primeros tres argumentos son las rutas de archivos JSON
                String[] jsonFiles = { args[0], args[1], args[2] };
                String algorithm = args[3];
                MatrixPlot example = new MatrixPlot("Scatter Plot for Matrix Scores", jsonFiles, algorithm);
                example.setSize(800, 600);
                example.setLocationRelativeTo(null);
                example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                example.setVisible(true);
            } catch (JSONException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
