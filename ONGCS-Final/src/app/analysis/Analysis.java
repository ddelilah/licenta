package app.analysis;

import java.util.ArrayList;
import java.util.List;

import app.GUI.ChartAirflow;
import app.GUI.Charts;
import app.access.*;
import app.access.impl.*;
import app.constants.PolicyType;
import app.energy.Utilization;
import app.execution.Execution;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.policies.Policy;
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
		this.serverList = serverList;
		this.rackList = rackList;
	}

	public Analysis() {

		VirtualMachineDAO vmDao = new VirtualMachineDAOImpl();
		ServerDAO serverDao = new ServerDAOImpl();
		RackDAO rackDao = new RackDAOImpl();
		
	//	this.vmList = vmDao.getAllVMs();
		this.serverList= serverDao.getAllServers();
		this.rackList = rackDao.getAllRacks();
		
		
	}

	public void performAnalysis(List<VirtualMachine> vmList, String algorithm, Charts chart, ChartAirflow chartAirflow) {
		/* evaluate all policies */
		boolean isViolated = false;
		boolean shouldStartScheduler = false;
		
			/** If there are undeployed VMs learning algorithm should be started */
			for (VirtualMachine vm : vmList) {
				VmPolicy vmPolicy = new VmPolicy(p.VM_POLICY, false, vm);
				if (vmPolicy.evaluatePolicy() == true){
					System.out.println("\n\n\n.....VM policy violated........");
					isViolated = true;
					shouldStartScheduler = true;
					checkPlanning(1, vmList, algorithm, chart, chartAirflow);
					break;
				}
			}
			

			for (Server server : serverList) {
				ServerPolicy serverPolicy = new ServerPolicy(p.SERVER_POLICY,
						false, server);
				if (serverPolicy.evaluatePolicy() == true && !isViolated){
					System.out.println("\n\n\n............Server policy violated.............");
					isViolated = true;
					shouldStartScheduler = true;
					checkPlanning(1, vmList, algorithm, chart, chartAirflow);
					break;
					
				}
			}
		
		
			for (Rack rack : rackList) {
				RackPolicy rackPolicy = new RackPolicy(p.RACK_POLICY, false,
						rack);
				if (rackPolicy.evaluatePolicy() == true && !isViolated){
					System.out.println("\n\n\n..........Rack policy violated........");
					checkPlanning(1, vmList, algorithm, chart,chartAirflow);
					isViolated = true;
					shouldStartScheduler = true;
					break;
				}
			}
			
			if(!shouldStartScheduler)
				checkPlanning(0, vmList, algorithm, chart ,chartAirflow);
}

	public void checkPlanning(int value,List<VirtualMachine> allVMs, String algorithm, Charts chart, ChartAirflow chartAirflow) {

		System.out.println("\n\n\n ...........Starting Learning Algorithm...........\n\n");
		Execution execution = new Execution();
		RackDAO rackDAO = new RackDAOImpl();
		List<Rack> allRacks  = new ArrayList<Rack>();
		allRacks = rackDAO.getAllRacks();
			
		if(value == 1) {
			switch(algorithm) {
				case "RBR":
					execution.executeRBR(allVMs, allRacks, chart, chartAirflow);
					break;
				case "NUR":
					execution.executeNUR(allVMs, allRacks, chart, chartAirflow);
					break;
				case "FFD":
					execution.performFFD(allVMs, chart, chartAirflow);
					break;
					
			}	
		} else {
			System.out.println("............System is Optimal.............");
		}
	}

}
