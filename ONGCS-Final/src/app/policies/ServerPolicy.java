package app.policies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.constants.PolicyType;
import app.model.Server;
import app.model.VirtualMachine;

public class ServerPolicy extends Policy {
	
	private static final float MIN_SERVER_UTIL = 0.2f;
	private static final float MAX_SERVER_UTIL = 0.8f;

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

	public boolean checkServerUtilizationViolation(float utilization) {
		if (server.getState().equalsIgnoreCase("ON") && utilization <= MAX_SERVER_UTIL && utilization >= MIN_SERVER_UTIL) {
			isViolated = false;
		} else {
			isViolated = true;
		}
		
		return isViolated;
	}

	@Override
	public boolean evaluatePolicy() {
		float totalRequestedMips = 0, utilization = 0;
		
		for (VirtualMachine vm : server.getCorrespondingVMs()) {
			totalRequestedMips += vm.getVmMips();
		}
		utilization = totalRequestedMips / server.getServerMIPS();
		
		if (server.getState().equalsIgnoreCase("ON") && utilization <= MAX_SERVER_UTIL && utilization >= MIN_SERVER_UTIL) {
			isViolated = false;
		} else {
			isViolated = true;
		}
		
		return isViolated;
	
			
	}

}
