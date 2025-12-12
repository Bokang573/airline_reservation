package com.airline.reservation.ui;

import com.airline.reservation.util.AirLinkAnimations;
import com.airline.reservation.util.SceneHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FlightSelectionController {

    @FXML private TableView<Flight> flightsTable;
    @FXML private Label lblPassengerName;
    @FXML private Label lblTravelDate;
    @FXML private Label lblConcession;
    @FXML private Label lblSelectedFlight;
    @FXML private Label lblSelectedRoute;
    @FXML private Label lblSelectedTiming;
    @FXML private ComboBox<String> cmbClass;
    @FXML private Button btnNext;

    private ObservableList<Flight> flights;

    @FXML
    public void initialize() {
        if (flightsTable != null) AirLinkAnimations.fadeIn(flightsTable, 400);

        loadSampleFlights();

        if (flightsTable != null) {
            flightsTable.getSelectionModel().selectedItemProperty().addListener(
                    (obs, oldSelection, newSelection) -> updateFlightDetails(newSelection)
            );
        }

        if (cmbClass != null) {
            cmbClass.valueProperty().addListener((obs, oldVal, newVal) -> updateNextButtonState());
        }

        if (lblPassengerName != null) lblPassengerName.setText("John Doe");
        if (lblTravelDate != null) lblTravelDate.setText("2024-12-15");
        if (lblConcession != null) lblConcession.setText("Student (25% discount)");
    }

    private void loadSampleFlights() {
        flights = FXCollections.observableArrayList(
                new Flight("AL101", "Delhi (DEL) → Mumbai (BOM)", "08:00", "10:00", "2h", "$120", "$250", "45"),
                new Flight("AL102", "Mumbai (BOM) → Delhi (DEL)", "12:00", "14:00", "2h", "$120", "$250", "32"),
                new Flight("AL201", "Delhi (DEL) → Bangalore (BLR)", "09:30", "12:00", "2h 30m", "$180", "$350", "28"),
                new Flight("AL202", "Bangalore (BLR) → Delhi (DEL)", "15:00", "17:30", "2h 30m", "$180", "$350", "51")
        );

        if (flightsTable != null) flightsTable.setItems(flights);
    }

    private void updateFlightDetails(Flight flight) {
        if (flight != null) {
            if (lblSelectedFlight != null) lblSelectedFlight.setText(flight.getFlightNumber());
            if (lblSelectedRoute != null) lblSelectedRoute.setText(flight.getRoute());
            if (lblSelectedTiming != null)
                lblSelectedTiming.setText(flight.getDepartureTime() + " - " + flight.getArrivalTime());
            updateNextButtonState();
        }
    }

    private void updateNextButtonState() {
        boolean flightSelected = flightsTable != null && flightsTable.getSelectionModel().getSelectedItem() != null;
        boolean classSelected = cmbClass != null && cmbClass.getValue() != null;
        if (btnNext != null) btnNext.setDisable(!(flightSelected && classSelected));
    }

    @FXML
    private void handleNext() {
        Flight selectedFlight = flightsTable != null ? flightsTable.getSelectionModel().getSelectedItem() : null;
        String selectedClass = cmbClass != null ? cmbClass.getValue() : null;

        if (selectedFlight != null && selectedClass != null) {
            try {
                // Navigate using SceneHelper
                SceneHelper.loadNewScene(btnNext, "/ui/seat_selection.fxml");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to proceed: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Selection Required",
                    "Please select both a flight and class to continue.");
        }
    }

    @FXML
    private void handleBack() {
        try {
            SceneHelper.loadNewScene(btnNext, "/ui/customer_form.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Cannot load customer form: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Flight {
        private final String flightNumber;
        private final String route;
        private final String departureTime;
        private final String arrivalTime;
        private final String duration;
        private final String economyPrice;
        private final String businessPrice;
        private final String availableSeats;

        public Flight(String flightNumber, String route, String departureTime,
                      String arrivalTime, String duration, String economyPrice,
                      String businessPrice, String availableSeats) {
            this.flightNumber = flightNumber;
            this.route = route;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
            this.duration = duration;
            this.economyPrice = economyPrice;
            this.businessPrice = businessPrice;
            this.availableSeats = availableSeats;
        }

        public String getFlightNumber() { return flightNumber; }
        public String getRoute() { return route; }
        public String getDepartureTime() { return departureTime; }
        public String getArrivalTime() { return arrivalTime; }
        public String getDuration() { return duration; }
        public String getEconomyPrice() { return economyPrice; }
        public String getBusinessPrice() { return businessPrice; }
        public String getAvailableSeats() { return availableSeats; }
    }
}
