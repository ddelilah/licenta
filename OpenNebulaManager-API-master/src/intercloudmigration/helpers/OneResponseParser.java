///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package intercloudmigration.helpers;
//
//import intercloudmigration.database.ConnectDB;
//import intercloudmigration.models.Disk;
//import intercloudmigration.models.Image;
//import intercloudmigration.models.ImageType;
//import java.io.StringReader;
//import java.util.ArrayList;
//import java.util.List;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import org.opennebula.client.image.ImagePool;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
//import services.ImageService;
//import services.ImageServiceImpl;
//import client.OpenNebulaClient;
//
//
///**
// *
// * @author oneadmin
// */
//public class OneResponseParser {
//
//    public static String getImagePath(String imageName) {
//        String path = "";
//        String imageBody = new ConnectDB().getImageBody(imageName);
//        if (!imageBody.equals("")) {
//            try {
//                Document document = OneResponseParser.loadXMLFromString(imageBody);
//                NodeList nodeList = document.getElementsByTagName("IMAGE");
//
//                Node node = nodeList.item(0);
//                Element templateElement = (Element) node;
//                path = OneResponseParser.getValues("SOURCE", templateElement).get(0);
//            } catch (Exception ex) {
//                
//            }
//        } else {
//            System.out.println("Could not retrieve image body");
//        }
//        return path;
//
//    }
//
//    public static Document loadXMLFromString(String xml) throws Exception {
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        InputSource is = new InputSource(new StringReader(xml));
//        return builder.parse(is);
//    }
//
//    public static List<String> getValues(String tag, Element element) {
//        List<String> values = new ArrayList<String>();
//        NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
//        for (int i = 0; i < nodes.getLength(); i++) {
//            Node node = (Node) nodes.item(i);
//            values.add(node.getNodeValue());
//        }
//        return values;
//    }
//
//    public static List<Disk> getDisksWithImages(String tag, Element element) {
//
//        NodeList imagesn = element.getElementsByTagName(tag);
//        List<Disk> disks = new ArrayList<Disk>();
//        for (int i = 0; i < imagesn.getLength(); i++) {
//            int imageId
//                    = Integer.parseInt(imagesn.item(i).getChildNodes().item(0).getNodeValue());
//            ImageService iis = new ImageServiceImpl();
//            
//            ImagePool imagePool = new ImagePool(OpenNebulaClient.getInstance());
//            imagePool.info();            
//            
//            org.opennebula.client.image.Image openNebulaImage = imagePool.getById(imageId);
//            
//            openNebulaImage.info();
//            Image image
//                    = new Image(
//                            imageId,
//                            ImageType.values()[openNebulaImage.type()]);
//            disks.add(new Disk(image, i));
//        }
//
//        return disks;
//    }
//}
