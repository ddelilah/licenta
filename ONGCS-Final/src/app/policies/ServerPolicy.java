package app.policies;

import app.constants.PolicyType;
import app.model.Server;
import app.model.VirtualMachine;

public class ServerPolicy extends Policy {

	/**
	 * When cpu utilization is 70% and storage usage(disk utilization) is 50%
	 * the server has the lowest energy consumption
	 * */
	/*
	 * private static final int THRESHOLD = 80;
	 * private static final int OPTIMAL_CPU_UTIL = 70; 
	 * private static final int OPTIMAL_STORAGE_UTIL 50;
	 */
	
	private static final double MIN_SERVER_UTIL = 0.2;
	private static final double MAX_SERVER_UTIL = 0.8;

	private Server server;

	public ServerPolicy(PolicyType policyType, boolean isViolated, Server server) {
		super(policyType, isViolated);
		this.server = server;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	@Override
	public boolean evaluatePolicy() {
		double totalRequestedMips = 0;
		
		for (VirtualMachine vm : server.getCorrespondingVMs()) {
			totalRequestedMips += vm.getVmMips();
		}
		double utilization = totalRequestedMips / server.getServerMIPS();
		if (server.getState().equalsIgnoreCase("ON") && utilization < MAX_SERVER_UTIL && utilization > MIN_SERVER_UTIL) {
			isViolated = false;
		} else {
			isViolated = true;
		}
		
		return isViolated;
			
	}

}
