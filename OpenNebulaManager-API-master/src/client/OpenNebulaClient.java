/**
 * 
 */
package client;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.opennebula.client.Client;
import org.opennebula.client.ClientConfigurationException;
import config.OpenNebulaConfigurations;

/**
 * @author oneadmin
 *
 */
public class OpenNebulaClient {

	 private static Client _instance;
	    
	    public static Client getInstance(){
	        if (_instance == null) {
	            try {
	                _instance = new Client(
	                		OpenNebulaConfigurations.NEBULA_CREDENTIALS,
	                		OpenNebulaConfigurations.NEBULA_RCP_ADDRESS);
	            } catch (ClientConfigurationException ex) {
	                Logger.getLogger(OpenNebulaClient.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        }
	        return _instance;
	    }
	}

