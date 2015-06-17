package app.util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import app.model.*;

public class ServerProcessor implements Comparator<Server> {

	private List<Server> serverList;
		
	public ServerProcessor(List<Server> serverList){
		this.serverList=serverList;
	}
	
	public ServerProcessor() {
	}

	public List<Server> getServerList(){
		return serverList;
	}
	
	public void setServerList(List<Server> serverList){
		this.serverList=serverList;
	}

	@Override
	public int compare(Server r1, Server r2) {
		return r1.getUtilization() > r2.getUtilization() ? -1 : 
			r1.getUtilization() == r2.getUtilization()? 0 : 1;
	}
	
	public List sortServerListDescending(){
		
		Collections.sort(serverList, new ServerProcessor());
		return serverList;
	}
	
}