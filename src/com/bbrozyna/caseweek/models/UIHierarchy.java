package com.bbrozyna.caseweek.models;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

public class UIHierarchy {
    private Document document;

    public ArrayList<UINode> getUiElements() {
        return uiElements;
    }

    private ArrayList<UINode> uiElements;
    private HashMap<String, HashMap<String, String>> attributeByName;
    private DocumentBuilder dBuilder;


    UIHierarchy() {
        initializeVariables();

    }

    public UIHierarchy(String filePath) {
        updateHierarchy(filePath);
    }

    UIHierarchy(InputStream inputStream) {
        updateHierarchy(inputStream);

    }

    public void updateHierarchy(InputStream inputStream){
        initializeVariables();
        try {
            parseDump(inputStream);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateHierarchy(String filePath){
        initializeVariables();
        try {
            parseDump(filePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void initializeVariables() {
        uiElements = new ArrayList<>();
        attributeByName = new HashMap<>();
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public HashMap<String, HashMap<String, String>> getAttributeByName() {
        if (attributeByName.isEmpty()) {
            getAllElementsNames();
        }
        return attributeByName;
    }

    public Set<String> getAttributeProperties(String name) {
        HashMap<String, String> attribute = this.attributeByName.get(name);
        if (attribute != null)
            return attribute.keySet();
        return null;
    }

    public Collection<String> getAttributeValues(String name) {
        HashMap<String, String> attribute = this.attributeByName.get(name);
        if (attribute != null)
            return attribute.values();
        return null;
    }


    private void parseDump(String filepath) throws IOException, SAXException {
        this.document = dBuilder.parse(new File(filepath));
        parseNodes(document.getChildNodes());
    }

    private void parseDump(InputStream inputStream) throws IOException, SAXException {
        this.document = dBuilder.parse(inputStream);
        parseNodes(document.getChildNodes());
    }

    public ArrayList<String> getAllElementsNames() {
        ArrayList<String> representation = new ArrayList<>();
        uiElements.forEach(e -> {
            String name = e.toString();
            representation.add(name);
            attributeByName.put(name, e.getAttributes());
        });
        return representation;
    }

    private void parseNodes(NodeList nodeList) {

        if (!document.hasChildNodes()) {
            System.out.println("No nodes available");
            return;
        }

        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                if (tempNode.hasAttributes()) {

                    NamedNodeMap nodeMap = tempNode.getAttributes();
                    UINode tempUINode = new UINode();

                    for (int i = 0; i < nodeMap.getLength(); i++) {

                        Node node = nodeMap.item(i);
                        tempUINode.getAttributes().put(node.getNodeName(), node.getNodeValue());
                        System.out.println("attr name : " + node.getNodeName());  // debug
                        System.out.println("attr value : " + node.getNodeValue());

                    }
                    uiElements.add(tempUINode);

                }

                if (tempNode.hasChildNodes()) {
                    parseNodes(tempNode.getChildNodes());

                }

            }

        }
    }


    public static void main(String[] args) {
        UIHierarchy ui = new UIHierarchy("dump.xml");  // test file
        for (String name : ui.getAllElementsNames()) {
            System.out.println(name);
        }
    }
}


