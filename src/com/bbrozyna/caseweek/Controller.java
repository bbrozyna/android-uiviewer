package com.bbrozyna.caseweek;

import com.bbrozyna.caseweek.models.UIHierarchy;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public ListView attributeNames;
    public ListView attributeKeys;
    public ListView attributeValues;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        UIHierarchy ui = new UIHierarchy("dump.xml");  // test file
        attributeNames.getItems().addAll(ui.getAllElementsNames());
        attributeNames.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> fillAttributes(ui, newValue));

    }

    private void fillAttributes(UIHierarchy ui, Object newValue) {
        attributeKeys.getItems().addAll(ui.getAttributeByName().get(newValue).keySet());
        attributeValues.getItems().addAll(ui.getAttributeByName().get(newValue).values());
    }
}
