package app.policies;

import app.constants.PolicyType;
import app.model.Rack;
import app.model.Server;

public class RackPolicy extends Policy {

	private static float MIN_UTIL_THRESHOLD = 0.4f;
	private static float MAX_UTIL_THRESHOLD = 0.8f;
	private Rack rack;

	public RackPolicy(PolicyType policyType, boolean isViolated, Rack rack) {
		super(policyType, isViolated);
		this.rack = rack;
	}
	
	public boolean checkRackUtilizationViolation(float utilization) {
	
		if (utilization <= MAX_UTIL_THRESHOLD
				&& utilization >= MIN_UTIL_THRESHOLD) {
			isViolated = false;
		} else {
			isViolated = true;
		}
		
		return isViolated;
	}

	@Override
	public boolean evaluatePolicy() {
		float totalUtilizationFromServers = 0, totalRequestedUtilization = 0;

		for (Server s : rack.getServers()) {
			totalUtilizationFromServers += s.getUtilization();
		}
		
		totalRequestedUtilization = (float)totalUtilizationFromServers/rack.getServers().size();
		
		

		if (totalRequestedUtilization <= MAX_UTIL_THRESHOLD
				&& totalRequestedUtilization >= MIN_UTIL_THRESHOLD) {
			isViolated = false;
		} else {
			isViolated = true;
		}
		
		return isViolated;
	}

}
