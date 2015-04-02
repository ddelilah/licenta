package app.policies;

import app.model.Rack;

public class RackPolicy extends Policy{

	private static int MIN_UTIL_THRESHOLD = 40;
	private static int MAX_UTIL_THRESHOLD = 80;
	private Rack rack;
	
	public RackPolicy(Rack rack){
		this.rack =rack;
	}

	public boolean evaluatePolicy() {
		if(rack.getUtilization() >= MIN_UTIL_THRESHOLD && rack.getUtilization() <= MAX_UTIL_THRESHOLD )
			/* rack utilization is within range */
				return false;
			return true;
	}

}
