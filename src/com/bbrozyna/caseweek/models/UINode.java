package com.bbrozyna.caseweek.models;

import java.util.HashMap;

public class UINode {

    private HashMap<String, String> attributes;

    UINode(){
        attributes = new HashMap<>();
    }

    HashMap<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }


    public int[] getBorderCorners(){
        return null;
    }

    public boolean hasProperties(HashMap<String, String> propertiesMap){
        return false;
    }

    @Override
    public String toString() {
        if (attributes.isEmpty())
            return "Empty node";

        String talkback = attributes.get("talkback");
        if (!(talkback == null || talkback.trim().isEmpty())){
            return talkback;
        }

        String text = attributes.get("text");
        if (!(text == null || text.trim().isEmpty())){
            return text;
        }

        String resourceid = attributes.get("resource-id");
        if (!(resourceid == null || resourceid.trim().isEmpty())){
            return resourceid;
        }
        String nodeClass = attributes.get("class");
        if (!(nodeClass == null || nodeClass.trim().isEmpty())){
            return nodeClass;
        }

        return super.toString();
    }

    public static void main(String[] args) {

    }

}
