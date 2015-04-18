package app.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.scheduling.*;
import app.access.RackDAO;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class FFD {
	

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
	//		System.out.println("Searching for vm: "+ vm.getVmId());
			
			for(Rack rack: allRacks){
				if(!foundServer){
		//			System.out.println("Searching through racks for vm: "+ vm.getVmId());
					allServers = rack.getServers();
								
						for(Server server: allServers){
	//						System.out.println("Searching through servers for vm: "+ vm.getVmId());
							if(enoughResources(server, vm, allocation)){
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

	public boolean enoughResources(Server server, VirtualMachine vm, Map<VirtualMachine, Server> map){
		
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float totalMips = server.getServerMIPS();
		float ramCapacity = server.getRam().getCapacity();
		float hddCapacity = server.getHdd().getCapacity();
		
		for (VirtualMachine virtualMachine : vmList) {
			totalMips -= virtualMachine.getVmMips();
			ramCapacity -= virtualMachine.getRam().getCapacity();
			hddCapacity -= virtualMachine.getHdd().getCapacity();
			if(hddCapacity < 0 || ramCapacity <0 || totalMips <0)
				return false;
		}
//		System.out.println("Server with id: "+server.getServerId()+"has cores: "+cpuCores+" ram: "+ramCapacity+" hdd: "+hddCapacity);
		for (Entry<VirtualMachine, Server> entry : map.entrySet()) 
			if (server.getServerId() == entry.getValue().getServerId()){
				totalMips -= entry.getKey().getVmMips();
				ramCapacity -= entry.getKey().getRam().getCapacity();
				hddCapacity -= entry.getKey().getHdd().getCapacity();
			}
		if(hddCapacity < 0 || ramCapacity <0 || totalMips <0)
			return false;
	//	System.out.println("Server with id: "+server.getServerId()+"has cores: "+cpuCores+" ram: "+ramCapacity+" hdd: "+hddCapacity);

		if(totalMips - vm.getVmMips() >= 0 &&
				ramCapacity - vm.getRam().getCapacity() >=0 &&
				hddCapacity - vm.getHdd().getCapacity() >=0)
			return true;
		
		return false;
	}
	
	
}
