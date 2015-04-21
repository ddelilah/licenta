package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.constants.RackState;
import app.constants.VMState;
import app.hibernate.SessionFactoryUtil;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class NUR {

	private static VMProcessor vmProcessor;
	private static RackProcessor rackProcessor;

	public static GenericDAOImpl dao = new GenericDAOImpl();

	private static VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
	private static RackDAOImpl rackDAO = new RackDAOImpl();

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
		List<Server> serversInOffRacks = new ArrayList<Server>();
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
				if (s.getUtilization() > 0) {
					serversInUnderUtilizedRacks.add(s);
				} else
					serversInOffRacks.add(s);
			}

		}

		vmProcessor = new VMProcessor(vmList);
		sortedVMs = vmProcessor.sortVMListDescending();
		for (VirtualMachine v : sortedVMs) {

			OBFD obfdNonUnderUtilized = new OBFD(serversInNonUnderUtilizedRacks);
			if (!obfdNonUnderUtilized.findAppropriateServer(v, allocation)
					.isEmpty()) {
				resultOfOBFD = obfdNonUnderUtilized.findAppropriateServer(v,
						allocation);

				for (Entry<Server, List<Double>> entry : resultOfOBFD
						.entrySet()) {
					allocatedServer = entry.getKey();
					System.out
							.println("[Allocated VM on a server from a non underutilized rack] VM "
									+ v.getVmId()
									+ " is allocated on server "
									+ allocatedServer.getServerId()
									+ " hosted by rack "
									+ allocatedServer.getRack().getRackId());
				}

				if (allocatedServer != null) {
					allocation.put(v, allocatedServer);
				}

			} else {
				// TODO: modify allocation
				OBFD obfdUnderUtilized = new OBFD(serversInUnderUtilizedRacks);
				if (!obfdUnderUtilized.findAppropriateServer(v, allocation)
						.isEmpty()) {
					resultOfOBFD = obfdUnderUtilized.findAppropriateServer(v,
							allocation);

					for (Entry<Server, List<Double>> entry : resultOfOBFD
							.entrySet()) {
						allocatedServer = entry.getKey();
						System.out
								.println("[Allocated VM on a server from an underutilized rack] VM "
										+ v.getVmId()
										+ " is allocated on server "
										+ allocatedServer.getServerId()
										+ " hosted by rack "
										+ allocatedServer.getRack().getRackId());
					}

					if (allocatedServer != null) {
						allocation.put(v, allocatedServer);
					}

				} else {
					// if all racks are off
					OBFD obfdOff = new OBFD(serversInOffRacks);
					if (!obfdOff.findAppropriateServer(v, allocation).isEmpty()) {
						resultOfOBFD = obfdOff.findAppropriateServer(v,
								allocation);

						for (Entry<Server, List<Double>> entry : resultOfOBFD
								.entrySet()) {
							allocatedServer = entry.getKey();
							System.out
									.println("[Allocated VM on a server from an off rack] VM "
											+ v.getVmId()
											+ " is allocated on server "
											+ allocatedServer.getServerId()
											+ " hosted by rack "
											+ allocatedServer.getRack()
													.getRackId());
							allocatedServer.getRack().setState(
									RackState.ON.getValue());
							rackDAO.mergeSessionsForRack(allocatedServer
									.getRack());
						}

						if (allocatedServer != null) {
							allocation.put(v, allocatedServer);
						}
					}

				}
			}

			if (!allocation.keySet().contains(v)) {
				System.out.println("[Allocation failed] VM " + v.getVmId()
						+ v.getState());
				v.setState(VMState.FAILED.getValue());
				vmDAO.mergeSessionsForVirtualMachine(v);
			}

		}

		return allocation;

	}

	public static void main(String[] args) {
		List<VirtualMachine> all = new ArrayList<VirtualMachine>();
		List<Rack> racks = new ArrayList<Rack>();
		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
		all = vmDAO.getAllVMs();

		RackDAOImpl rackDAO = new RackDAOImpl();
		racks = rackDAO.getAllRacks();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		allocation = placeVMsInNoneUnderutilizedRack(all, racks);

	}
}
