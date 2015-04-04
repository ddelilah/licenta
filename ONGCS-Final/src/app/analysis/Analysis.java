package app.analysis;

import java.util.ArrayList;
import java.util.List;

import app.constants.PolicyType;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.policies.RackPolicy;
import app.policies.ServerPolicy;
import app.policies.VmPolicy;

public class Analysis {

	/**
	 * Restrictive threshold (lowest entropy value-whenever a policy-imposed
	 * restriction is broken, the reinforced-learning algorithm is started
	 */
	private static final float THRESHOLD = 0;
	private static float entropy;
	private List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
	private List<Server> serverList = new ArrayList<Server>();
	private List<Rack> rackList = new ArrayList<Rack>();

	private PolicyType p;

	public Analysis(List<VirtualMachine> vmList, List<Server> serverList,
			List<Rack> rackList) {
		this.vmList = vmList;
		this.serverList = serverList;
		this.rackList = rackList;
	}

	public Analysis() {

	}

	public void performAnalysis() {
		/* evaluate all policies */

		switch (p) {
		case VM_POLICY:
			/** If there are undeployed VMs learning algorithm should be started */
			for (VirtualMachine vm : vmList) {
				VmPolicy vmPolicy = new VmPolicy(p.VM_POLICY, false, vm);
				if (vmPolicy.evaluatePolicy() == true)
					checkPlanning((float) 1);
			}
			break;

		case SERVER_POLICY:
			for (Server server : serverList) {
				ServerPolicy serverPolicy = new ServerPolicy(p.SERVER_POLICY,
						false, server);
				if (serverPolicy.evaluatePolicy() == true)
					checkPlanning((float) 1);

			}
			break;

		case RACK_POLICY:
			for (Rack rack : rackList) {
				RackPolicy rackPolicy = new RackPolicy(p.RACK_POLICY, false,
						rack);
				if (rackPolicy.evaluatePolicy() == true)
					checkPlanning((float) 1);
			}

			break;
		}

	}

	public void checkPlanning(Float entropy) {

		if (entropy > THRESHOLD) {
			System.out.println("Starting Learning Algorithm");
		} else {
			System.out.println("System is Optimal");
		}
	}

}
