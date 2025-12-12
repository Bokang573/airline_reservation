package com.airline.reservation.ui;

import com.airline.reservation.database.DatabaseConnector;
import com.airline.reservation.util.SceneHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnLogin;
    @FXML private Button btnCreateAccount;
    @FXML private Button btnExit;
    @FXML private Hyperlink linkGoogle;
    @FXML private Hyperlink linkForgot;
    @FXML private VBox loginCard;

    /** Handles standard login */
    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            show(Alert.AlertType.WARNING, "Username and password are required.");
            return;
        }

        try (Connection conn = DatabaseConnector.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT password FROM users WHERE username = ?")) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashed = rs.getString("password");
                if (BCrypt.checkpw(password, hashed)) {
                    // Use SceneHelper to load dashboard
                    SceneHelper.loadNewScene(btnLogin, "/ui/dashboard.fxml");
                } else {
                    show(Alert.AlertType.ERROR, "Invalid password. Try again.");
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Account Not Found");
                alert.setHeaderText(null);
                alert.setContentText("Account does not exist. Create one?");
                ButtonType create = new ButtonType("Create Account");
                ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(create, cancel);
                alert.showAndWait().ifPresent(resp -> {
                    if (resp == create) handleCreateAccount();
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            show(Alert.AlertType.ERROR, "Database error: " + e.getMessage());
        }
    }

    /** Opens create account page */
    @FXML
    private void handleCreateAccount() {
        SceneHelper.loadNewScene(btnCreateAccount, "/ui/create_account.fxml");
    }

    /** Stub for Google sign-in */
    @FXML
    private void handleGoogleSignIn() {
        show(Alert.AlertType.INFORMATION, "Google Sign-In not implemented yet.");
    }

    /** Opens forgot password page */
    @FXML
    private void handleForgotPassword() {
        SceneHelper.loadNewScene(linkForgot, "/ui/forgot_password.fxml");
    }

    /** Exits application */
    @FXML
    private void handleExit() {
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    /** Shows a simple alert */
    private void show(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
