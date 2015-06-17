package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import app.constants.ServerState;
import app.energy.CoolingSimulation;
import app.energy.Utilization;
import app.model.Server;
import app.model.VirtualMachine;
import app.util.SchedulingUtil;

public class PABFD {

	private List<Server> serverList;
	private SchedulingUtil schedulingUtil;
	private CoolingSimulation coolingSimulation;
	private static final float UNDERUTILIZED = 0.2f;
	/** Pmax = 1023W */
	private static final int MAXIMUM_POWER = 1023;
	/** fraction of power consumption of an idle server */
	private static final int K = 70;

	public PABFD(List<Server> serverList, String cracTemp) {
		this.serverList = serverList;

		coolingSimulation = new CoolingSimulation(Integer.parseInt(cracTemp));
		schedulingUtil = new SchedulingUtil();
	}

	public Server findAppropriateServerForConsolidationStep(VirtualMachine vm,
			Map<VirtualMachine, Server> allocation) {
		Server result = new Server();
		Utilization util = new Utilization();

		float minPower = Float.MAX_VALUE;
		float power = 0;
		float utilization;

		for (Server server : serverList) {
			if (schedulingUtil.enoughResources(server, vm, allocation)) {
				utilization = util.computePotentialUtilizationForAServer(
						server, vm, allocation);
				power = server.getIdleEnergy()
						+ (MAXIMUM_POWER - server.getIdleEnergy())
						* utilization;

				if (power < minPower) {
					result = server;
					break;
				}
			} else {
				// System.out.println("[ERROR 404: Server not found] Virtual machine"
				// + vm.getVmId() + vm.getName() + " can't be placed anywhere"
				// );
			}
		}

		/*
		 * 
		 * if no underutilized server can be found, leave the workload on the
		 * existing server
		 */

		return result;
	}

	/**
	 * returns the most appropriate server and the corresponding power
	 * consumption and cooling
	 */
	public Map<Server, List<Float>> findAppropriateServer(VirtualMachine vm,
			Map<VirtualMachine, Server> allocation) {
		float minPower = Float.MAX_VALUE;
		float cooling = 0;
		float power = 0;
		float utilization;
		Map<Server, List<Float>> returnValue = new HashMap<Server, List<Float>>();
		List<Float> correspondingValues = new ArrayList<Float>();
		List<Server> allocatedServers = new ArrayList<Server>();
		List<Server> emptyServers = new ArrayList<Server>();
		List<Server> underutilizedServers = new ArrayList<Server>();
		Utilization util = new Utilization();

		for (Server server : serverList) {
			/* if server is empty = server is OFF */
			if (server.getState().equalsIgnoreCase(ServerState.OFF.getValue()))
				emptyServers.add(server);

			else {
				/* underutilized server */
				if (server.getUtilization() < UNDERUTILIZED)
					underutilizedServers.add(server);

				else {
					/* non underutilized server */
					if (schedulingUtil.enoughResources(server, vm, allocation)) {
						utilization = util
								.computePotentialUtilizationForAServer(server,
										vm, allocation);
						power = server.getIdleEnergy()
								+ (MAXIMUM_POWER - server.getIdleEnergy())
								* utilization;

						if (power < minPower) {
							allocatedServers.add(server);
							minPower = power;
							cooling = power / coolingSimulation.getCOP();
						}
					}
				}
			}
		}

		if (allocatedServers.isEmpty()) {
			for (Server sUnderutilized : underutilizedServers) {
				if (schedulingUtil.enoughResources(sUnderutilized, vm,
						allocation)) {
					utilization = util.computePotentialUtilizationForAServer(
							sUnderutilized, vm, allocation);
					power = sUnderutilized.getIdleEnergy()
							+ (MAXIMUM_POWER - sUnderutilized.getIdleEnergy())
							* utilization;
					if (power < minPower) {
						allocatedServers.add(sUnderutilized);
						minPower = power;
						cooling = power / coolingSimulation.getCOP();
					}
				}
			}

			if (allocatedServers.isEmpty()) {
				for (Server sEmpty : emptyServers) {
					if (schedulingUtil.enoughResources(sEmpty, vm, allocation)) {
						utilization = util
								.computePotentialUtilizationForAServer(sEmpty,
										vm, allocation);
						power = sEmpty.getIdleEnergy()
								+ (MAXIMUM_POWER - sEmpty.getIdleEnergy())
								* utilization;
						if (power < minPower) {
							sEmpty.setState(ServerState.ON.getValue());
							allocatedServers.add(sEmpty);
							minPower = power;
							cooling = power / coolingSimulation.getCOP();
						}
					}
				}
			}

		}

		if (!allocatedServers.isEmpty()) {
			correspondingValues.add(power);
			correspondingValues.add(cooling);
			returnValue.put(allocatedServers.get(0), correspondingValues);
		} else {
			// System.out.println("[ERROR] No server has enough resources");
		}

		return returnValue;

	}
}