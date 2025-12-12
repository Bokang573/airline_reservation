package com.airline.reservation.ui;

import com.airline.reservation.util.AirLinkAnimations;
import com.airline.reservation.util.SceneHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class SettingsController {

    @FXML private TextField txtDisplayName;
    @FXML private TextField txtContactEmail;

    @FXML private TextField txtDbHost;
    @FXML private TextField txtDbPort;
    @FXML private TextField txtDbName;
    @FXML private TextField txtDbUser;
    @FXML private PasswordField txtDbPassword;

    @FXML private ToggleButton themeToggle;

    private final Path configFile = Path.of(System.getProperty("user.home"), ".airlink-config.properties");

    @FXML
    public void initialize() {
        // Safe fade-in: wait for scene to be ready
        if (themeToggle != null) {
            themeToggle.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null && newScene.getRoot() != null) {
                    AirLinkAnimations.fadeIn(newScene.getRoot(), 300);
                }
            });
        }

        loadConfig();
    }

    @FXML
    private void saveProfile() {
        String name = txtDisplayName.getText().trim();
        String email = txtContactEmail.getText().trim();

        if (name.isEmpty() || email.isEmpty()) {
            alert(AlertType.WARNING, "Please fill both name and email.");
            return;
        }

        try {
            Properties p = loadProps();
            p.setProperty("profile.name", name);
            p.setProperty("profile.email", email);
            saveProps(p);
            alert(AlertType.INFORMATION, "Profile saved.");
        } catch (Exception e) {
            e.printStackTrace();
            alert(AlertType.ERROR, "Failed to save profile.");
        }
    }

    @FXML
    private void testDbConnection() {
        String host = txtDbHost.getText().trim();
        String port = txtDbPort.getText().trim();
        String db = txtDbName.getText().trim();
        String user = txtDbUser.getText().trim();
        String pass = txtDbPassword.getText();

        if (host.isEmpty() || port.isEmpty() || db.isEmpty() || user.isEmpty()) {
            alert(AlertType.WARNING, "Please fill DB host, port, name and user.");
            return;
        }

        // Network test
        try (var socket = new java.net.Socket()) {
            var addr = new java.net.InetSocketAddress(host, Integer.parseInt(port));
            socket.connect(addr, 1800);
        } catch (Exception e) {
            alert(AlertType.ERROR, "Cannot reach database host: " + e.getMessage());
            return;
        }

        // JDBC test
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + db;
        try (var conn = java.sql.DriverManager.getConnection(url, user, pass)) {
            if (conn != null && !conn.isClosed()) {
                alert(AlertType.INFORMATION, "Database connection successful.");
            } else {
                alert(AlertType.ERROR, "JDBC connection failed.");
            }
        } catch (Exception e) {
            alert(AlertType.ERROR, "JDBC test failed: " + e.getMessage());
        }
    }

    @FXML
    private void saveDbConfig() {
        try {
            Properties p = loadProps();
            p.setProperty("db.host", txtDbHost.getText().trim());
            p.setProperty("db.port", txtDbPort.getText().trim());
            p.setProperty("db.name", txtDbName.getText().trim());
            p.setProperty("db.user", txtDbUser.getText().trim());
            p.setProperty("db.pass", txtDbPassword.getText());
            saveProps(p);
            alert(AlertType.INFORMATION, "Database configuration saved.");
        } catch (Exception e) {
            e.printStackTrace();
            alert(AlertType.ERROR, "Failed to save DB config.");
        }
    }

    @FXML
    private void showRestartMessage() {
        alert(AlertType.INFORMATION, "Restart the application to apply some system changes.");
    }

    /** FIXED toggleTheme: pass the scene, not the ToggleButton */
    @FXML
    private void toggleTheme() {
        if (themeToggle != null && themeToggle.getScene() != null) {
            SceneHelper.toggleTheme(themeToggle.getScene());
        }
    }

    private Properties loadProps() throws IOException {
        Properties p = new Properties();
        if (Files.exists(configFile)) {
            try (InputStream in = Files.newInputStream(configFile)) {
                p.load(in);
            }
        }
        return p;
    }

    private void saveProps(Properties p) throws IOException {
        try (OutputStream out = Files.newOutputStream(configFile)) {
            p.store(out, "AirLink local configuration");
        }
    }

    private void loadConfig() {
        try {
            Properties p = loadProps();
            txtDisplayName.setText(p.getProperty("profile.name", ""));
            txtContactEmail.setText(p.getProperty("profile.email", ""));
            txtDbHost.setText(p.getProperty("db.host", "localhost"));
            txtDbPort.setText(p.getProperty("db.port", "5432"));
            txtDbName.setText(p.getProperty("db.name", "airline"));
            txtDbUser.setText(p.getProperty("db.user", "postgres"));
            txtDbPassword.setText(p.getProperty("db.pass", ""));
        } catch (Exception e) {
            // defaults used
        }
    }

    private void alert(AlertType type, String msg) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.showAndWait();
    }
}
