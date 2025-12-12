package com.airline.reservation.ui;

import com.airline.reservation.util.AirLinkAnimations;
import com.airline.reservation.util.SceneHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;

public class SeatSelectionController {

    @FXML private Label lblFlightInfo;
    @FXML private Label lblClassInfo;
    @FXML private Label lblPassengerName;
    @FXML private Label lblConcession;
    @FXML private Label lblBaseFare;
    @FXML private Label lblFinalFare;
    @FXML private Label lblSelectedSeat;
    @FXML private Label lblSeatType;
    @FXML private CheckBox chkWindowPreference;
    @FXML private CheckBox chkAislePreference;
    @FXML private Button btnConfirm;

    private String selectedSeat = null;
    private final Map<String, Button> seatButtons = new HashMap<>();
    private final Map<String, String> seatTypes = new HashMap<>();

    private final double BASE_ECONOMY_PRICE = 120.0;
    private final double BASE_BUSINESS_PRICE = 250.0;
    private double currentBasePrice = BASE_ECONOMY_PRICE;
    private double discountRate = 0.0;

    @FXML
    public void initialize() {
        // Safe fade-in animation
        if (lblFlightInfo != null) AirLinkAnimations.fadeIn(lblFlightInfo, 400);

        initializeSampleData();
        initializeSeatMap();
        applyConcessionDiscount();

        if (btnConfirm != null) btnConfirm.setDisable(true);
    }

    private void initializeSampleData() {
        lblFlightInfo.setText("AL101 - Delhi (DEL) → Mumbai (BOM)");
        lblClassInfo.setText("Economy Class • Dec 15, 2024 • 08:00-10:00");
        lblPassengerName.setText("John Doe");
        lblConcession.setText("Student (25% discount)");

        discountRate = 0.25;
        currentBasePrice = BASE_ECONOMY_PRICE;

        lblBaseFare.setText(String.format("$%.0f", currentBasePrice));
        updateFinalPrice();
    }

    private void initializeSeatMap() {
        // Define seat types
        seatTypes.put("1A", "Window"); seatTypes.put("1B", "Aisle"); seatTypes.put("1C", "Aisle"); seatTypes.put("1D", "Window");
        seatTypes.put("2A", "Window"); seatTypes.put("2B", "Aisle"); seatTypes.put("2C", "Aisle"); seatTypes.put("2D", "Window");

        String[] economyRows = {"10","11","12","13","14"};
        String[] economyColumns = {"A","B","C","D","E","F"};
        for (String row : economyRows) {
            for (String col : economyColumns) {
                String seat = row + col;
                if (col.equals("A") || col.equals("F")) seatTypes.put(seat, "Window");
                else if (col.equals("C") || col.equals("D")) seatTypes.put(seat, "Aisle");
                else seatTypes.put(seat, "Middle");
            }
        }

        markOccupiedSeats("1D", "10F", "12C");
    }

    private void markOccupiedSeats(String... seats) {
        for (String seat : seats) {
            Button seatBtn = getSeatButton(seat);
            if (seatBtn != null) {
                seatBtn.getStyleClass().remove("seat-available");
                seatBtn.getStyleClass().add("seat-occupied");
                seatBtn.setDisable(true);
            }
        }
    }

    private Button getSeatButton(String seatId) {
        return seatButtons.get(seatId);
    }

    @FXML
    private void selectSeat(ActionEvent event) {
        Button clickedSeat = (Button) event.getSource();
        String seatNumber = (String) clickedSeat.getUserData();
        String seatType = seatTypes.get(seatNumber);

        if (selectedSeat != null) {
            Button previousSeat = getSeatButton(selectedSeat);
            if (previousSeat != null && !previousSeat.getStyleClass().contains("seat-occupied")) {
                previousSeat.getStyleClass().remove("seat-selected");
                previousSeat.getStyleClass().add("seat-available");
            }
        }

        selectedSeat = seatNumber;
        clickedSeat.getStyleClass().remove("seat-available");
        clickedSeat.getStyleClass().add("seat-selected");

        lblSelectedSeat.setText(seatNumber);
        lblSeatType.setText(seatType + " Seat • " + getSeatLocation(seatNumber));
        btnConfirm.setDisable(false);

        applySeatPreferences();
    }

    private String getSeatLocation(String seatNumber) {
        return (seatNumber.startsWith("1") || seatNumber.startsWith("2")) ? "Business Class" : "Economy Class";
    }

    @FXML
    private void updateSeatPreferences() {
        if (selectedSeat != null) applySeatPreferences();
    }

    private void applySeatPreferences() {
    }

    private void applyConcessionDiscount() {
        updateFinalPrice();
    }

    private void updateFinalPrice() {
        double discountedPrice = currentBasePrice * (1 - discountRate);
        lblFinalFare.setText(String.format("$%.0f", discountedPrice));
    }

    @FXML
    private void handleConfirm() {
        if (selectedSeat != null) {
            try {
                SceneHelper.loadNewScene(btnConfirm, "/ui/booking_confirmation.fxml");

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Booking Error",
                        "Failed to confirm booking: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Seat Selection",
                    "Please select a seat before confirming.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            SceneHelper.loadNewScene(btnConfirm, "/ui/flight_selection.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Cannot load flight selection: " + e.getMessage());
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
