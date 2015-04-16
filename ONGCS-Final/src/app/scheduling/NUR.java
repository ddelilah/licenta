package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.access.impl.RackDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class NUR {

	private static VMProcessor vmProcessor;
	private static RackProcessor rackProcessor;

	public NUR() {

	}

	public static Map<VirtualMachine, Server> placeVMsInNoneUnderutilizedRack(
			List<VirtualMachine> vmList, List<Rack> racks) {

		List<VirtualMachine> sortedVMs = new ArrayList<VirtualMachine>();
		rackProcessor = new RackProcessor(racks);
		List<Rack> nonUnderUtilizedRacks = rackProcessor
				.getNonUnderUtilizedRacks(racks);
		List<Rack> underUtilizedRacks = rackProcessor
				.getUnderUtilizedRacks(racks);
		List<Server> serversInNonUnderUtilizedRacks = new ArrayList<Server>();
		List<Server> serversInUnderUtilizedRacks = new ArrayList<Server>();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		
		Map<Server, List<Double>> resultOfOBFD = new HashMap<Server, List<Double>>();
		
		Server allocatedServer = new Server();

		for (Rack r : nonUnderUtilizedRacks) {
			for (Server s : r.getServers()) {
				serversInNonUnderUtilizedRacks.add(s);
			}

		}

		for (Rack r : underUtilizedRacks) {
			for (Server s : r.getServers()) {
				serversInUnderUtilizedRacks.add(s);
			}

		}

		vmProcessor = new VMProcessor(vmList);
		sortedVMs = vmProcessor.sortVMListDescending();
		for (VirtualMachine v : sortedVMs) {

			OBFD obfdNonUnderUtilized = new OBFD(serversInNonUnderUtilizedRacks);
			if (!obfdNonUnderUtilized.findAppropriateServer(v,allocation).isEmpty()) {
				resultOfOBFD = obfdNonUnderUtilized.findAppropriateServer(v,allocation);
				
				for(Entry<Server, List<Double>> entry : resultOfOBFD.entrySet()) {
					allocatedServer = entry.getKey();
					System.out.println("allocated server non under utilized serverId" + allocatedServer.getServerId() + "name:"+allocatedServer.getName());
					
				}
				//allocatedServer = (Server) obfdNonUnderUtilized
				//		.findAppropriateServer(v).get(0);

				if (allocatedServer == null) {
					OBFD obfdUnderUtilized = new OBFD(
							serversInUnderUtilizedRacks);
					if (!obfdUnderUtilized.findAppropriateServer(v,allocation).isEmpty()) {
						resultOfOBFD = obfdUnderUtilized.findAppropriateServer(v,allocation);
						
						for(Entry<Server, List<Double>> entry : resultOfOBFD.entrySet()) {
							allocatedServer = entry.getKey();
							System.out.println("allocated server under utilized serverId" + allocatedServer.getServerId() + "name:"+allocatedServer.getName());
							
						}

						if (allocatedServer != null) {
							allocation.put(v, allocatedServer);
						}
					}

				} else {
					allocation.put(v, allocatedServer);
				}
			}

		}

		return allocation;

	}

//	public static void main(String[] args) {
//		List<VirtualMachine> all = new ArrayList<VirtualMachine>();
//		List<Rack> racks = new ArrayList<Rack>();
//		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
//		all = vmDAO.getAllVMs();
//
//		RackDAOImpl rackDAO = new RackDAOImpl();
//		racks = rackDAO.getAllRacks();
//		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
//		allocation = placeVMsInNoneUnderutilizedRack(all, racks);
//
//	}
}
