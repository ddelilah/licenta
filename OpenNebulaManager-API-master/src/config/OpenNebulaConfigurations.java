package config;

public class OpenNebulaConfigurations {
	public static final String LogPath = "Logs/Logs";
	public static final int GCL_RMI_API_PORT = 1333;
	public static final String NEBULA_RCP_ADDRESS = OpenNebulaConfigurationManager
			.getOpenNebulaRCPAddress();
	public static final String NEBULA_CREDENTIALS = OpenNebulaConfigurationManager
			.getOpenNebulaCredentials();
}
