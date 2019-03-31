package com.bbrozyna.caseweek.views;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

import java.util.HashMap;
import java.util.stream.IntStream;

public class SelectedNodeAttributesView extends Application {

    Stage window;
    Scene scene;
    Button button;
    ListView<String> listView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        button = new Button("Submit");

        VBox keys = new VBox();
        VBox values = new VBox();

        HashMap<String, String> node = new HashMap<>();
        node.put("resource", "id/test");
        node.put("text", "create");
        node.put("talkback", "create node");
        node.put("package", "com.samsung.cocacola");

        listView = new ListView<>();
        ListView<String> listView2 = new ListView<>();
        listView.getItems().addAll(node.keySet());
        listView2.getItems().addAll(node.values());

        keys.getChildren().addAll(listView);
        keys.setPrefWidth(100);
        values.getChildren().addAll(listView2);

        HBox split = new HBox(10, keys, values);

        HBox layout = new HBox(10, keys, values);

        Scene scene = new Scene(layout);


        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void buttonClicked(){
        String message = "";
        ObservableList<String> movies;
        movies = listView.getSelectionModel().getSelectedItems();

        for(String m: movies)
            message += m + "\n";

        System.out.println(message);
    }


}

