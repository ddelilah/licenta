package app.policies;

import app.model.Server;

public class ServerPolicy {

	/**
	 * When cpu utilization is 70% and storage usage(disk utilization) is 50%
	 * the server has the lowest energy consumption
	 * */
	private static final int THRESHOLD = 80;
	private static final int OPTIMAL_CPU_UTIL = 70;
	private static final int OPTIMAL_STORAGE_UTIL = 50;
	private static final int MIN_SERVER_UTIL = 20;
	private static final int MAX_SERVER_UTIL = 80;

	private Server server;
	
	public ServerPolicy(Server server){
		this.server=server;
	}
	
	public boolean evaluatePolicy() {
		/*check if we have underutilized or over-utilized servers*/
		if(server.getUtilization() >= MIN_SERVER_UTIL && server.getUtilization() <= MAX_SERVER_UTIL )
		/* server utilization is within range */
			return false;
		return true;
	}
	
	
	
	public float computeViolation(){
		double disk = Math.abs(OPTIMAL_STORAGE_UTIL - server.getHdd().getCapacity());
		double cpu = Math.abs(OPTIMAL_CPU_UTIL - server.getCpu().getFrequency());
	
		return (float) Math.sqrt(Math.pow(disk, 2) + Math.pow(cpu, 2));
		
	}
	
}
