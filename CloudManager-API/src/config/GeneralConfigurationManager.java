package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class GeneralConfigurationManager {

    private static Properties generalProperties;
    private static String path = "/var/lib/one/workspace/CloudManager-API/src/config/config.properties";

    static {
        generalProperties = new Properties();
        try {
            generalProperties.load(new FileInputStream(path));
        } catch (IOException e) {
            System.out.println("Unable to load CONFIG_PATH file");
            System.exit(1);
        }
    }

    public static String getNodesWakeUpMechanism() {
        return generalProperties.getProperty("nodesWakeUpMechanism");
    }
    
    public static String getPingLocation() {
        return generalProperties.getProperty("pingLocation");
    }
    public static String getArpLocation() {
        return generalProperties.getProperty("arpLocation");
    }
    public static String getARPTableFileLocation() {
        return generalProperties.getProperty("arpTableFileLocation");
    }


}
