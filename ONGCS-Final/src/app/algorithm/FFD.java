package app.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.scheduling.*;
import app.access.RackDAO;
import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.constants.ServerState;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class FFD {
	
	private SchedulingUtil schedulingUtil = new SchedulingUtil();
	@SuppressWarnings("unchecked")
	public Map<VirtualMachine, Server> performFFD(List<VirtualMachine> vmList) {
	
		boolean foundServer = false;
		RackDAO rackDAO = new RackDAOImpl();
		List<Rack> allRacks  = new ArrayList<Rack>();
		List<Server> allServers  = new ArrayList<Server>();
		allRacks = rackDAO.getAllRacks();
		System.out.println(allRacks);
		VMProcessor vmProcessor= new VMProcessor(vmList); 
		vmList = vmProcessor.sortVMListDescending();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		
		for(VirtualMachine vm: vmList){
			 foundServer = false;
			System.out.println("Searching for vm: "+ vm.getName());
			
			for(Rack rack: allRacks){
				if(!foundServer){
		//			System.out.println("Searching through racks for vm: "+ vm.getVmId());
					allServers = rack.getServers();
								
						for(Server server: allServers){
	//						System.out.println("Searching through servers for vm: "+ vm.getVmId());
							if(schedulingUtil.enoughResources(server, vm, allocation)){
								System.out.println("Found server : "+ server.getServerId() +" on rack: "+rack.getRackId()+" for vm: "+vm.getVmId());
								allocation.put(vm, server);
								foundServer = true;
								break;
							}
					}
	
				}
			}
			
		}
	
		return allocation;
	}
	
	private static final int MAXIMUM_POWER = 1023;

	public void setServerPowerConsumption() {

		List<Rack> allRacks = new ArrayList<Rack>();
		List<Server> allServers = new ArrayList<Server>();
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		ServerDAOImpl serverDAO = new ServerDAOImpl();

		allServers = serverDAO.getAllServers();

		if (!allServers.isEmpty())
			for (Server server : allServers) {
					float utilization = server.getUtilization();
					float power = server.getIdleEnergy()
							+ (MAXIMUM_POWER - server.getIdleEnergy())
							* utilization;

					if(power!= server.getIdleEnergy()){
						server.setPowerValue(power);
						server.setState(ServerState.ON.getValue());
						genericDAO.updateInstance(server);
					}
					
			
			}
	}
	
	
}
