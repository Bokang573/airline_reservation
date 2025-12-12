package com.airline.reservation.ui;

import com.airline.reservation.dao.FlightDAO;
import com.airline.reservation.model.Flight;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FlightManagementController {

    @FXML private TextField txtFlightNumber;
    @FXML private TextField txtOrigin;
    @FXML private TextField txtDestinationForm;
    @FXML private TextField txtPrice;

    @FXML private DatePicker dpDepartureDate;
    @FXML private TextField txtDepartureTime; // HH:mm
    @FXML private DatePicker dpArrivalDate;
    @FXML private TextField txtArrivalTime;   // HH:mm

    @FXML private TableView<Flight> tableFlights;
    @FXML private TableColumn<Flight, Integer> colId;
    @FXML private TableColumn<Flight, String> colFlightNumber;
    @FXML private TableColumn<Flight, String> colOrigin;
    @FXML private TableColumn<Flight, String> colDestination;
    @FXML private TableColumn<Flight, String> colDeparture;
    @FXML private TableColumn<Flight, String> colArrival;
    @FXML private TableColumn<Flight, Double> colPrice;

    private final FlightDAO flightDAO = new FlightDAO();
    private ObservableList<Flight> flightList;

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
        colFlightNumber.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFlightNumber()));
        colOrigin.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrigin()));
        colDestination.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDestination()));
        colDeparture.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getDepartureTime() != null ? data.getValue().getDepartureTime().format(dateTimeFormatter) : ""));
        colArrival.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getArrivalTime() != null ? data.getValue().getArrivalTime().format(dateTimeFormatter) : ""));
        colPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());

        loadFlights();
    }

    private void loadFlights() {
        List<Flight> flights = flightDAO.getAllFlights();
        flightList = FXCollections.observableArrayList(flights);
        tableFlights.setItems(flightList);
    }

    @FXML
    private void handleAdd() {
        try {
            Flight flight = getFlightFromForm();
            boolean success = flightDAO.insertFlight(flight);
            if (success) {
                loadFlights();
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Flight added successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add flight.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void handleUpdate() {
        Flight selected = tableFlights.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        try {
            Flight updatedFlight = getFlightFromForm();
            updatedFlight.setId(selected.getId());
            boolean success = flightDAO.updateFlight(updatedFlight);
            if (success) {
                loadFlights();
                clearForm();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Flight updated successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update flight.");
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid input: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Flight selected = tableFlights.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        boolean success = flightDAO.deleteFlight(selected.getId());
        if (success) {
            loadFlights();
            clearForm();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Flight deleted successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete flight.");
        }
    }

    @FXML
    private void handleSelect() {
        Flight selected = tableFlights.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        txtFlightNumber.setText(selected.getFlightNumber());
        txtOrigin.setText(selected.getOrigin());
        txtDestinationForm.setText(selected.getDestination());
        txtPrice.setText(String.valueOf(selected.getPrice()));

        if (selected.getDepartureTime() != null) {
            dpDepartureDate.setValue(selected.getDepartureTime().toLocalDate());
            txtDepartureTime.setText(selected.getDepartureTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        } else {
            dpDepartureDate.setValue(null);
            txtDepartureTime.clear();
        }

        if (selected.getArrivalTime() != null) {
            dpArrivalDate.setValue(selected.getArrivalTime().toLocalDate());
            txtArrivalTime.setText(selected.getArrivalTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        } else {
            dpArrivalDate.setValue(null);
            txtArrivalTime.clear();
        }
    }

    private Flight getFlightFromForm() {
        Flight flight = new Flight();
        flight.setFlightNumber(txtFlightNumber.getText());
        flight.setOrigin(txtOrigin.getText());
        flight.setDestination(txtDestinationForm.getText());
        flight.setPrice(Double.parseDouble(txtPrice.getText()));

        LocalDate depDate = dpDepartureDate.getValue();
        LocalDate arrDate = dpArrivalDate.getValue();

        LocalTime depTime = txtDepartureTime.getText().isEmpty() ? LocalTime.MIDNIGHT :
                LocalTime.parse(txtDepartureTime.getText(), DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime arrTime = txtArrivalTime.getText().isEmpty() ? LocalTime.MIDNIGHT :
                LocalTime.parse(txtArrivalTime.getText(), DateTimeFormatter.ofPattern("HH:mm"));

        flight.setDepartureTime(depDate != null ? LocalDateTime.of(depDate, depTime) : null);
        flight.setArrivalTime(arrDate != null ? LocalDateTime.of(arrDate, arrTime) : null);

        return flight;
    }

    private void clearForm() {
        txtFlightNumber.clear();
        txtOrigin.clear();
        txtDestinationForm.clear();
        txtPrice.clear();
        dpDepartureDate.setValue(null);
        txtDepartureTime.clear();
        dpArrivalDate.setValue(null);
        txtArrivalTime.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
