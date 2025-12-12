package com.airline.reservation.ui;

import com.airline.reservation.dao.ReportsDAO;
import com.airline.reservation.model.BookingSummary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    @FXML private Label lblFlights, lblPassengers, lblBookings;
    @FXML private BarChart<String, Number> barChart;
    @FXML private PieChart pieChart;
    @FXML private javafx.scene.control.ProgressBar progressBar;
    @FXML private javafx.scene.control.ProgressIndicator progressIndicator;
    @FXML private Pagination pagination;

    private final ReportsDAO dao = new ReportsDAO();
    private final DateTimeFormatter monthLabelFmt = DateTimeFormatter.ofPattern("yyyy-MM");
    private final int PAGE_SIZE = 20;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // KPI labels
        lblFlights.setText(String.valueOf(dao.countFlights()));
        lblPassengers.setText(String.valueOf(dao.countPassengers()));
        lblBookings.setText(String.valueOf(dao.countBookings()));

        // fill charts
        populateBarChart(6);   // last 6 months
        populatePieChart();
        setupPagination();

        // set progress to booking utilization (demo: bookings / (flights * 10) capped to 1.0)
        double flights = Math.max(1, dao.countFlights());
        double bookings = dao.countBookings();
        double progress = Math.min(1.0, bookings / (flights * 10.0));
        progressBar.setProgress(progress);
        progressIndicator.setProgress(progress);
    }

    private void populateBarChart(int months) {
        Map<String, Integer> monthCounts = dao.bookingsPerMonth(months);
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bookings");

        monthCounts.forEach((ym, cnt) -> {
            series.getData().add(new XYChart.Data<>(ym, cnt));
        });

        barChart.getData().add(series);
    }

    private void populatePieChart() {
        pieChart.getData().clear();
        Map<String, Integer> dist = dao.bookingClassDistribution();
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        dist.forEach((cls, cnt) -> {
            data.add(new PieChart.Data(cls == null ? "Unknown" : cls, cnt));
        });
        pieChart.setData(data);
    }

    private void setupPagination() {
        int total = dao.totalBookingCount();
        int pages = Math.max(1, (int) Math.ceil((double) total / PAGE_SIZE));
        pagination.setPageCount(pages);
        pagination.setPageFactory(this::createPage);
    }

    private VBox createPage(int pageIndex) {
        VBox box = new VBox(6);
        List<BookingSummary> page = dao.getBookingsPage(pageIndex, PAGE_SIZE);
        if (page.isEmpty()) return box;

        // header row
        String header = String.format("%-6s  %-24s  %-10s  %-8s  %-10s  %-7s  %-8s",
                "ID", "Passenger", "Flight", "Class", "Date", "Seat", "Fare");
        javafx.scene.control.Label hdr = new javafx.scene.control.Label(header);
        hdr.setStyle("-fx-font-weight:bold; -fx-font-family:monospace;");
        box.getChildren().add(hdr);

        DateTimeFormatter df = DateTimeFormatter.ISO_LOCAL_DATE;
        for (BookingSummary s : page) {
            String line = String.format("%-6d  %-24s  %-10s  %-8s  %-10s  %-7s  $%6.2f",
                    s.getId(),
                    safeTrunc(s.getPassengerName(), 24),
                    safeTrunc(s.getFlightNumber(), 10),
                    safeTrunc(s.getTicketClass(), 8),
                    s.getTravelDate() != null ? s.getTravelDate().format(df) : "-",
                    safeTrunc(s.getSeatNumber(), 7),
                    s.getTotalPrice()
            );
            javafx.scene.control.Label l = new javafx.scene.control.Label(line);
            l.setStyle("-fx-font-family:monospace; -fx-font-size:11px;");
            box.getChildren().add(l);
        }

        javafx.scene.control.ScrollPane sp = new javafx.scene.control.ScrollPane(box);
        sp.setFitToWidth(true);
        sp.setPrefViewportHeight(220);
        VBox wrapper = new VBox(sp);
        return wrapper;
    }

    private String safeTrunc(String s, int len) {
        if (s == null) return "-";
        if (s.length() <= len) return s;
        return s.substring(0, len - 1) + "…";
    }
}
