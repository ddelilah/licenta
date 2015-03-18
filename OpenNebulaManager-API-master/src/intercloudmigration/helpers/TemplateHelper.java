///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package intercloudmigration.helpers;
//
//import client.OpenNebulaClient;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.opennebula.client.OneResponse;
//import org.opennebula.client.template.Template;
//import org.opennebula.client.template.TemplatePool;
//import org.opennebula.client.vm.VirtualMachine;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import models.TemplateModel;
//import models.TemplateModelON;
//
///**
// *
// * @author oneadmin
// */
//public class TemplateHelper {
//
//    private final TemplateModelFactory _factory;
//
//    public TemplateHelper() {
//        _factory = new TemplateModelFactory();
//    }
//
//    public TemplateModelON createTemplateModel(VirtualMachine virtualMachine) {
//        OneResponse vmOneResponse = virtualMachine.info();
//        return this.createTemplateModel(vmOneResponse);
//    }
//
//    public TemplateModelON createTemplateModel(OneResponse vmOneResponse) {
//        Template template = this.getTemplate(vmOneResponse);
//        TemplateModelON tm = this.createTemplateModel(template); 
//        return tm;
//    }
//
//    public TemplateModelON createTemplateModel(Template template) {
//        return _factory.createTemplateModel(template);
//    }
//
//    public Template getTemplate(OneResponse vmOneResponse) {
//
//        int templateId = getTemplateId(vmOneResponse.getMessage());
//        TemplatePool templatePool = new TemplatePool(
//                OpenNebulaClient.getInstance());
//        templatePool.info();
//        return templatePool.getById(templateId);
//    }
//
//    private int getTemplateId(String vmOneResponseMessage) {
//
//        int templateId = -1;
//        Document document;
//
//        try {
//            document = OneResponseParser.loadXMLFromString(vmOneResponseMessage);
//            Node node = document.getElementsByTagName("VM").item(0);
//            Element template = (Element) node;
//            String TemplateIdString = OneResponseParser.getValues("TEMPLATE_ID", template).get(0);
//            templateId = Integer.parseInt(TemplateIdString);
//        } catch (Exception ex) {
//            Logger.getLogger(TemplateHelper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return templateId;
//    }
//}
