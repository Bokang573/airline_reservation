package com.airline.reservation.ui;

import com.airline.reservation.util.SceneHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BookingConfirmationController {

    // Flight / Ticket Info
    @FXML private Label lblPnrNumber;
    @FXML private Label lblDepartureCity;
    @FXML private Label lblDepartureName;
    @FXML private Label lblArrivalCity;
    @FXML private Label lblArrivalName;
    @FXML private Label lblFlightNumber;
    @FXML private Label lblDuration;
    @FXML private Label lblDepartureTime;
    @FXML private Label lblArrivalTime;
    @FXML private Label lblTravelDate;
    @FXML private Label lblClass;
    @FXML private Label lblSeat;

    // Passenger / Fare Info
    @FXML private Label lblPassengerName;
    @FXML private Label lblConcession;
    @FXML private Label lblBaseFare;
    @FXML private Label lblDiscount;
    @FXML private Label lblTotalAmount;

    // Buttons
    @FXML private Button btnPrint;
    @FXML private Button btnEmail;
    @FXML private Button btnNewBooking;
    @FXML private Button btnDashboard;

    @FXML
    public void initialize() {
        if (lblPnrNumber != null) lblPnrNumber.setText("AL5X9B2M");
        if (lblDepartureCity != null) lblDepartureCity.setText("DEL");
        if (lblDepartureName != null) lblDepartureName.setText("Delhi");
        if (lblArrivalCity != null) lblArrivalCity.setText("BOM");
        if (lblArrivalName != null) lblArrivalName.setText("Mumbai");
        if (lblFlightNumber != null) lblFlightNumber.setText("AL101");
        if (lblDuration != null) lblDuration.setText("2h 00m");
        if (lblDepartureTime != null) lblDepartureTime.setText("08:00");
        if (lblArrivalTime != null) lblArrivalTime.setText("10:00");
        if (lblTravelDate != null) lblTravelDate.setText("Dec 15, 2024");
        if (lblClass != null) lblClass.setText("Economy");
        if (lblSeat != null) lblSeat.setText("14A");

        if (lblPassengerName != null) lblPassengerName.setText("John Doe");
        if (lblConcession != null) lblConcession.setText("Student (25% discount)");
        if (lblBaseFare != null) lblBaseFare.setText("$120.00");
        if (lblDiscount != null) lblDiscount.setText("-$30.00");
        if (lblTotalAmount != null) lblTotalAmount.setText("$108.50");
    }

    @FXML
    private void handlePrint() {
        System.out.println("Print Ticket clicked");
        // Add printing logic here
    }

    @FXML
    private void handleEmail() {
        System.out.println("Email Receipt clicked");
        // Add email sending logic here
    }

    @FXML
    private void handleNewBooking() {
        try {
            SceneHelper.loadNewScene(btnNewBooking, "/ui/bookings.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDashboard() {
        try {
            SceneHelper.loadNewScene(btnDashboard, "/ui/dashboard.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
