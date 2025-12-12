package com.airline.reservation.ui;

import com.airline.reservation.util.AppContext;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainController {

    // AIRLINK DASHBOARD MAIN BUTTONS

    @FXML
    public void startReservation() {
        System.out.println("=== DEBUG: startReservation called ===");

        // Test 1: Try loading.fxml first
        System.out.println("TEST 1: Trying to load loading.fxml...");
        try {
            URL loadingUrl = getClass().getResource("/ui/loading.fxml");
            System.out.println("Loading FXML URL: " + loadingUrl);

            FXMLLoader loader = new FXMLLoader(loadingUrl);
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 200));
            stage.setTitle("TEST - Loading Screen");
            stage.setX(100);
            stage.setY(100);
            stage.show();

            System.out.println("✅ LOADING.FXML WINDOW OPENED SUCCESSFULLY!");
            System.out.println("This proves the stage creation works!");

        } catch (Exception e) {
            System.out.println("❌ Even loading.fxml failed: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        // Test 2: Now try bookings.fxml
        System.out.println("\nTEST 2: Now trying bookings.fxml...");
        try {
            URL fxmlUrl = getClass().getResource("/ui/bookings.fxml");
            System.out.println("Bookings FXML URL: " + fxmlUrl);

            if (fxmlUrl == null) {
                System.out.println("❌ bookings.fxml NOT FOUND!");
                return;
            }

            System.out.println("✅ bookings.fxml found, attempting to load...");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            System.out.println("✅ bookings.fxml loaded successfully!");

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("New Reservation - AirLink");
            stage.setX(150);
            stage.setY(150);
            stage.show();

            System.out.println("✅ BOOKINGS.FXML WINDOW SHOULD BE VISIBLE NOW!");

        } catch (Exception e) {
            System.out.println("❌ bookings.fxml failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void manageCancellations() {
        System.out.println("=== DEBUG: manageCancellations called ===");
        try {
            URL fxmlUrl = getClass().getResource("/ui/cancellation.fxml");
            System.out.println("Cancellation FXML URL: " + fxmlUrl);

            if (fxmlUrl == null) {
                System.out.println("❌ cancellation.fxml NOT FOUND!");
                return;
            }

            System.out.println("✅ cancellation.fxml found, attempting to load...");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            System.out.println("✅ cancellation.fxml loaded successfully!");

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Manage Cancellations - AirLink");
            stage.setX(200);
            stage.setY(200);
            stage.show();

            System.out.println("✅ CANCELLATION WINDOW SHOULD BE VISIBLE NOW!");

        } catch (Exception e) {
            System.out.println("❌ Could not load cancellation window: " + e.getMessage());
        }
    }

    @FXML
    public void manageFlights() {
        System.out.println("=== DEBUG: manageFlights called ===");
        try {
            URL fxmlUrl = getClass().getResource("/ui/flight_management.fxml");
            System.out.println("Flight Management FXML URL: " + fxmlUrl);

            if (fxmlUrl == null) {
                System.out.println("❌ flight_management.fxml NOT FOUND!");
                return;
            }

            System.out.println("✅ flight_management.fxml found, attempting to load...");
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            System.out.println("✅ flight_management.fxml loaded successfully!");

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Flight Management - AirLink");
            stage.setX(250);
            stage.setY(250);
            stage.show();

            System.out.println("✅ FLIGHT MANAGEMENT WINDOW SHOULD BE VISIBLE NOW!");

        } catch (Exception e) {
            System.out.println("❌ Could not load flight management window: " + e.getMessage());
        }
    }

    // === DASHBOARD QUICK ACTIONS ===

    @FXML
    public void handleNewReservation() {
        startReservation();
    }

    @FXML
    public void handleViewFlights() {
        System.out.println("Opening View Flights...");
        try {
            URL fxmlUrl = getClass().getResource("/ui/flights.fxml");
            if (fxmlUrl == null) {
                System.out.println("ERROR: flights.fxml not found!");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("View Flights - AirLink");
            stage.show();
        } catch (Exception e) {
            System.out.println("ERROR: Could not load flights window: " + e.getMessage());
        }
    }

    @FXML
    public void handleViewPassengers() {
        System.out.println("Opening View Passengers...");
        try {
            URL fxmlUrl = getClass().getResource("/ui/passengers.fxml");
            if (fxmlUrl == null) {
                System.out.println("ERROR: passengers.fxml not found!");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Passengers - AirLink");
            stage.show();
        } catch (Exception e) {
            System.out.println("ERROR: Could not load passengers window: " + e.getMessage());
        }
    }

    @FXML
    public void handleViewBookings() {
        System.out.println("Opening View Bookings...");
        try {
            URL fxmlUrl = getClass().getResource("/ui/bookings.fxml");
            if (fxmlUrl == null) {
                System.out.println("ERROR: bookings.fxml not found!");
                return;
            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Bookings - AirLink");
            stage.show();
        } catch (Exception e) {
            System.out.println("ERROR: Could not load bookings window: " + e.getMessage());
        }
    }

    // === EXISTING NAVIGATION METHODS ===

    @FXML
    public void goToFlightSelection(ActionEvent ev) {
        try {
            URL fxmlUrl = getClass().getResource("/ui/flight_selection.fxml");
            if (fxmlUrl == null) {
                System.out.println("ERROR: flight_selection.fxml not found!");
                return;
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Select Flight");
            stage.show();
        } catch (Exception e) {
            System.out.println("ERROR: Could not load flight selection: " + e.getMessage());
        }
    }

    @FXML
    public void goToAdmin(ActionEvent ev) {
        try {
            URL fxmlUrl = getClass().getResource("/ui/admin_dashboard.fxml");
            if (fxmlUrl == null) {
                System.out.println("ERROR: admin_dashboard.fxml not found!");
                return;
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (Exception e) {
            System.out.println("ERROR: Could not load admin dashboard: " + e.getMessage());
        }
    }

    @FXML
    public void goToPaging(ActionEvent ev) {
        try {
            URL fxmlUrl = getClass().getResource("/ui/paging.fxml");
            if (fxmlUrl == null) {
                System.out.println("ERROR: paging.fxml not found!");
                return;
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Pagination Demo");
            stage.show();
        } catch (Exception e) {
            System.out.println("ERROR: Could not load paging: " + e.getMessage());
        }
    }

    @FXML
    public void logout(ActionEvent ev) {
        try {
            AppContext.setLoggedInUser(null);
            URL fxmlUrl = getClass().getResource("/ui/login.fxml");
            if (fxmlUrl == null) {
                System.out.println("ERROR: login.fxml not found!");
                return;
            }
            Parent root = FXMLLoader.load(fxmlUrl);
            Stage stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            System.out.println("ERROR: Could not load login: " + e.getMessage());
        }
    }

    @FXML
    public void exitApp(ActionEvent ev) {
        System.exit(0);
    }

    @FXML
    public void closeWindow(ActionEvent ev) {
        Stage stage = (Stage)((Node)ev.getSource()).getScene().getWindow();
        stage.close();
    }

    // INITIALIZATION METHOD

    @FXML
    public void initialize() {
        System.out.println("MainController initialized successfully!");
    }
}

