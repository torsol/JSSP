package utils;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;

import models.Model;

public class Visualizer extends JFrame {
    private static final TaskSeriesCollection model = new TaskSeriesCollection();

    public Visualizer(String title, List<Integer> scheduledOperations, int makespan, int numberOfJobs) {
        super(title);

        // Create the dataset
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        TaskSeries data = Scheduler.createDataseriesFromSchedule(scheduledOperations, makespan);
        model.add(data);
        dataset.add(data);
        // Create chart
        JFreeChart chart = ChartFactory.createGanttChart(title, // Chart title
                "Machine Jobs", // X-Axis Label
                "Scheduling", // Y-Axis Label
                dataset, true, true, false);

        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        DateAxis range = (DateAxis) plot.getRangeAxis();
        range.setDateFormatOverride(new SimpleDateFormat("sSSS"));
        range.setMaximumDate(new Date(makespan));

    // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 570));
        setContentPane(chartPanel);

        // Use custom renderer to force correct colors on subtask
        MyRenderer renderer = new MyRenderer(model);
        plot.setRenderer(renderer);

        // Update legend to reflect correct color
        LegendItemCollection chartLegend = new LegendItemCollection();
        Shape shape = new Rectangle(10, 10);
        for (int i = 0; i < numberOfJobs; i++) {
            chartLegend.add(new LegendItem("Job " + (i + 1), null, null, null, shape, renderer.colorList[i]));
        }
        plot.setFixedLegendItems(chartLegend);

        plot.setBackgroundPaint(Color.WHITE);
    }

    // See
    // https://stackoverflow.com/questions/8938690/code-for-changing-the-color-of-subtasks-in-gantt-chart/8949913#8949913
    private static class MyRenderer extends GanttRenderer {
        private static final int PASS = 2; // Assumes two passes. Unsure what the fuck it does
        private final Color[] colorList = getUniqueColors(Model.processingMatrix.length);
        private final List<Color> clut = new ArrayList<>();
        private final TaskSeriesCollection model;
        private int row;
        private int col;
        private int index;

        MyRenderer(TaskSeriesCollection model) {
            this.model = model;
        }

        @Override
        public Paint getItemPaint(int row, int col) {
            if (clut.isEmpty() || this.row != row || this.col != col) {
                fillColorList(row, col);
                this.row = row;
                this.col = col;
                index = 0;
            }
            int colorIndex = index++ / PASS;
            return clut.get(colorIndex);
        }

        // Loops over subtasks, reads description and extracts correct color from
        // colorList. Is added to clut.
        // Clut is used by the renderer for each task.
        private void fillColorList(int row, int col) {
            clut.clear();

            TaskSeries series = (TaskSeries) model.getRowKeys().get(row);
            List<Task> tasks = series.getTasks(); // unchecked

            int taskCount = tasks.get(col).getSubtaskCount();
            taskCount = Math.max(1, taskCount);

            // System.out.println("----> " + taskCount);
            String description;
            int index;
            for (int i = 0; i < taskCount; i++) {
                description = tasks.get(col).getSubtask(i).getDescription();
                index = Integer.parseInt(description);
                clut.add(colorList[index]);
        }
        }

        // Method for generating a list of unique colors
        Color[] getUniqueColors(int amount) {
            Color[] cols = new Color[amount];
            for (int i = 0; i < amount; i++) {
                cols[i] = Color.getHSBColor((float) i / amount, 1, 1);
            }
            return cols;
        }
    }
}