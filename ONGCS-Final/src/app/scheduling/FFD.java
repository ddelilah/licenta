package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import app.scheduling.*;
import app.util.SchedulingUtil;
import app.util.VMProcessor;
import app.access.RackDAO;
import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.constants.ServerState;
import app.constants.VMState;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class FFD {

	private SchedulingUtil schedulingUtil = new SchedulingUtil();
	private static VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();

	@SuppressWarnings("unchecked")
	public Map<VirtualMachine, Server> performFFD(List<VirtualMachine> vmList) {

		boolean foundServer = false;
		RackDAO rackDAO = new RackDAOImpl();
		List<Rack> allRacks = new ArrayList<Rack>();
		List<Server> allServers = new ArrayList<Server>();
		allRacks = rackDAO.getAllRacks();
		VMProcessor vmProcessor = new VMProcessor(vmList);
		vmList = vmProcessor.sortVMListDescending();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();

		for (VirtualMachine vm : vmList) {
			foundServer = false;

			for (Rack rack : allRacks) {
				if (!foundServer) {
					allServers = rack.getServers();

					for (Server server : allServers) {
						if (schedulingUtil.enoughResources(server, vm,
								allocation)) {
							allocation.put(vm, server);
							foundServer = true;
							break;
						}
					}

				}
			}

			if (!allocation.keySet().contains(vm)) {
				vm.setState(VMState.FAILED.getValue());
				vmDAO.mergeSessionsForVirtualMachine(vm);
			}

		}

		return allocation;
	}

}
