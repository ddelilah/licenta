package models;

public class VirtualNetworkON {
	private int id;
	private String ip;
	private Integer vncPort;
	private String vncPassword;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getVncPort() {
		return vncPort;
	}

	public void setVncPort(Integer vncPort) {
		this.vncPort = vncPort;
	}

	public String getVncPassword() {
		return vncPassword;
	}

	public void setVncPassword(String vncPassword) {
		this.vncPassword = vncPassword;
	}

}