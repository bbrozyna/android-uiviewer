package com.bbrozyna.caseweek;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("hierarchy.fxml"));

        primaryStage.setTitle("Android UI viewer");

        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(830);
        Scene scene = new Scene(root, 900, 830);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
