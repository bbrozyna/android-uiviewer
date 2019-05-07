package com.bbrozyna.caseweek.views;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AlertBox {
    public static void display(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);  // block all stuff until you accept that
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label(message);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, closeButton);
        layout.setAlignment(Pos.CENTER);

        BorderPane border = new BorderPane();
        border.setLeft(new HBox());

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

    }
}
