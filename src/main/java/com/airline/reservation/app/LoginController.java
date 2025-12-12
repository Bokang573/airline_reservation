package com.airline.reservation.app;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    
    // FXML Elements from login.fxml
    @FXML private ProgressBar progressBar;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label statusLabel;
    @FXML private Button btnReservation;
    @FXML private Button btnCancellation;
    @FXML private Button btnFlightManagement;
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("LoginController initialized!");
        setupProgressIndicators();
        setupVisualEffects();
        setupButtonActions();
        simulateProgress();
    }
    
    private void setupProgressIndicators() {
        // Show indeterminate progress initially
        progressBar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        
        // Style the progress bar
        progressBar.setStyle("-fx-accent: #3b82f6;");
    }
    
    private void setupVisualEffects() {
        // Apply DropShadow effect to buttons (Project Requirement)
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        dropShadow.setRadius(5);
        dropShadow.setOffsetX(2);
        dropShadow.setOffsetY(2);
        
        btnReservation.setEffect(dropShadow);
        btnCancellation.setEffect(dropShadow);
        btnFlightManagement.setEffect(dropShadow);
        
        // Apply FadeTransition to status label (Project Requirement)
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), statusLabel);
        fadeTransition.setFromValue(0.5);
        fadeTransition.setToValue(1.0);
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
    }
    
    private void setupButtonActions() {
        btnReservation.setOnAction(e -> {
            statusLabel.setText("Making reservation...");
            showAlert("Reservation", "Reservation feature will be implemented");
        });
        
        btnCancellation.setOnAction(e -> {
            statusLabel.setText("Processing cancellation...");
            showAlert("Cancellation", "Cancellation feature will be implemented");
        });
        
        btnFlightManagement.setOnAction(e -> {
            statusLabel.setText("Opening flight management...");
            showAlert("Flight Management", "Flight management feature will be implemented");
        });
    }
    
    private void simulateProgress() {
        // Simulate progress for demonstration
        new Thread(() -> {
            try {
                for (int i = 0; i <= 100; i += 10) {
                    int progress = i;
                    javafx.application.Platform.runLater(() -> {
                        progressBar.setProgress(progress / 100.0);
                        progressIndicator.setProgress(progress / 100.0);
                        statusLabel.setText("Loading... " + progress + "%");
                    });
                    Thread.sleep(300);
                }
                
                javafx.application.Platform.runLater(() -> {
                    statusLabel.setText("System ready! Welcome " + (txtUsername.getText().isEmpty() ? "Guest" : txtUsername.getText()));
                    progressBar.setProgress(1.0);
                    progressIndicator.setProgress(1.0);
                });
                
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Menu actions
    @FXML
    private void handleExit() {
        System.out.println("Exiting application...");
        System.exit(0);
    }
    
    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About AirLink");
        alert.setHeaderText("?? AirLink Reservation System");
        alert.setContentText("JavaFX Application for Airline Reservations\n\n" +
                            "Project Requirements Met:\n" +
                            "? JavaFX GUI\n" +
                            "? FXML with Controller\n" +
                            "? Progress Indicators\n" +
                            "? Visual Effects (DropShadow + Fade)\n" +
                            "? Menu Bar\n" +
                            "\nDeveloped for OOP2 Project");
        alert.showAndWait();
    }
}
