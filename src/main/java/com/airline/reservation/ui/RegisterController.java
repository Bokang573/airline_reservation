package com.airline.reservation.ui;

import com.airline.reservation.util.SceneHelper;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegisterController {

    @FXML private TextField txtName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirm;

    @FXML
    private void handleRegister() {

        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String user = txtUsername.getText().trim();
        String pass = txtPassword.getText().trim();
        String confirm = txtConfirm.getText().trim();

        if (name.isEmpty() || email.isEmpty() || user.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            shakeForm();
            showAlert("Missing Information", "Please fill out all fields.");
            return;
        }

        if (!pass.equals(confirm)) {
            shakeForm();
            showAlert("Password Mismatch", "Passwords do not match.");
            return;
        }

        // TODO: Save user to database
        showAlert("Success", "Your account has been created!");

        // Back to login screen
        handleBack();
    }

    @FXML
    private void handleBack() {
        try {
            SceneHelper.openNewWindow("/ui/login.fxml", "AirLink Login", 600, 420);

            Stage stage = (Stage) txtName.getScene().getWindow();
            stage.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void shakeForm() {
        shake(txtName);
        shake(txtEmail);
        shake(txtUsername);
        shake(txtPassword);
        shake(txtConfirm);
    }

    private void shake(javafx.scene.Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(70), node);
        tt.setFromX(0);
        tt.setByX(12);
        tt.setCycleCount(6);
        tt.setAutoReverse(true);
        tt.play();
    }
}
