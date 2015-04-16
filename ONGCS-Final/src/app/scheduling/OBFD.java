package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.constants.ServerState;
import app.model.*;

public class OBFD {

	private List<Server> serverList;
	private SchedulingUtil schedulingUtil;
	
	private static final int UNDERUTILIZED = 20;
	/** Pmax = 500W */
	private static final int MAXIMUM_POWER = 500;
	/** fraction of power consumption of an idle server */
	private static final int K = 70;
	/* suppose temperature is 25 degrees */
	private static final float COP = (float) (0.0068 * Math.pow(25, 2) + 0.0008 * 25 + 0.458);

	public OBFD(List<Server> serverList) {
		this.serverList = serverList;
		
		schedulingUtil = new SchedulingUtil();
	}

	/** returns the most appropriate server and the corresponding power consumption and cooling */
	public Map<Server, List<Double>> findAppropriateServer(VirtualMachine vm,Map<VirtualMachine, Server> allocation) {
		double minPower = Float.MAX_VALUE;
		double cooling = 0;
		double power = 0;
		double utilization;
		Map<Server, List<Double>> returnValue = new HashMap<Server, List<Double>>();
		List<Double> correspondingValues = new ArrayList<Double>();
		List<Server> allocatedServers = new ArrayList<Server>();
		List<Server> emptyServers = new ArrayList<Server>();
		List<Server> underutilizedServers = new ArrayList<Server>();

		for (Server server : serverList) {
			/* if server is empty = server is OFF */
			if (server.getState().equalsIgnoreCase(ServerState.OFF.getValue()))
				emptyServers.add(server);

			else {
				/* underutilized server */
				if (server.getUtilization() < UNDERUTILIZED)
					underutilizedServers.add(server);

				else {
					/** if server has enough resources */
					if (schedulingUtil.enoughResources(server, vm,allocation)) {
						// float power = K*MAXIMUM_POWER + (1-K)*MAXIMUM_POWER*
						// server.getUtilization();
						// float power =
						// server.getCpu().getCpu_utilization()*MAXIMUM_POWER+server.getIdleEnergy();

						/*** !!!!!!!!!!!!!!!!!! 
						 *  take into account the previously scheduled vms when computing utilization ????????
						 *  ????????????????????????????
						 *  *********/
						utilization = computeUtilization(server);
						power = server.getIdleEnergy()
								+ (MAXIMUM_POWER - server.getIdleEnergy())
								* utilization;

						if (power < minPower) {
							allocatedServers.add(server);
							minPower = power;
							cooling = power / COP;
						}
					}
				}
			}
		}
		
		if (allocatedServers.isEmpty()) {
			for (Server sUnderutilized : underutilizedServers) {
				if (schedulingUtil.enoughResources(sUnderutilized, vm,allocation)) {
					utilization = computeUtilization(sUnderutilized);
					power = sUnderutilized.getIdleEnergy()
							+ (MAXIMUM_POWER - sUnderutilized.getIdleEnergy())
							* utilization;
					if (power < minPower) {
						allocatedServers.add(sUnderutilized);
						minPower = power;
						cooling = power / COP;
					}
				}
			}

			if (allocatedServers.isEmpty()) {
				for (Server sEmpty : emptyServers) {
					if (schedulingUtil.enoughResources(sEmpty, vm,allocation)) {
						utilization = computeUtilization(sEmpty);
						power = sEmpty.getIdleEnergy()
								+ (MAXIMUM_POWER - sEmpty.getIdleEnergy())
								* utilization;
						if (power < minPower) {
							sEmpty.setState(ServerState.ON.getValue());
							allocatedServers.add(sEmpty);
							minPower = power;
							cooling = power / COP;
						}
					}
				}
			}
		}

		if(!allocatedServers.isEmpty()) {
			correspondingValues.add(power);
			correspondingValues.add(cooling);
			returnValue.put(allocatedServers.get(0), correspondingValues);
		}
		
		return returnValue;

	}



	public float computeUtilization(Server server) {
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float sum = 0;
		for (VirtualMachine vm : vmList) {
			sum += vm.getVmMips();
		}
		return sum / server.getServerMIPS();

	}

}