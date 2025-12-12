package com.airline.reservation.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class SceneHelper {

    private static boolean darkTheme = false;

    /**
     * Safe FXML loader that always returns a valid Parent.
     */
    public static Parent loadScene(String fxmlPath) {
        try {
            URL url = getResourceSafe(fxmlPath);
            if (url == null) {
                System.err.println("[SceneHelper] FXML not found: " + fxmlPath);
                return createErrorPane("FXML not found:\n" + fxmlPath);
            }

            return FXMLLoader.load(url);

        } catch (Exception e) {
            System.err.println("[SceneHelper] Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
            return createErrorPane("Failed to load FXML:\n" + fxmlPath);
        }
    }

    /**
     * Opens a modal window safely.
     */
    public static void openNewWindow(String fxmlPath, String title, int width, int height) {
        Platform.runLater(() -> {
            Parent root = loadScene(fxmlPath);

            Scene scene = new Scene(root, width, height);
            applyCurrentTheme(scene);

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        });
    }

    /**
     * Replaces scene root safely.
     */
    public static void loadNewScene(Node node, String fxmlPath) {
        if (node == null || node.getScene() == null) return;

        Parent root = loadScene(fxmlPath);
        node.getScene().setRoot(root);
        applyCurrentTheme(node.getScene());
    }

    /**
     * Shows simple error popup.
     */
    public static void showErrorAlert(String header, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public static void showInfoAlert(String header, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText(header);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    /**
     * Theme toggle (light/dark).
     */
    public static void toggleTheme(Scene scene) {
        if (scene == null) return;
        darkTheme = !darkTheme;
        applyCurrentTheme(scene);
    }

    /**
     * Applies CSS safely.
     */
    private static void applyCurrentTheme(Scene scene) {
        if (scene == null) return;

        scene.getStylesheets().clear();

        String cssPath = darkTheme ? "/styles/dark.css" : "/styles/airlink-theme.css";
        URL cssUrl = getResourceSafe(cssPath);

        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("[SceneHelper] CSS not found: " + cssPath);
        }
    }

    public static boolean isDarkTheme() {
        return darkTheme;
    }

    /**
     * For controllers to reapply theme.
     */
    public static void applyTheme(Scene scene) {
        if (scene == null) return;
        Platform.runLater(() -> applyCurrentTheme(scene));
    }

    // === Helper Methods ===

    /**
     * Ensures resource paths always work.
     */
    private static URL getResourceSafe(String path) {
        URL url = SceneHelper.class.getResource(path);
        if (url == null && !path.startsWith("/")) {
            url = SceneHelper.class.getResource("/" + path);
        }
        return url;
    }

    /**
     * Fallback UI if FXML fails.
     */
    private static Parent createErrorPane(String message) {
        javafx.scene.layout.VBox box = new javafx.scene.layout.VBox();
        box.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-alignment: center;");
        javafx.scene.control.Label lbl = new javafx.scene.control.Label(message);
        lbl.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        box.getChildren().add(lbl);
        return box;
    }
}
