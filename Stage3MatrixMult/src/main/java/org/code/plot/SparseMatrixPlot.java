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
import java.util.HashMap;
import java.util.Map;

public class SparseMatrixPlot extends JFrame {
    private JFreeChart chart;

    public SparseMatrixPlot(String title, String jsonFilePath, String algorithm) throws JSONException, IOException {
        super(title);

        // Crear el conjunto de datos (dataset)
        XYSeriesCollection dataset = createDataset(jsonFilePath);

        // Crear el gráfico de dispersión con líneas
        chart = ChartFactory.createXYLineChart(
                algorithm,        // Título
                "Matrix size (N)",// Etiqueta del eje X
                "Score (ms/op)",  // Etiqueta del eje Y
                dataset,          // Datos
                PlotOrientation.VERTICAL,
                true,             // Incluir leyenda
                true,
                false
        );

        customizeChartAppearance(dataset);

        // Crear un panel para mostrar el gráfico
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);
    }

    private XYSeriesCollection createDataset(String jsonFilePath) throws JSONException, IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();
        Map<Double, XYSeries> seriesMap = new HashMap<>(); // Mapa para almacenar series por cada valor de percentage

        // Leer datos desde el archivo JSON
        String jsonData = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
        JSONArray jsonArray = new JSONArray(jsonData);

        // Iterar sobre cada objeto en el arreglo JSON
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            int size = obj.getJSONObject("params").getInt("N");
            double score = obj.getJSONObject("primaryMetric").getDouble("score");
            double percentage = obj.getJSONObject("params").getDouble("percentage");

            // Obtener o crear una serie para el porcentaje actual
            XYSeries series = seriesMap.computeIfAbsent(percentage, p -> new XYSeries("Percentage " + p));
            series.add(size, score);
        }

        // Agregar todas las series al conjunto de datos
        for (XYSeries series : seriesMap.values()) {
            dataset.addSeries(series);
        }

        return dataset;
    }

    private void customizeChartAppearance(XYSeriesCollection dataset) {
        XYPlot plot = chart.getXYPlot();

        // Establecer fondo blanco
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);

        // Crear un renderer para las líneas y los puntos
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer(true, true);

        // Asignar colores distintos a cada serie
        Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA}; // Colores predefinidos
        for (int i = 0; i < dataset.getSeriesCount(); i++) {
            renderer.setSeriesPaint(i, colors[i % colors.length]);
        }

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
                String algorithm = args[1];
                SparseMatrixPlot example = new SparseMatrixPlot("Scatter Plot for Matrix Scores", jsonFilePath, algorithm);
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
