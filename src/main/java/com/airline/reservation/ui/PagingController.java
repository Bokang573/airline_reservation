package com.airline.reservation.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Pagination;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

public class PagingController {
    @FXML private Pagination pager;

    @FXML
    public void initialize() {
        pager.setPageFactory(pageIndex -> {
            javafx.scene.control.Label lbl = new javafx.scene.control.Label("Page " + (pageIndex+1) + " content");
            lbl.setStyle("-fx-font-size:18px;");
            return new javafx.scene.layout.StackPane(lbl);
        });
    }

    public void backToMenu(ActionEvent ev) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/ui/main.fxml"));
        Stage stage = (Stage)((javafx.scene.Node)ev.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Airline Reservation System");
        stage.show();
    }
}

