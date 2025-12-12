package com.airline.reservation.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("=== STARTING AIRLINK RESERVATION ===");
        
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ui/login.fxml"));
            root.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 0);");
            
            Scene scene = new Scene(root, 800, 600);
            
            // Set up stage
            primaryStage.setTitle(" AirLink Reservation System ");
            primaryStage.setScene(scene);
            primaryStage.show();
            
            System.out.println("=== FXML APPLICATION STARTED SUCCESSFULLY ===");
            
        } catch (Exception e) {
            System.err.println("ERROR loading FXML: " + e.getMessage());
            e.printStackTrace();
            showSimpleVersion(primaryStage);
        }
    }
    
    private void showSimpleVersion(Stage stage) {
        System.out.println("Using simple fallback version...");
        javafx.scene.control.Label label = new javafx.scene.control.Label(
            "AirLink Reservation System\n\n" +
            "FXML loading failed. Basic version active.\n" +
            "Features working:\n" +
            "? JavaFX GUI ?\n" +
            "? Basic controls ?\n" +
            "? Visual effects ?\n\n" +
            "To fix: Check FXML files and controllers"
        );
        javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane(label);
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
