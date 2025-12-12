package com.airline.reservation.ui;

import com.airline.reservation.database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CreateAccountController {
    @FXML private TextField txtFullName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtConfirmPassword;
    @FXML private Button btnCreate;
    @FXML private Hyperlink btnBack;

    @FXML
    private void handleCreateAccount() {
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        String confirm = txtConfirmPassword.getText();

        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            show(Alert.AlertType.WARNING, "All fields required.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            show(Alert.AlertType.WARNING, "Enter a valid email address.");
            return;
        }
        if (!password.equals(confirm)) {
            show(Alert.AlertType.WARNING, "Passwords do not match.");
            return;
        }

        try (Connection conn = DatabaseConnector.connect()) {
            PreparedStatement check = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
            check.setString(1, username);
            ResultSet rs = check.executeQuery();
            if (rs.next()) {
                show(Alert.AlertType.ERROR, "Username already exists.");
                return;
            }

            String hash = BCrypt.hashpw(password, BCrypt.gensalt());
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO users (full_name, email, username, password) VALUES (?, ?, ?, ?)");
            insert.setString(1, fullName);
            insert.setString(2, email);
            insert.setString(3, username);
            insert.setString(4, hash);
            insert.executeUpdate();

            show(Alert.AlertType.INFORMATION, "Account created successfully.");
            load("/ui/login.fxml", btnCreate);

        } catch (Exception e) {
            e.printStackTrace();
            show(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin() {
        load("/ui/login.fxml", btnBack);
    }

    private void load(String resource, Control control) {
        try {
            Stage stage = (Stage) control.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(resource));
            stage.getScene().setRoot(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
            show(Alert.AlertType.ERROR, "Unable to load: " + resource);
        }
    }

    private void show(Alert.AlertType t, String msg) {
        new Alert(t, msg).showAndWait();
    }
}
