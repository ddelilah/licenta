///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package intercloudmigration.models;
//
///**
// *
// * @author oneadmin
// */
//public enum TemplateTagsEnum {
//
//    NAME("NAME"),
//    CPU("CPU"),
//    MEMORY("MEMORY"),
//    DISK("DISK", "DISK_ID","IMAGE_ID","IMAGE_TYPE"),
//    IMAGE("IMAGE", "IMAGE_ID"),
//    GRAPHICS("GRAPHICS", "LISTEN", "TYPE"),
//    NIC("NIC", "NETWORK_ID"),
//    OS("OS", "ARCH", "BOOT");
//    
//    private String type;
//    private String child1;
//    private String child2;
//    private String child3;
//    TemplateTagsEnum(String type) {
//        this.type = type;
//    }
//
//    TemplateTagsEnum(String type, String child1) {
//        this.type = type;
//        this.child1 = child1;
//    }
//    
//    TemplateTagsEnum(String type, String child1, String child2) {
//        this.type = type;
//        this.child1 = child1;
//        this.child2 = child2;
//    }
//     TemplateTagsEnum(String type, String child1, String child2,String child3) {
//        this.type = type;
//        this.child1 = child1;
//        this.child2 = child2;
//        this.child3 = child3;
//    }
//    
//    public String getType(){
//        return type;
//    }
//     public String getFirstChild(){
//        return child1;
//    }
//      public String getSecondChild(){
//        return child2;
//    }
//      public String getThirdChild(){
//        return child3;
//    }
//    
//}
