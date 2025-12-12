package com.airline.reservation.ui;

import com.airline.reservation.dao.DashboardDAO;
import com.airline.reservation.model.Activity;
import com.airline.reservation.util.SceneHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label lblWelcome, lblStats, lblTotalBookings, lblActiveFlights, lblTotalPassengers, lblTotalRevenue, lblStatus;
    @FXML private ImageView imgLogo;
    @FXML private TableView<Activity> tblRecentActivity;
    @FXML private TableColumn<Activity, String> colTime, colActivity, colUser, colStatus;

    private final ObservableList<Activity> activityList = FXCollections.observableArrayList();
    private DashboardDAO dashboardDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dashboardDAO = new DashboardDAO(); // DAO handles DB connection internally
        loadLogo();
        setupLabels();
        setupTableView();
        updateDashboard();
    }

    private void loadLogo() {
        try {
            URL logoUrl = getClass().getResource("/images/airline-logo.png");
            if (logoUrl != null) imgLogo.setImage(new Image(logoUrl.toString()));
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
    }

    private void setupLabels() {
        lblWelcome.setText("Welcome, Admin");
        updateSystemStatus();
    }

    private void updateSystemStatus() {
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        lblStatus.setText("System Status: Online | Last Updated: " + time);
    }

    private void setupTableView() {
        colTime.setCellValueFactory(cell -> cell.getValue().timeProperty());
        colActivity.setCellValueFactory(cell -> cell.getValue().activityProperty());
        colUser.setCellValueFactory(cell -> cell.getValue().userProperty());
        colStatus.setCellValueFactory(cell -> cell.getValue().statusProperty());
        tblRecentActivity.setItems(activityList);
    }

    private void updateDashboard() {
        try {
            lblTotalBookings.setText(String.valueOf(dashboardDAO.getTotalBookings()));
            lblActiveFlights.setText(String.valueOf(dashboardDAO.getActiveFlights()));
            lblTotalPassengers.setText(String.valueOf(dashboardDAO.getTotalPassengers()));
            lblTotalRevenue.setText("$" + String.format("%.2f", dashboardDAO.getTotalRevenue()));

            List<Activity> recentActivities = dashboardDAO.getRecentActivities();
            if (recentActivities != null) activityList.setAll(recentActivities);

            lblStats.setText(dashboardDAO.getStatsSummary());
        } catch (Exception e) {
            System.err.println("Error updating dashboard: " + e.getMessage());
            SceneHelper.showErrorAlert("Dashboard Error", "Could not load dashboard data.");
        }
    }

    //  FXML HANDLERS

    @FXML private void handleNewReservation() {
        SceneHelper.openNewWindow("/ui/bookings.fxml", "New Reservation", 1200, 800);
    }

    @FXML private void handleCancellation() {
        SceneHelper.openNewWindow("/ui/cancellation.fxml", "Cancel Booking", 800, 600);
    }

    @FXML private void handleFlightManagement() {
        SceneHelper.openNewWindow("/ui/flight_management.fxml", "Flight Management", 1200, 800);
    }

    @FXML private void handlePassengerManagement() {
        SceneHelper.openNewWindow("/ui/Passengers.fxml", "Passenger Management", 1200, 700);
    }

    @FXML private void handleReports() {
        SceneHelper.openNewWindow("/ui/Reports.fxml", "Reports", 1200, 600);
    }

    @FXML private void handleSettings() {
        SceneHelper.openNewWindow("/ui/Settings.fxml", "Settings", 1200, 600);
    }

    @FXML private void handleRefresh() {
        updateDashboard();
        updateSystemStatus();
        SceneHelper.showInfoAlert("Refreshed", "Dashboard data has been refreshed.");
    }

    @FXML private void handleLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to logout?");
        alert.setTitle("Logout");
        alert.setHeaderText("Confirm Logout");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    Stage stage = (Stage) lblWelcome.getScene().getWindow();
                    stage.close();
                    SceneHelper.openNewWindow("/ui/login.fxml", "AirLink Login", 600, 400);
                } catch (Exception e) {
                    SceneHelper.showErrorAlert("Logout Failed", e.getMessage());
                }
            }
        });
    }
}
