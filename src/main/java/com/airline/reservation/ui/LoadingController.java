package com.airline.reservation.ui;

import com.airline.reservation.util.AirLinkAnimations;
import com.airline.reservation.util.SceneHelper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class LoadingController {

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    public void initialize() {
        startLoadingAnimation();
    }

    private void startLoadingAnimation() {
        // Indeterminate progress
        progressIndicator.setProgress(-1);

        // Simulate loading delay
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3)));
        timeline.setOnFinished(e -> loadNextScreen());
        timeline.play();
    }

    /**
     * Attempts to load login.fxml first, then falls back to dashboard.fxml.
     */
    private void loadNextScreen() {
        // Try login screen
        Parent nextScreen = SceneHelper.loadScene("/ui/login.fxml");

        if (nextScreen != null) {
            switchToNextScreen(nextScreen);
        } else {
            System.err.println("[LoadingController] login.fxml not found, trying dashboard.fxml...");

            // Fallback to dashboard
            Parent fallbackScreen = SceneHelper.loadScene("/ui/dashboard.fxml");
            if (fallbackScreen != null) {
                switchToNextScreen(fallbackScreen);
            } else {
                System.err.println("[LoadingController] Both login.fxml and dashboard.fxml failed to load!");
                showErrorScreen();
            }
        }
    }

    /**
     * Switches to the next screen with fade animations.
     */
    private void switchToNextScreen(Parent nextScreen) {
        Node currentRoot = progressIndicator.getScene().getRoot();

        try {
            AirLinkAnimations.fadeOut(currentRoot, 320, () -> {
                currentRoot.getScene().setRoot(nextScreen);
                AirLinkAnimations.fadeIn(nextScreen, 420);
            });
        } catch (Exception e) {
            System.err.println("[LoadingController] Animation failed, using direct transition");
            currentRoot.getScene().setRoot(nextScreen);
        }
    }

    /**
     * Shows a simple error screen if both login and dashboard FXMLs fail.
     */
    private void showErrorScreen() {
        VBox errorPane = new VBox();
        errorPane.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-alignment: center;");

        Label errorLabel = new Label("Failed to load application. Please restart.");
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

        errorPane.getChildren().add(errorLabel);

        if (progressIndicator.getScene() != null) {
            progressIndicator.getScene().setRoot(errorPane);
        }
    }
}
