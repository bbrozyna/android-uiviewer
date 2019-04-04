package com.bbrozyna.caseweek;

import com.bbrozyna.caseweek.models.UIHierarchy;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public final static String DUMP_PATH = "dump.xml";

    public final static int IMAGE_WIDTH = 480;
    public final static int IMAGE_HEIGHT = 800;

    private AndroidWrapperRuntime awr;
    private UIHierarchy ui;

    public ListView attributeNames;
    public ListView<String> attributeKeys;
    public ListView<String> attributeValues;
    public ImageView screenshot;
    private Image image;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        awr = new AndroidWrapperRuntime();
        ui = new UIHierarchy(DUMP_PATH);
        attributeNames.getItems().addAll(ui.getAllElementsNames());
        attributeNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillAttributes(ui, newValue));

        updateImage();


        screenshot.setOnMouseClicked(event -> {
            clickAndUpdateScreenshot(event);
        });

    }

    private void print(Object t){
        System.out.println(String.valueOf(t));
    }

    private void clickAndUpdateScreenshot(MouseEvent event) {
        double x = (event.getX());
        double y = (event.getY());
        ArrayList<Integer> phoneSize = awr.getScreenSize();

        long touchPositionX = Math.round((x/image.getWidth()) * phoneSize.get(0));
        long touchPositionY = Math.round((y/image.getHeight()) * phoneSize.get(1));

        try {
            awr.touch(touchPositionX, touchPositionY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateImage();
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
            awr.replaceScreenshot();
            inputStream = new FileInputStream("screen.png");
            image = new Image(inputStream, IMAGE_WIDTH, IMAGE_HEIGHT, true, true);
            screenshot.setImage(image);
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

}
