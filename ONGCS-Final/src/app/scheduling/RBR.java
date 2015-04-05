package app.scheduling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class RBR implements Serializable {

	private List<Rack> rackList= new ArrayList<Rack>();
	private List<VirtualMachine> vmList= new ArrayList<VirtualMachine>();
	private List<Server> serverList = new ArrayList<Server>();
	private VMProcessor vmProcessor= new VMProcessor(); 
	private RackProcessor rackProcessor = new RackProcessor();
	private SchedulingUtil schedulingUtil= new SchedulingUtil();

	public RBR(List<Rack> rackList, List<VirtualMachine> vmList) {
		this.rackList = rackList;
		this.vmList = vmList;

		serverList = new ArrayList<Server>();

		vmProcessor = new VMProcessor(vmList);
		rackProcessor = new RackProcessor(rackList);
		
		schedulingUtil = new SchedulingUtil();

	}
	public RBR(){}

	@SuppressWarnings("unchecked")
	public Map<VirtualMachine, Server> placeVMsRackByRack(List<VirtualMachine> vmList, List<Rack> rackList) {
		
		//Map<Server, VirtualMachine> power = new HashMap<Server, VirtualMachine>();
		Map<Server, List<Double>> resultOfOBFD = new HashMap<Server, List<Double>>();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();

		Server allocatedServer = new Server();
		rackList = rackProcessor.sortRackListDescending();
		vmList = vmProcessor.sortVMListDescending();
		Rack rack = new Rack();

		for (VirtualMachine vm : vmList) {
			rack = selectSuitableRack(rackList, vm);

			serverList = rack.getServers();
			// for(Server s: serverList){
			/* find appropriate sever */

			OBFD obfd = new OBFD(serverList);
			if (!obfd.findAppropriateServer(vm).isEmpty()) {
				resultOfOBFD = obfd.findAppropriateServer(vm);

				for (Entry<Server, List<Double>> entry : resultOfOBFD
						.entrySet()) {
					allocatedServer = entry.getKey();
				}

			}

			if (allocatedServer != null) {
				allocation.put(vm, allocatedServer);

			}

		}
		
		return allocation;
	}

	public Rack selectSuitableRack(List<Rack> rackList, VirtualMachine vm) {
		List<Server> serverList = new ArrayList<Server>();

		for (Rack rack : rackList) {
			serverList = rack.getServers();
			for (Server server : serverList)
				if (schedulingUtil.enoughResources(server, vm))
					return rack;
		}
		return null;
	}

	// public boolean enoughResources(Server server, VirtualMachine vmToCheck){
	// List<VirtualMachine> vmList = server.getCorrespondingVMs();
	// float totalNbCores =0;
	// float totalCapacityRam=0;
	// float totalCapacityHdd=0;
	//
	// for(VirtualMachine vm : vmList){
	// totalNbCores += vm.getCpu().getNr_cores();
	// totalCapacityRam += vm.getRam().getCapacity();
	// totalCapacityHdd += vm.getHdd().getCapacity();
	// }
	// if( server.getCpu().getNr_cores() - totalNbCores >
	// vmToCheck.getCpu().getNr_cores()
	// && server.getRam().getCapacity() - totalCapacityRam >
	// vmToCheck.getRam().getCapacity()
	// && server.getHdd().getCapacity() -totalCapacityHdd >
	// vmToCheck.getHdd().getCapacity() )
	// return true;
	//
	// return false;
	// }

}
