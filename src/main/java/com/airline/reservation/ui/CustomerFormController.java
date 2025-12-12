package com.airline.reservation.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    @FXML private TextField txtCustomerName;
    @FXML private TextField txtFatherName;
    @FXML private DatePicker dpDateOfBirth;
    @FXML private DatePicker dpTravelDate;
    @FXML private ComboBox<String> cmbGender;
    @FXML private TextField txtProfession;
    @FXML private TextArea txtAddress;
    @FXML private TextField txtTelephone;
    @FXML private RadioButton rbNone, rbStudent, rbSenior, rbCancer;
    @FXML private Button btnNext;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupDatePickers();
        setupGenderComboBox();
        setupRadioButtons();
        setupVisualEffects();
    }

    private void setupDatePickers() {
        // Date formatter for consistent date handling
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.trim().isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (DateTimeParseException e) {
                        System.err.println("Invalid date format: " + string);
                        return null;
                    }
                }
                return null;
            }
        };

        dpDateOfBirth.setConverter(converter);
        dpTravelDate.setConverter(converter);

        // Set default travel date to tomorrow
        dpTravelDate.setValue(LocalDate.now().plusDays(1));
    }

    private void setupGenderComboBox() {
        cmbGender.getItems().addAll("Male", "Female", "Other");
    }

    private void setupRadioButtons() {
        ToggleGroup concessionGroup = new ToggleGroup();
        rbNone.setToggleGroup(concessionGroup);
        rbStudent.setToggleGroup(concessionGroup);
        rbSenior.setToggleGroup(concessionGroup);
        rbCancer.setToggleGroup(concessionGroup);
    }

    private void setupVisualEffects() {
        // Add fade transition to Next button
        btnNext.setOnMouseEntered(e -> {
            btnNext.setStyle("-fx-background-color: #3182ce; -fx-text-fill: white; -fx-font-weight: bold;");
        });

        btnNext.setOnMouseExited(e -> {
            btnNext.setStyle("-fx-background-color: #4299e1; -fx-text-fill: white; -fx-font-weight: bold;");
        });
    }

    @FXML
    private void handleNext() {
        if (validateForm()) {
            // Proceed to next step
            System.out.println("Form validated successfully!");
        }
    }

    @FXML
    private void handleCancel() {
        // Clear all fields
        clearForm();
    }

    private boolean validateForm() {
        if (txtCustomerName.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Full Name is required");
            return false;
        }

        if (cmbGender.getValue() == null) {
            showAlert("Validation Error", "Please select gender");
            return false;
        }

        if (txtAddress.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Address is required");
            return false;
        }

        if (txtTelephone.getText().trim().isEmpty()) {
            showAlert("Validation Error", "Telephone is required");
            return false;
        }

        if (dpTravelDate.getValue() == null) {
            showAlert("Validation Error", "Travel Date is required");
            return false;
        }

        return true;
    }

    private void clearForm() {
        txtCustomerName.clear();
        txtFatherName.clear();
        dpDateOfBirth.setValue(null);
        cmbGender.setValue(null);
        txtProfession.clear();
        txtAddress.clear();
        txtTelephone.clear();
        dpTravelDate.setValue(LocalDate.now().plusDays(1));
        rbNone.setSelected(true);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

