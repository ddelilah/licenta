///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package intercloudmigration.models;
//
//import java.io.Serializable;
//
///**
// *
// * @author oneadmin
// */
//public class Image implements Serializable {
//
//    /**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private int _imageId;
//    private ImageType _imageType;
//    private String imagePath;
//    private boolean isPublic;
//    private String description;
//    private String name;
//
//    public Image(int _imageId, ImageType _imageType) {
//        this._imageId = _imageId;
//        this._imageType = _imageType;
//    }
//
//    public Image(){}
//    public int getImageId() {
//        return _imageId;
//    }
//
//    public ImageType getImageType() {
//        return _imageType;
//    }
//
//    public void setImageId(int _imageId) {
//        this._imageId = _imageId;
//    }
//
//    public void setImageType(ImageType _imageType) {
//        this._imageType = _imageType;
//    }
//
//    public String getImagePath() {
//        return imagePath;
//    }
//
//    public void setImagePath(String imagePath) {
//        this.imagePath = imagePath;
//    }
//
//    public boolean isPublic() {
//        return isPublic;
//    }
//
//    public void setIsPublic(boolean isPublic) {
//        this.isPublic = isPublic;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public String toString() {
//        String p = "NO";
//        if (isPublic) {
//            p = "YES";
//        }
//
//        return "NAME = " +'"' + name + '"' + "\n"
//                + "PATH = " + imagePath+"\n"
//                + "PUBLIC = " + p +"\n"
//                + "PERSISTENT = " + "NO" + "\n"
//                + "TYPE = " + "OS"+ "\n" 
//                + "DATASTORE = " + 108 + "\n"
//                + "DESCRIPTION = " + '"'+ description+'"';
//               
//    }
//
//}
