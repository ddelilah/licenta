//package factory;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.Properties;
//
//public class Configurations {
//	private static Properties generalProperties;
//	private static String path = "/var/lib/one/workspace/CloudManagerFactory/src/factory/config.properties";
//
//    static {
//        generalProperties = new Properties();
//        try {
//            generalProperties.load(new FileInputStream(path));
//        } catch (IOException e) {
//            System.out.println("Unable to load CONFIG_PATH file");
//            System.exit(1);
//        }
//    }
//    
//    public static String getManager() {
//        return generalProperties.getProperty("manager");
//    }
//}
