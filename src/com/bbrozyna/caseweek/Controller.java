package com.bbrozyna.caseweek;

import com.bbrozyna.caseweek.models.UIHierarchy;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public final static String DUMP_PATH = "dump.xml";

    public final static int IMAGE_WIDTH = 500;
    public final static int IMAGE_HEIGHT= 800;

    private AndroidWrapperRuntime awr;
    private UIHierarchy ui;

    public ListView attributeNames;
    public ListView<String> attributeKeys;
    public ListView<String> attributeValues;
    public ImageView screenshot;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        awr = new AndroidWrapperRuntime();
        ui = new UIHierarchy(DUMP_PATH);
        attributeNames.getItems().addAll(ui.getAllElementsNames());
        attributeNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillAttributes(ui, newValue));
        updateImage();

        screenshot.setOnMouseClicked(event -> {
            clickAndUpdateScreenshot(event);
            try {
                awr.touch(Math.round(event.getSceneX()), Math.round(event.getSceneY()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void clickAndUpdateScreenshot(MouseEvent event) {
        double x = (event.getSceneX());
        double y = (event.getSceneY());

        ArrayList<Integer> phoneSize = awr.getScreenSize();

       //  todo finish the touching event

    }

    private void fillAttributes(UIHierarchy ui, Object newValue) {
        System.out.println(newValue);
        String selectedPosition = String.valueOf(newValue);
        attributeValues.getItems().clear();
        attributeKeys.getItems().clear();
        attributeKeys.getItems().addAll(ui.getAttributeProperties(selectedPosition));
        attributeValues.getItems().addAll(ui.getAttributeValues(selectedPosition));
    }

    private void updateImage(){
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("test2.jpg");
            Image image = new Image(inputStream, IMAGE_WIDTH, IMAGE_HEIGHT, true, true);
            screenshot.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
