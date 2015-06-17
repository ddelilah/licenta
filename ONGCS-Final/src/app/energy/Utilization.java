package app.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.access.ServerDAO;
import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.constants.RackState;
import app.constants.ServerState;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class Utilization {

	public void setSingleServerUtilization(Server s) {
		ServerDAOImpl serverDAO = new ServerDAOImpl();
		float utilization = computeUtilization(s);
		
		if(utilization == 0) {
			s.setState(ServerState.OFF.getValue());
		}
		s.setUtilization(utilization);
		serverDAO.mergeSessionsForServer(s);
	}

	public float computeUtilization(Server server) {
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float sum = 0;
		
		for (VirtualMachine vm : vmList) {
			sum += vm.getVmMips();
		}
		return sum / server.getServerMIPS();

	}
	
	public float computePotentialUtilizationForAServer(Server server, VirtualMachine vmToCheck, Map<VirtualMachine, Server> map) {
		float potentialUtilization = 0, totalRequiredMips = 0;
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		
		for (VirtualMachine vm : vmList) {
			totalRequiredMips += vm.getVmMips();
		}
		
		if(!map.isEmpty()) {
			for (Entry<VirtualMachine, Server> entry : map.entrySet()) {
				if(entry.getValue() != null)
				if (server.getServerId() == entry.getValue().getServerId()) {
					totalRequiredMips += entry.getKey().getVmMips();
				}
			}
		}
			
			potentialUtilization = (totalRequiredMips + vmToCheck.getVmMips()) / server.getServerMIPS();

		return potentialUtilization;
	}
	
	
	public float computePotentialUtilizationForARack(Rack r, VirtualMachine vmToCheck, Map<VirtualMachine, Server> map) {
		List<Server> serversOnRack = r.getServers();
		int nrOfServers = serversOnRack.size();
		float rackPotentialUtilization = 0;
		for(Server s: serversOnRack) {
			rackPotentialUtilization += computePotentialUtilizationForAServer(s, vmToCheck, map);
		}
		
		return (float) rackPotentialUtilization/nrOfServers;
	}
	
	public float computeSingleRackUtilization(Rack r) {

		List<Server> allServers = new ArrayList<Server>();
		float utilization = 0;
		
		allServers = r.getServers();
		
		for(Server server: allServers) {
			utilization += server.getUtilization();
		}
	
		return utilization/allServers.size();
		
		
	}
	
	public void setSingleRackUtilization(Rack r) {
		RackDAOImpl rackDAO = new RackDAOImpl();
		float utilization = computeSingleRackUtilization(r);
		if(utilization == 0) {
			r.setState(RackState.OFF.getValue());
		}
		r.setUtilization(utilization);
		rackDAO.mergeSessionsForRack(r);

	}

}
