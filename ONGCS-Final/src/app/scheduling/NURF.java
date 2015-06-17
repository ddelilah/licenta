package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.constants.RackState;
import app.constants.VMState;
import app.energy.Utilization;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.util.RackProcessor;
import app.util.SchedulingUtil;
import app.util.VMProcessor;

public class NURF {

	private static VMProcessor vmProcessor;
	private static RackProcessor rackProcessor;
	private static VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
	private static RackDAOImpl rackDAO = new RackDAOImpl();

	private SchedulingUtil schedulingUtil = new SchedulingUtil();
	private List<Server> serverList = new ArrayList<Server>();

	private static List<Server> serversInNonUnderUtilizedRacks = new ArrayList<Server>();
	private static List<Server> serversInUnderUtilizedRacks = new ArrayList<Server>();
	private static List<Server> serversInOffRacks = new ArrayList<Server>();

	private static Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();

	private static String cracTemp;

	private static Utilization util = new Utilization();

	public NURF(String cracTemp) {
		this.cracTemp = cracTemp;
	}
	
	public static Map<VirtualMachine, Server> placeVMsInNoneUnderutilizedRack(
			List<VirtualMachine> vmList, List<Rack> racks) {

		List<VirtualMachine> sortedVMs = new ArrayList<VirtualMachine>();
		rackProcessor = new RackProcessor(racks);
		List<Rack> nonUnderUtilizedRacks = rackProcessor
				.getNonUnderUtilizedRacks(racks);
		List<Rack> underUtilizedRacks = rackProcessor
				.getUnderUtilizedRacks(racks);

		Map<Server, List<Float>> resultOfOBFD = new HashMap<Server, List<Float>>();

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

			PABFD obfdNonUnderUtilized = new PABFD(
					serversInNonUnderUtilizedRacks, cracTemp);
			if (!obfdNonUnderUtilized.findAppropriateServer(v, allocation)
					.isEmpty()) {
				resultOfOBFD = obfdNonUnderUtilized.findAppropriateServer(v,
						allocation);

				for (Entry<Server, List<Float>> entry : resultOfOBFD.entrySet()) {
					allocatedServer = entry.getKey();
					float potentialUtilization = util
							.computePotentialUtilizationForAServer(
									allocatedServer, v, allocation);
					System.out
							.println("[NON-UNDERUTILIZED SERVER'S POTENTIAL UTILIZATION]:"
									+ potentialUtilization);
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
					getServerRemainingResources(allocatedServer, allocation);
				}

			} else {
				// TODO: modify allocation
				PABFD obfdUnderUtilized = new PABFD(
						serversInUnderUtilizedRacks, cracTemp);
				if (!obfdUnderUtilized.findAppropriateServer(v, allocation)
						.isEmpty()) {
					resultOfOBFD = obfdUnderUtilized.findAppropriateServer(v,
							allocation);

					for (Entry<Server, List<Float>> entry : resultOfOBFD
							.entrySet()) {
						allocatedServer = entry.getKey();

						float potentialUtilization = util
								.computePotentialUtilizationForAServer(
										allocatedServer, v, allocation);
						System.out
								.println("[UNDERUTILIZED - SERVER'S POTENTIAL UTILIZATION]:"
										+ potentialUtilization);
						if (potentialUtilization > 0.2
								&& potentialUtilization < 0.8) {
							serversInNonUnderUtilizedRacks.add(allocatedServer);

							Iterator<Server> iterator = serversInUnderUtilizedRacks
									.iterator();
							while (iterator.hasNext()) {
								Server sr1 = iterator.next();
								if (sr1.getServerId() == allocatedServer
										.getServerId()) {
									iterator.remove();
								}
							}
						}
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
						getServerRemainingResources(allocatedServer, allocation);
					}

				} else {
					// if all racks are off
					PABFD obfdOff = new PABFD(serversInOffRacks, cracTemp);
					if (!obfdOff.findAppropriateServer(v, allocation).isEmpty()) {
						resultOfOBFD = obfdOff.findAppropriateServer(v,
								allocation);

						for (Entry<Server, List<Float>> entry : resultOfOBFD
								.entrySet()) {
							allocatedServer = entry.getKey();

							float potentialUtilization = util
									.computePotentialUtilizationForAServer(
											allocatedServer, v, allocation);
							System.out
									.println("[OFF SERVER'S POTENTIAL UTILIZATION]:"
											+ potentialUtilization);
							if (potentialUtilization > 0.2
									&& potentialUtilization < 0.8) {
								serversInNonUnderUtilizedRacks
										.add(allocatedServer);

								Iterator<Server> iterator = serversInOffRacks
										.iterator();
								while (iterator.hasNext()) {
									Server sr1 = iterator.next();
									if (sr1.getServerId() == allocatedServer
											.getServerId()) {
										iterator.remove();
									}
								}

							} else if (potentialUtilization != 0) {
								serversInUnderUtilizedRacks
										.add(allocatedServer);

								Iterator<Server> iterator = serversInOffRacks
										.iterator();
								while (iterator.hasNext()) {
									Server sr1 = iterator.next();
									if (sr1.getServerId() == allocatedServer
											.getServerId()) {
										iterator.remove();
									}
								}
							}
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
							getServerRemainingResources(allocatedServer,
									allocation);
						}
					}

				}
			}

			if (!allocation.keySet().contains(v)) {
				// System.out.println("[Allocation failed] VM " + v.getVmId()
				// + v.getState());
				v.setState(VMState.FAILED.getValue());
				vmDAO.mergeSessionsForVirtualMachine(v);
			}

		}

		return allocation;

	}


	@SuppressWarnings("unchecked")
	public Map<VirtualMachine, Server> placeVMsNURAfterFailed(
			List<VirtualMachine> vmList, List<Rack> racks) {

		rackProcessor = new RackProcessor(racks);
		vmProcessor = new VMProcessor(vmList);
		Map<Server, List<Float>> resultOfOBFD = new HashMap<Server, List<Float>>();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();

		Server allocatedServer = new Server();
		racks = rackProcessor.sortRackListDescending();
		vmList = vmProcessor.sortVMListDescending();
		Rack rack = new Rack();

		for (VirtualMachine vm : vmList) {

			if (selectSuitableRack(racks, vm, allocation) != null) {
				rack = selectSuitableRack(racks, vm, allocation);

				serverList = rack.getServers();

				allocatedServer = null;
				PABFD obfd = new PABFD(serverList, cracTemp);
				if (!obfd.findAppropriateServer(vm, allocation).isEmpty()) {
					resultOfOBFD = obfd.findAppropriateServer(vm, allocation);

					for (Entry<Server, List<Float>> entry : resultOfOBFD
							.entrySet()) {
						allocatedServer = entry.getKey();
					}

				}

				if (allocatedServer != null) {
					allocation.put(vm, allocatedServer);
					System.out.println("Virtual machine " + vm.getVmId() + " "
							+ vm.getName() + " allocated to server "
							+ allocatedServer);
					getServerRemainingResources(allocatedServer, allocation);
				} else {
					System.out.println("Allocation failed " + vm.getName()
							+ vm.getVmId() + vm.getState());
					vm.setState(VMState.FAILED.getValue());
					vmDAO.updateInstance(vm);
				}

			} else {
				System.out.println("Allocation failed " + vm.getName()
						+ vm.getVmId() + vm.getState());
				vm.setState(VMState.FAILED.getValue());
				vmDAO.updateInstance(vm);
			}
		}
		return allocation;
	}

	public Rack selectSuitableRack(List<Rack> rackList, VirtualMachine vm,
			Map<VirtualMachine, Server> allocation) {
		List<Server> serverList = new ArrayList<Server>();

		for (Rack rack : rackList) {
			serverList = rack.getServers();
			for (Server server : serverList)
				if (schedulingUtil.enoughResources(server, vm, allocation))
					return rack;
		}
		return null;
	}

	public static void getServerRemainingResources(Server server,
			Map<VirtualMachine, Server> allocation) {

		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		int remainingMIPS = server.getServerMIPS();
		int remainingCores = server.getCpu().getNr_cores();
		float remainingHDD = server.getHdd().getCapacity();
		float remainingRam = server.getRam().getCapacity();
		for (VirtualMachine vm : vmList) {
			remainingMIPS -= vm.getVmMips();
			remainingCores -= vm.getCpu().getNr_cores();
			remainingHDD -= vm.getHdd().getCapacity();
			remainingRam -= vm.getRam().getCapacity();
		}
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			if (entry.getValue().getServerId() == server.getServerId())
				remainingMIPS -= entry.getKey().getVmMips();
			remainingCores -= entry.getKey().getCpu().getNr_cores();
			remainingHDD -= entry.getKey().getHdd().getCapacity();
			remainingRam -= entry.getKey().getRam().getCapacity();
		}
	}

}
