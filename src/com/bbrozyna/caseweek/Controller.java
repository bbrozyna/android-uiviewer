package com.bbrozyna.caseweek;

import com.bbrozyna.caseweek.models.UIHierarchy;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ListView attributeNames;
    public ListView<String> attributeKeys;
    public ListView<String> attributeValues;
    public ImageView screenshot;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UIHierarchy ui = new UIHierarchy("dump.xml");
        attributeNames.getItems().addAll(ui.getAllElementsNames());
        attributeNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillAttributes(ui, newValue));
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
            inputStream = new FileInputStream("screen.png");
            Image image = new Image(inputStream, 800, 600, true, true);
            screenshot.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
