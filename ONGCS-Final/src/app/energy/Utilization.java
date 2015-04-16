package app.energy;

import java.util.ArrayList;
import java.util.List;

import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class Utilization {
	
public void setServerUtilization(){
		
		List<Rack> allRacks = new ArrayList<Rack>();
		List<Server> allServers = new ArrayList<Server>();
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		RackDAOImpl rackDAO = new RackDAOImpl();
		
		allRacks = rackDAO.getAllRacks();
		
		for(Rack rack: allRacks){
			allServers = rack.getServers();
			if(!allServers.isEmpty()){
				for(Server server: allServers){
					float utilization = computeUtilization(server);
					server.setUtilization(utilization);
					genericDAO.updateInstance(server);
				}
		}
	}
	}

	public float computeUtilization(Server server) {
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float sum = 0;
		for (VirtualMachine vm : vmList) {
			sum += vm.getVmMips();
		}
		return sum / server.getServerMIPS();

	}
}
