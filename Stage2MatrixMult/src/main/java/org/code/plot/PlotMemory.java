package org.code.plot;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONObject;
import org.jfree.chart.ChartUtils;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.*;

public class PlotMemory {
    public static void main(String[] args) {
        JFreeChart chart;
        JSONObject data;
        data = loadJsonData("block-matrix-memory.json");
        chart = createChart(data);
        System.out.println(chart);
        //displayChart(chart);
        saveChartAsPNG(chart, "block_memory.png");

        data = loadJsonData("strassen-matrix-memory.json");
        chart = createChart(data);
        //displayChart(chart);
        saveChartAsPNG(chart, "strassen_memory.png");

        data = loadJsonData("naive-matrix-memory.json");
        chart = createChart(data);
        //displayChart(chart);
        saveChartAsPNG(chart, "naive_memory.png");
    }

    // Method to load JSON data
    private static JSONObject loadJsonData(String fileName) {
        StringBuilder jsonData = new StringBuilder();
        try (FileReader reader = new FileReader(fileName)) {
            int ch;
            while ((ch = reader.read()) != -1) {
                jsonData.append((char) ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONObject(jsonData.toString());
    }

    // Method to create the chart
    private static JFreeChart createChart(JSONObject data) {
        // Create a dataset
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Extract the matrix sizes (keys) and memory used (values)
        List<Integer> sortedSizes = new ArrayList<>();
        for (String size : data.keySet()) {
            sortedSizes.add(Integer.parseInt(size));
        }
        Collections.sort(sortedSizes);

        // Add sorted data to the dataset
        for (int size : sortedSizes) {
            int memoryUsed = data.getInt(String.valueOf(size));
            dataset.addValue(memoryUsed, "Memory Used (bytes)", String.valueOf(size));
        }

        // Create the chart
        JFreeChart chart = ChartFactory.createLineChart(
                "Memory Usage for Matrix Multiplication", // Chart title
                "Matrix Size (N)",                        // X-axis label
                "Memory Used (bytes)",                    // Y-axis label
                dataset,                                  // Dataset
                PlotOrientation.VERTICAL,                 // Plot orientation
                true,                                     // Include legend
                true,                                     // Tooltips
                false                                     // URLs
        );

        // Set background colors
        chart.setBackgroundPaint(java.awt.Color.WHITE);              // Chart background
        chart.getPlot().setBackgroundPaint(java.awt.Color.WHITE);    // Plot background

        CategoryPlot plot = chart.getCategoryPlot();
        plot.setDomainGridlinesVisible(true);               // Enable gridlines for X-axis
        plot.setRangeGridlinesVisible(true);                // Enable gridlines for Y-axis
        plot.setDomainGridlinePaint(Color.GRAY);            // Set grid color for X-axis
        plot.setRangeGridlinePaint(Color.GRAY);             // Set grid color for Y-axis

        return chart;
    }

    // Method to save the chart as a PNG file
    private static void saveChartAsPNG(JFreeChart chart, String fileName) {
        try {
            // Save the chart to the specified PNG file
            File file = new File(fileName);
            OutputStream outStream = new FileOutputStream(file);
            int width = 800;  // Width of the image
            int height = 600; // Height of the image
            ChartUtils.saveChartAsPNG(file, chart, width, height);
            System.out.println("Chart saved as " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to display the chart in a window (optional)
    private static void displayChart(JFreeChart chart) {
        JFrame frame = new JFrame("Matrix Multiplication Memory Usage");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new ChartPanel(chart));
        frame.pack();
        frame.setVisible(true);
    }
}
