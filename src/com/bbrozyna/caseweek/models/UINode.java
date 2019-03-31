package com.bbrozyna.caseweek.models;

import java.util.HashMap;

public class UINode {
    // TODO create abstract object for all types of android parameters

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    private HashMap<String, String> attributes;

    UINode(){
        attributes = new HashMap<>();
    }

    public int[] getBorderCorners(){
        return null;
    }

    public boolean hasProperties(HashMap<String, String> propertiesMap){
        return false;
    }

}
