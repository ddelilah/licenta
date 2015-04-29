package app.policies;

import app.constants.PolicyType;
import app.model.Rack;
import app.model.Server;

public class RackPolicy extends Policy {

	private static int MIN_UTIL_THRESHOLD = 40;
	private static int MAX_UTIL_THRESHOLD = 80;
	private Rack rack;

	public RackPolicy(PolicyType policyType, boolean isViolated, Rack rack) {
		super(policyType, isViolated);
		this.rack = rack;
	}

	@Override
	public boolean evaluatePolicy() {
		float totalRequestedUtilization = 0;

		for (Server s : rack.getServers()) {
			totalRequestedUtilization += s.getUtilization();
		}

		if (totalRequestedUtilization < MAX_UTIL_THRESHOLD
				&& totalRequestedUtilization > MIN_UTIL_THRESHOLD) {
			isViolated = false;
		} else {
			isViolated = true;
		}
		
		return isViolated;
	}

}
