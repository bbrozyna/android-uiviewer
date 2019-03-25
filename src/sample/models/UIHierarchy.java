package sample.models;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UIHierarchy {
    private Document document;
    private ArrayList<UINode> uiElements;

    UIHierarchy(String filePath) {
        File file = new File(filePath);

        try {
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            this.document = dBuilder.parse(file);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public Document getDocument() {
        return document;
    }

    private void printNodes(NodeList nodeList) {
        //  todo transform into a UINode arrayList generator

        if (!document.hasChildNodes()) {
            System.out.println("No nodes available");  // TODO throw custom exception
            return;
        }

        for (int count = 0; count < nodeList.getLength(); count++) {

            Node tempNode = nodeList.item(count);

            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

                // get node name and value
                System.out.println("\nUINode Name =" + tempNode.getNodeName() + " [OPEN]");
                System.out.println("UINode Value =" + tempNode.getTextContent());

                if (tempNode.hasAttributes()) {

                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();

                    for (int i = 0; i < nodeMap.getLength(); i++) {

                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());

                    }

                }

                if (tempNode.hasChildNodes()) {

                    // loop again if has child nodes
                    printNodes(tempNode.getChildNodes());

                }

                System.out.println("UINode Name =" + tempNode.getNodeName() + " [CLOSE]");

            }

        }
    }


    public static void main(String[] args) {
        UIHierarchy ui = new UIHierarchy("dump.xml");  // test file
        ui.printNodes(ui.getDocument().getChildNodes());

    }


}

