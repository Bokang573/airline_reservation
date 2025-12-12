package com.airline.reservation.ui;

import com.airline.reservation.dao.CancellationDAO;
import com.airline.reservation.model.Booking;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class CancellationController implements Initializable {

    @FXML private TextField txtPnr;
    @FXML private VBox bookingDetails;
    @FXML private Label lblPnr;
    @FXML private Label lblPassenger;
    @FXML private Label lblFlight;
    @FXML private Label lblDate;
    @FXML private Label lblOriginalFare;
    @FXML private Label lblRefund;

    @FXML private TableView<Booking> tableView;
    @FXML private TableColumn<Booking, Integer> colId;
    @FXML private TableColumn<Booking, Integer> colPassengerId;
    @FXML private TableColumn<Booking, Integer> colFlightId;
    @FXML private TableColumn<Booking, String> colSeat;
    @FXML private TableColumn<Booking, String> colClass;
    @FXML private TableColumn<Booking, LocalDate> colDate;
    @FXML private TableColumn<Booking, Double> colPrice;
    @FXML private TableColumn<Booking, String> colStatus;

    private final ObservableList<Booking> bookingsList = FXCollections.observableArrayList();
    private final CancellationDAO dao = new CancellationDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Use constructors (not a static factory)
        colId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getId()));
        colPassengerId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getPassengerId()));
        colFlightId.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getFlightId()));
        colSeat.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getSeatNumber()));
        colClass.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getTicketClass()));
        colDate.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getTravelDate()));
        colPrice.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getTotalPrice()));
        colStatus.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus()));

        tableView.setItems(bookingsList);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel != null) populateDetails(sel);
        });

        bookingDetails.setVisible(false);
        loadAllBookings();
    }

    private void loadAllBookings() {
        bookingsList.clear();
        List<Booking> all = dao.getAllBookings();
        if (all != null && !all.isEmpty()) bookingsList.addAll(all);
    }

    @FXML
    private void handleShowAll(ActionEvent event) {
        txtPnr.clear();
        loadAllBookings();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String id = txtPnr.getText().trim();
        if (id.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter a booking id (PNR).");
            return;
        }
        Booking b = dao.getBookingById(id);
        if (b != null) {
            tableView.getSelectionModel().select(b);
            tableView.scrollTo(b);
            populateDetails(b);
        } else {
            showAlert(Alert.AlertType.WARNING, "Not Found", "No booking found with ID: " + id);
            bookingDetails.setVisible(false);
        }
    }

    @FXML
    private void handleClear(ActionEvent event) {
        txtPnr.clear();
        tableView.getSelectionModel().clearSelection();
        clearDetails();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        Node src = (Node) event.getSource();
        Stage stage = (Stage) src.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        String idText = lblPnr.getText();
        if (idText == null || idText.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "No selection", "No booking selected to cancel.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to cancel booking ID " + idText + "?\nRefund: " + lblRefund.getText(),
                ButtonType.OK, ButtonType.CANCEL);
        confirm.setHeaderText("Confirm cancellation");
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                boolean ok = dao.cancelBooking(idText);
                if (ok) {
                    showAlert(Alert.AlertType.INFORMATION, "Cancelled", "Booking cancelled successfully.");
                    loadAllBookings();
                    clearDetails();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed", "Failed to cancel booking. Check logs.");
                }
            }
        });
    }

    private void populateDetails(Booking b) {
        if (b == null) { bookingDetails.setVisible(false); return; }
        lblPnr.setText(String.valueOf(b.getId()));
        lblPassenger.setText(String.valueOf(b.getPassengerId()));
        lblFlight.setText(String.valueOf(b.getFlightId()));
        lblDate.setText(b.getTravelDate() != null ? b.getTravelDate().toString() : "-");
        lblOriginalFare.setText(String.format("%.2f", b.getTotalPrice()));
        lblRefund.setText(String.format("%.2f", dao.calculateRefund(b)));
        bookingDetails.setVisible(true);
    }

    private void clearDetails() {
        lblPnr.setText("");
        lblPassenger.setText("");
        lblFlight.setText("");
        lblDate.setText("");
        lblOriginalFare.setText("");
        lblRefund.setText("");
        bookingDetails.setVisible(false);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}
