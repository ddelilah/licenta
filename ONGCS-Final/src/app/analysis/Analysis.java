package app.analysis;

import java.util.ArrayList;
import java.util.List;

import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.policies.RackPolicy;
import app.policies.ServerPolicy;
import app.policies.VmPolicy;

public class Analysis {

	/**
	 *  Restrictive threshold (lowest entropy value-whenever a policy-imposed restriction 
	 *  is broken, the reinforced-learning algorithm is started
	 */
	private static final float THRESHOLD = 0;
	private static float entropy;
	private  List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
	private  List<Server> serverList = new ArrayList<Server>();
	private  List<Rack> rackList = new ArrayList<Rack>();
	
	public Analysis(List<VirtualMachine> vmList, List<Server> serverList, List<Rack> rackList){
		this.vmList = vmList;
		this.serverList = serverList;
		this.rackList = rackList;
	}
	public Analysis(){}
	public void performAnalysis(){
		/*evaluate all policies */
	
		/** If there are undeployed VMs learning algorithm should be started */
		for(VirtualMachine vm : vmList){
			VmPolicy vmPolicy = new VmPolicy(vm);
			if(vmPolicy.evaluatePolicy() == true)
				checkPlanning((float)1);
		}
		
		for(Server server : serverList){
			ServerPolicy serverPolicy = new ServerPolicy(server);
			if(serverPolicy.evaluatePolicy() == true)
				checkPlanning((float)1);
			else
				// entropy = weight * violation (weight is a constant)
				entropy = serverPolicy.computeViolation();
		}
		for(Rack rack : rackList){
			RackPolicy rackPolicy = new RackPolicy(rack);
			if(rackPolicy.evaluatePolicy() == true)
				checkPlanning((float)1);
		}
	
		checkPlanning(entropy);
	}
	
	
	public void checkPlanning(Float entropy){
		
		if (entropy > THRESHOLD) {
			System.out.println("Starting Learning Algorithm");
		} else {
			System.out.println("System is Optimal");
		}
	}

	
}
