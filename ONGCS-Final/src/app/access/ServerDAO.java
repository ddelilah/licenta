package app.access;

import java.util.List;

import app.model.Server;

public interface ServerDAO {
	
	public List<Server> getAllServers();
	
	public Server getServerById(int serverId);

	public List<Server> getAllServersByState(String state);
	
	public String mergeSessionsForServer(Server s);
}
