package com.bbrozyna.caseweek.models;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UIHierarchy {
    private Document document;

    public ArrayList<UINode> getUiElements() {
        return uiElements;
    }

    private ArrayList<UINode> uiElements;
    private DocumentBuilder dBuilder;


    UIHierarchy(){
        uiElements = new ArrayList<UINode>();
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    UIHierarchy(String filePath) {
        uiElements = new ArrayList<UINode>();
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            parseDump(filePath);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    UIHierarchy(InputStream inputStream) {
        uiElements = new ArrayList<UINode>();
        try {
            dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            parseDump(inputStream);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void parseDump(String filepath) throws IOException, SAXException {
        this.document = dBuilder.parse(new File(filepath));
        parseNodes(document.getChildNodes());
    }

    public void parseDump(InputStream inputStream) throws IOException, SAXException {
        this.document = dBuilder.parse(inputStream);
        parseNodes(document.getChildNodes());
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
        for (UINode uiNode : ui.getUiElements()){
            for (String key : uiNode.getAttributes().keySet()){
                System.out.println(key + " " + uiNode.getAttributes().get(key));
            }
        }
    }

}

