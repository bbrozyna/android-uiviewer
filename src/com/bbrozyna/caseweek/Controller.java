package com.bbrozyna.caseweek;

import com.bbrozyna.caseweek.models.UIHierarchy;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
    public ListView attributeKeys;
    public ListView attributeValues;
    public ImageView screenshot;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UIHierarchy ui = new UIHierarchy("dump.xml");  // test file
        attributeNames.getItems().addAll(ui.getAllElementsNames());
        attributeNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillAttributes(ui, newValue));
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("screen.png");
            Image image = new Image(inputStream, 800, 600, true, true);
            screenshot.setImage(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }



    }

    private void fillAttributes(UIHierarchy ui, Object newValue) {
        System.out.println(newValue);
        attributeValues.getItems().clear();
        attributeKeys.getItems().clear();
        attributeKeys.getItems().addAll(ui.getAttributeByName().get(newValue).keySet()); // todo encapsulation
        attributeValues.getItems().addAll(ui.getAttributeByName().get(newValue).values());
    }
}
