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

        // Crear el conjunto de datos (dataset)
        XYSeriesCollection dataset = createDataset(jsonFilePaths);

        // Crear el gráfico de puntos
        chart = ChartFactory.createScatterPlot(
                algorithm,      // Título
                "Matrix size",  // Etiqueta del eje X
                "Score (ms/op)",// Etiqueta del eje Y
                dataset,        // Datos
                PlotOrientation.VERTICAL,
                true,           // Incluir leyenda
                true,
                false
        );

        customizeChartAppearance();

        // Crear un panel para mostrar el gráfico
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(800, 600));
        setContentPane(panel);
    }

    private XYSeriesCollection createDataset(String[] jsonFilePaths) throws JSONException, IOException {
        XYSeriesCollection dataset = new XYSeriesCollection();

        // Iterar sobre cada archivo JSON y añadir datos a una serie
        for (String filePath : jsonFilePaths) {
            String jsonData = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray jsonArray = new JSONArray(jsonData);

            XYSeries series = new XYSeries(filePath);

            // Iterar sobre cada objeto en el arreglo JSON
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

        // Establecer fondo blanco
        plot.setBackgroundPaint(Color.WHITE);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.GRAY);  // Grid line color for X-axis
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);   // Grid line color for Y-axis

        // Personalizar los puntos del gráfico
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
