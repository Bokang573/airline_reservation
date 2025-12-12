package com.airline.reservation.util;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

public class ReportsController {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private Label titleLabel;

    @FXML
    public void initialize() {
        titleLabel.setText("AirLink Reports");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Bookings");

        series.getData().add(new XYChart.Data<>("Jan", 120));
        series.getData().add(new XYChart.Data<>("Feb", 80));
        series.getData().add(new XYChart.Data<>("Mar", 150));
        series.getData().add(new XYChart.Data<>("Apr", 65));

        barChart.getData().clear();
        barChart.getData().add(series);
    }
}

