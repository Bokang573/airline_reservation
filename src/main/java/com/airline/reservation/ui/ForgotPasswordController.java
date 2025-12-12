package com.airline.reservation.ui;

import com.airline.reservation.database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Base64;

public class ForgotPasswordController {
    @FXML private TextField txtUsername;
    @FXML private Button btnGenerate;
    @FXML private Hyperlink btnBack;

    @FXML
    private void handleGenerateTemp() {
        String username = txtUsername.getText().trim();
        if (username.isEmpty()) {
            show(Alert.AlertType.WARNING, "Enter username.");
            return;
        }

        try (Connection conn = DatabaseConnector.connect()) {
            // check exists
            PreparedStatement check = conn.prepareStatement("SELECT id FROM users WHERE username = ?");
            check.setString(1, username);
            var rs = check.executeQuery();
            if (!rs.next()) {
                show(Alert.AlertType.ERROR, "No account with that username.");
                return;
            }

            // generate a safe temp password
            String temp = generateTempPassword();
            String hash = BCrypt.hashpw(temp, BCrypt.gensalt());

            PreparedStatement upd = conn.prepareStatement("UPDATE users SET password = ? WHERE username = ?");
            upd.setString(1, hash);
            upd.setString(2, username);
            upd.executeUpdate();

            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Temporary Password");
            info.setHeaderText("Temporary password generated");
            info.setContentText("Temporary password: " + temp + "\nPlease log in and change it immediately.");
            info.showAndWait();

            load("/ui/login.fxml", btnGenerate);

        } catch (Exception e) {
            e.printStackTrace();
            show(Alert.AlertType.ERROR, "Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleBack() {
        load("/ui/login.fxml", btnBack);
    }

    private String generateTempPassword() {
        SecureRandom rnd = new SecureRandom();
        byte[] b = new byte[9];
        rnd.nextBytes(b);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
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
