package com.airline.reservation.ui;

import com.airline.reservation.util.SceneHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class NavigationController {

    @FXML
    private Button currentActiveButton;

    @FXML
    public void initialize() {
        // Set dashboard as active by default
        setActiveButton(null); // You can track the active button if needed
    }

    @FXML
    private void handleNewReservation() {
        try {
            System.out.println("Loading customer form for new reservation...");
            SceneHelper.loadScene("/com/airline/reservation/ui/customer_form.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Cannot load reservation form: " + e.getMessage());
        }
    }

    @FXML
    private void handleDashboard() {
        try {
            SceneHelper.loadScene("/com/airline/reservation/ui/dashboard.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleFlights() {
        try {
            SceneHelper.loadScene("/com/airline/reservation/ui/flights.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePassengers() {
        try {
            SceneHelper.loadScene("/com/airline/reservation/ui/passengers.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBookings() {
        try {
            SceneHelper.loadScene("/com/airline/reservation/ui/bookings.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleReports() {
        try {
            SceneHelper.loadScene("/com/airline/reservation/ui/reports.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSettings() {
        try {
            SceneHelper.loadScene("/com/airline/reservation/ui/settings.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            SceneHelper.loadScene("/com/airline/reservation/ui/login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button button) {
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().remove("nav-btn-active");
        }
        currentActiveButton = button;
        if (currentActiveButton != null) {
            currentActiveButton.getStyleClass().add("nav-btn-active");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

