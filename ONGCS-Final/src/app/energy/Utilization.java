package app.energy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class Utilization {
	
	private static final int MAXIMUM_POWER = 500;
	
public void setServerUtilization() {
		
		List<Server> allServers = new ArrayList<Server>();
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		ServerDAOImpl serverDAO = new ServerDAOImpl();
		
		allServers = serverDAO.getAllServers();
		
			if(!allServers.isEmpty()) {
				for(Server server: allServers) {
					float utilization = computeUtilization(server);
					server.setUtilization(utilization);
					genericDAO.updateInstance(server);
				}
		}
	}

	public float computeUtilization(Server server) {
		System.out.println("Server for which we compute the utilization: " + server.getServerId());
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float sum = 0;
		for (VirtualMachine vm : vmList) {
			sum += vm.getVmMips();
		}
		return sum / server.getServerMIPS();

	}
	
	public float computePotentialUtilizationForAServer(Server server, VirtualMachine vmToCheck, Map<VirtualMachine, Server> map) {
		float potentialUtilization = 0;
		float totalRequiredMips = 0;
		
		if(!map.isEmpty())
			for (Entry<VirtualMachine, Server> entry : map.entrySet()) {
				if(entry.getValue() != null)
				if (server.getServerId() == entry.getValue().getServerId()) {
					totalRequiredMips += entry.getKey().getVmMips();
				}
			}
			
			potentialUtilization = (totalRequiredMips + vmToCheck.getVmMips()) / server.getServerMIPS();

		return potentialUtilization;
	}
	
	public float computeSingleRackUtilization(Rack r) {
		
		float maxRackPowerConsumption;
		float currentPowerConsumption=0;
		int maximumServerPowerConsumption=500;
		
		List<Server> allServers = new ArrayList<Server>();
		
		allServers = r.getServers();
		maxRackPowerConsumption =(float) allServers.size() * maximumServerPowerConsumption;
		
		for(Server server: allServers) {
			currentPowerConsumption += server.getPowerValue();
		}
		
		float utilization = currentPowerConsumption / maxRackPowerConsumption;
		
		return utilization;
		
		
	}
	
public void setRackUtilization() {
		
		float maxRackPowerConsumption;
		float currentPowerConsumption=0;
		
		List<Rack> allRacks = new ArrayList<Rack>();
		List<Server> allServers = new ArrayList<Server>();
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		RackDAOImpl rackDAO = new RackDAOImpl();
		
		allRacks = rackDAO.getAllRacks();
				
		System.out.println("\n\n\n\n"+allRacks);
		for(Rack rack: allRacks) {
			
			currentPowerConsumption = 0;
			allServers = rack.getServers();
			System.out.println("\n Rack"+rack + "\n" +allServers);
			maxRackPowerConsumption =(float) allServers.size() * MAXIMUM_POWER;
			
			if(!allServers.isEmpty()) {
				for(Server server: allServers) {
					currentPowerConsumption += server.getPowerValue();
				}
				
				float utilization = currentPowerConsumption / maxRackPowerConsumption;
				
				rack.setUtilization(utilization);
				genericDAO.updateInstance(rack);
			}
			else {
				rack.setUtilization(0);
				genericDAO.updateInstance(rack);
			}
		}
		
		
		
	}

}
