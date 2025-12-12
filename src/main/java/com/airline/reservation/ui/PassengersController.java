package com.airline.reservation.ui;

import com.airline.reservation.model.Passenger;
import com.airline.reservation.util.AirLinkAnimations;
import com.airline.reservation.util.SceneHelper;
import com.airline.reservation.dao.PassengerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class PassengersController {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhone;
    @FXML private TextField txtPassport;
    @FXML private TextField txtNationality;

    @FXML private TableView<PassengerRow> tablePassengers;
    @FXML private TableColumn<PassengerRow, String> colName;
    @FXML private TableColumn<PassengerRow, String> colEmail;
    @FXML private TableColumn<PassengerRow, String> colPhone;
    @FXML private TableColumn<PassengerRow, String> colPassport;
    @FXML private TableColumn<PassengerRow, String> colNationality;

    @FXML private ToggleButton themeToggle;
    @FXML private VBox rootContainer;

    private final ObservableList<PassengerRow> passengers = FXCollections.observableArrayList();
    private final PassengerDAO passengerDAO = new PassengerDAO();

    @FXML
    public void initialize() {
        if (tablePassengers != null) {
            AirLinkAnimations.fadeIn(tablePassengers, 260);
        }

        // Bind table columns
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colEmail.setCellValueFactory(data -> data.getValue().emailProperty());
        colPhone.setCellValueFactory(data -> data.getValue().phoneProperty());
        colPassport.setCellValueFactory(data -> data.getValue().passportProperty());
        colNationality.setCellValueFactory(data -> data.getValue().nationalityProperty());

        // Load passengers from DB
        passengers.clear();
        for (Passenger p : passengerDAO.getAllPassengers()) {
            passengers.add(new PassengerRow(
                    p.getFullName(),
                    p.getEmail(),
                    p.getPhone(),
                    p.getPassport(),
                    p.getNationality()
            ));
        }
        tablePassengers.setItems(passengers);
    }

    @FXML
    private void addPassenger() {
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String passport = txtPassport.getText().trim();
        String nationality = txtNationality.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || passport.isEmpty() || nationality.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        // Create Passenger model
        Passenger newPassenger = new Passenger(name, email, phone, passport, nationality);

        // Save to DB
        boolean saved = passengerDAO.insertPassenger(newPassenger);

        if (!saved) {
            showAlert("Failed to save passenger.");
            return;
        }

        // Update TableView
        passengers.add(new PassengerRow(name, email, phone, passport, nationality));
        AirLinkAnimations.bounce(tablePassengers);
        clearFields();
    }

    private void clearFields() {
        txtName.clear();
        txtEmail.clear();
        txtPhone.clear();
        txtPassport.clear();
        txtNationality.clear();
    }

    @FXML
    private void toggleTheme() {
        if (themeToggle != null && themeToggle.getScene() != null) {
            SceneHelper.toggleTheme(themeToggle.getScene());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }


    // INNER CLASS MODEL FOR TABLE

    public static class PassengerRow {
        private final javafx.beans.property.SimpleStringProperty name;
        private final javafx.beans.property.SimpleStringProperty email;
        private final javafx.beans.property.SimpleStringProperty phone;
        private final javafx.beans.property.SimpleStringProperty passport;
        private final javafx.beans.property.SimpleStringProperty nationality;

        public PassengerRow(String name, String email, String phone, String passport, String nationality) {
            this.name = new javafx.beans.property.SimpleStringProperty(name);
            this.email = new javafx.beans.property.SimpleStringProperty(email);
            this.phone = new javafx.beans.property.SimpleStringProperty(phone);
            this.passport = new javafx.beans.property.SimpleStringProperty(passport);
            this.nationality = new javafx.beans.property.SimpleStringProperty(nationality);
        }

        public javafx.beans.property.StringProperty nameProperty() { return name; }
        public javafx.beans.property.StringProperty emailProperty() { return email; }
        public javafx.beans.property.StringProperty phoneProperty() { return phone; }
        public javafx.beans.property.StringProperty passportProperty() { return passport; }
        public javafx.beans.property.StringProperty nationalityProperty() { return nationality; }
    }
}
