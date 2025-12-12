package com.airline.reservation.ui;

import com.airline.reservation.util.AirLinkAnimations;
import com.airline.reservation.util.SceneHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.time.format.DateTimeFormatter;

public class FlightsController {

    // INPUTS
    @FXML private TextField txtFlightNumber;
    @FXML private TextField txtOrigin;
    @FXML private TextField txtDestination;
    @FXML private TextField txtDepartureTime;
    @FXML private DatePicker txtDate;

    // TABLE
    @FXML private TableView<FlightRow> tableFlights;
    @FXML private TableColumn<FlightRow, String> colFlightNo;
    @FXML private TableColumn<FlightRow, String> colOrigin;
    @FXML private TableColumn<FlightRow, String> colDestination;
    @FXML private TableColumn<FlightRow, String> colDate;
    @FXML private TableColumn<FlightRow, String> colDeparture;

    // THEME SWITCH
    @FXML private ToggleButton themeToggle;

    // For fade-in safety
    @FXML private Pane rootPane;

    private final ObservableList<FlightRow> flights = FXCollections.observableArrayList();

    // INITIALIZE
    @FXML
    public void initialize() {
        // Bind table columns
        colFlightNo.setCellValueFactory(data -> data.getValue().flightNoProperty());
        colOrigin.setCellValueFactory(data -> data.getValue().originProperty());
        colDestination.setCellValueFactory(data -> data.getValue().destinationProperty());
        colDate.setCellValueFactory(data -> data.getValue().dateProperty());
        colDeparture.setCellValueFactory(data -> data.getValue().departureProperty());

        tableFlights.setItems(flights);

        // Safe fade-in
        if (rootPane != null) {
            rootPane.sceneProperty().addListener((obs, oldS, newS) -> {
                if (newS != null) {
                    AirLinkAnimations.fadeIn(rootPane, 300);
                }
            });
        } else if (tableFlights != null) {
            AirLinkAnimations.fadeIn(tableFlights, 300);
        }
    }

    // ADD FLIGHT
    @FXML
    private void addFlight() {
        String no = txtFlightNumber.getText().trim();
        String origin = txtOrigin.getText().trim();
        String dest = txtDestination.getText().trim();
        String dep = txtDepartureTime.getText().trim();

        if (no.isEmpty() || origin.isEmpty() || dest.isEmpty()
                || dep.isEmpty() || txtDate.getValue() == null) {
            show("Please fill in all fields.");
            return;
        }

        String date = txtDate.getValue()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        flights.add(new FlightRow(no, origin, dest, date, dep));

        // Bounce animation
        AirLinkAnimations.bounce(tableFlights);
        clearFields();
    }

    private void clearFields() {
        txtFlightNumber.clear();
        txtOrigin.clear();
        txtDestination.clear();
        txtDepartureTime.clear();
        txtDate.setValue(null);
    }

    // TOGGLE THEME
    @FXML
    private void toggleTheme() {
        if (rootPane != null && rootPane.getScene() != null) {
            SceneHelper.toggleTheme(rootPane.getScene());
        }
    }

    // POPUP ALERT
    private void show(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.showAndWait();
    }

    // TABLE MODEL CLASS
    public static class FlightRow {
        private final javafx.beans.property.SimpleStringProperty flightNo;
        private final javafx.beans.property.SimpleStringProperty origin;
        private final javafx.beans.property.SimpleStringProperty destination;
        private final javafx.beans.property.SimpleStringProperty date;
        private final javafx.beans.property.SimpleStringProperty departure;

        public FlightRow(String flightNo, String origin, String destination,
                         String date, String departure) {
            this.flightNo = new javafx.beans.property.SimpleStringProperty(flightNo);
            this.origin = new javafx.beans.property.SimpleStringProperty(origin);
            this.destination = new javafx.beans.property.SimpleStringProperty(destination);
            this.date = new javafx.beans.property.SimpleStringProperty(date);
            this.departure = new javafx.beans.property.SimpleStringProperty(departure);
        }

        public javafx.beans.property.StringProperty flightNoProperty() { return flightNo; }
        public javafx.beans.property.StringProperty originProperty() { return origin; }
        public javafx.beans.property.StringProperty destinationProperty() { return destination; }
        public javafx.beans.property.StringProperty dateProperty() { return date; }
        public javafx.beans.property.StringProperty departureProperty() { return departure; }
    }
}
