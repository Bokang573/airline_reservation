package com.airline.reservation.ui;

import com.airline.reservation.database.DatabaseConnector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookingController implements Initializable {

    @FXML private ComboBox<String> cmbFlights;
    @FXML private ComboBox<String> cmbPassengers;
    @FXML private ComboBox<String> cmbTicketClass;
    @FXML private DatePicker dpTravelDate;
    @FXML private TextField txtSeatNumber;
    @FXML private Label lblPrice;
    @FXML private Label lblStatus;
    @FXML private Label lblBookingCount;
    @FXML private Button btnNewBooking;

    private final ObservableList<String> flightData = FXCollections.observableArrayList();
    private final ObservableList<String> passengerData = FXCollections.observableArrayList();

    // regex to parse "Name (ID: 123)" and also DBID "(DBID:123)"
    private static final Pattern ID_PATTERN = Pattern.compile("ID\\s*[:]?\\s*(\\d+)");
    private static final Pattern DBID_PATTERN = Pattern.compile("DBID\\s*[:]?\\s*(\\d+)");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            loadFlightsFromDB();
            loadPassengersFromDB();
            setupComboBoxes();
            updateBookingCount();
            updateStatus("System ready - " + flightData.size() + " flights available");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Failed to initialize booking screen:\n" + e.getMessage());
        }
    }

    /** Load flights from the database into cmbFlights */
    private void loadFlightsFromDB() {
        flightData.clear();
        String sql = "SELECT id, flight_number, origin, destination FROM flights ORDER BY flight_number";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String display = String.format("%s - %s to %s (DBID:%d)",
                        rs.getString("flight_number"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getInt("id"));
                flightData.add(display);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load flights: " + e.getMessage());
        }
    }

    /** Load passengers from the DB into cmbPassengers: "Full Name (ID: 123)" */
    private void loadPassengersFromDB() {
        passengerData.clear();
        String sql = "SELECT id, full_name FROM passengers ORDER BY id";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String display = String.format("%s (ID: %d)", rs.getString("full_name"), rs.getInt("id"));
                passengerData.add(display);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load passengers: " + e.getMessage());
        }
    }

    /** Initialize ComboBoxes and defaults */
    private void setupComboBoxes() {
        cmbFlights.setItems(flightData);
        cmbPassengers.setItems(passengerData);
        cmbTicketClass.getItems().setAll("Economy", "Business", "First Class");

        if (!flightData.isEmpty()) cmbFlights.setValue(flightData.get(0));
        if (!passengerData.isEmpty()) cmbPassengers.setValue(passengerData.get(0));
        cmbTicketClass.setValue("Economy");

        cmbTicketClass.valueProperty().addListener((obs, o, n) -> updatePrice());
        dpTravelDate.setValue(LocalDate.now().plusDays(1));

        updatePrice();
        suggestSeat();
    }

    /** Create and persist booking */
    @FXML
    private void handleCreateBooking() {
        if (!validateForm()) return;

        String flightText = cmbFlights.getValue();
        String passengerText = cmbPassengers.getValue();

        int flightId = resolveFlightIdFromDisplay(flightText);
        int passengerId = parseIdFromDisplay(passengerText);

        if (flightId <= 0 || passengerId <= 0) {
            showAlert(Alert.AlertType.ERROR, "Invalid Data", "Could not resolve flight or passenger database id.");
            return;
        }

        String seat = txtSeatNumber.getText().trim().toUpperCase();
        String ticketClass = cmbTicketClass.getValue();
        java.sql.Date travelDate = java.sql.Date.valueOf(dpTravelDate.getValue());
        double price = parsePrice(lblPrice.getText());

        String sql = "INSERT INTO bookings (flight_id, passenger_id, seat_number, ticket_class, travel_date, total_price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, flightId);
            stmt.setInt(2, passengerId);
            stmt.setString(3, seat);
            stmt.setString(4, ticketClass);
            stmt.setDate(5, travelDate);
            stmt.setDouble(6, price);

            int updated = stmt.executeUpdate();
            if (updated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Booking Created", "Booking stored successfully in database.");
                updateBookingCount();
                handleClearForm();
            } else {
                showAlert(Alert.AlertType.ERROR, "Insert Failed", "No rows inserted.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to store booking: " + e.getMessage());
        }
    }

    /**
     * Resolve flight DB id — supports explicit DBID in display "(DBID:123)" or queries by flight_number
     */
    private int resolveFlightIdFromDisplay(String flightDisplay) {
        if (flightDisplay == null) return -1;

        Matcher mDb = DBID_PATTERN.matcher(flightDisplay);
        if (mDb.find()) {
            try { return Integer.parseInt(mDb.group(1)); } catch (NumberFormatException ignored) {}
        }

        // fallback: assume first token is flight_number (e.g., "AI202 - ...")
        String flightNumber = flightDisplay.split(" -")[0].trim();
        String sql = "SELECT id FROM flights WHERE flight_number = ?";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, flightNumber);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /** Parse integer id from "Name (ID: 123)" */
    private int parseIdFromDisplay(String display) {
        if (display == null) return -1;
        Matcher m = ID_PATTERN.matcher(display);
        if (m.find()) {
            try { return Integer.parseInt(m.group(1)); } catch (NumberFormatException ignored) {}
        }
        return -1;
    }

    /** Helper to parse price string like "$299.00" */
    private double parsePrice(String text) {
        if (text == null) return 0.0;
        try {
            return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /** Clear the form */
    @FXML
    private void handleClearForm() {
        if (!flightData.isEmpty()) cmbFlights.setValue(flightData.get(0));
        if (!passengerData.isEmpty()) cmbPassengers.setValue(passengerData.get(0));
        cmbTicketClass.setValue("Economy");
        txtSeatNumber.clear();
        dpTravelDate.setValue(LocalDate.now().plusDays(1));
        updatePrice();
        updateStatus("Form cleared - ready for new booking");
        suggestSeat();
    }

    @FXML
    private void handleNewBooking() {
        handleClearForm();
        updateStatus("Ready for new booking");
    }

    /** Suggest a seat */
    private void suggestSeat() {
        Random random = new Random();
        int row = random.nextInt(30) + 1;
        char column = (char) ('A' + random.nextInt(6));
        txtSeatNumber.setText(row + "" + column);
    }

    /** Update price label */
    @FXML
    private void updatePrice() {
        String ticketClass = cmbTicketClass.getValue();
        double price;
        if ("Business".equals(ticketClass)) price = 699.00;
        else if ("First Class".equals(ticketClass)) price = 1299.00;
        else price = 299.00;
        lblPrice.setText(String.format("$%.2f", price));
    }

    /** Validate inputs */
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        String seat = txtSeatNumber.getText().trim();
        if (seat.isEmpty() || !seat.toUpperCase().matches("^\\d+[A-F]$")) {
            errors.append("• Invalid seat format (e.g., 12A)\n");
        }

        if (dpTravelDate.getValue() == null || dpTravelDate.getValue().isBefore(LocalDate.now())) {
            errors.append("• Travel date cannot be in the past\n");
        }

        if (cmbPassengers.getValue() == null || parseIdFromDisplay(cmbPassengers.getValue()) <= 0) {
            errors.append("• Please select a valid passenger\n");
        }

        if (cmbFlights.getValue() == null) {
            errors.append("• Please select a flight\n");
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", errors.toString());
            return false;
        }
        return true;
    }

    /** Close the window */
    @FXML
    private void handleClose() {
        Stage stage = (Stage) lblStatus.getScene().getWindow();
        stage.close();
        updateStatus("Window closed");
    }

    /** Simple message dialog */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(type);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /** Update status label */
    private void updateStatus(String message) {
        if (lblStatus != null) Platform.runLater(() -> lblStatus.setText("Status: " + message));
    }

    /** Query DB for booking count and update label */
    private void updateBookingCount() {
        String sql = "SELECT COUNT(*) AS total FROM bookings";
        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                Platform.runLater(() -> lblBookingCount.setText(total + " Active Bookings"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Platform.runLater(() -> lblBookingCount.setText("0 Active Bookings"));
        }
    }
}
