package app.scheduling;

import java.util.ArrayList;
import java.util.List;

import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.constants.PolicyType;
import app.constants.RackState;
import app.constants.ServerState;
import app.constants.VMState;
import app.energy.CoolingSimulation;
import app.energy.PowerConsumption;
import app.energy.Utilization;
import app.execution.Execution;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class Consolidation {

	private Utilization utilization = new Utilization();
	private PowerConsumption powerConsumption = new PowerConsumption();
	private CoolingSimulation coolingSimulation = new CoolingSimulation();
	private PolicyType p;
	private GenericDAOImpl dao = new GenericDAOImpl();
	private RackDAOImpl rackDAO = new RackDAOImpl();
	private VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
	private ServerDAOImpl serverDAO = new ServerDAOImpl();
	
	private static int OFF_SERVER_UTILIZATION = 0;
	private static int OFF_SERVER_COOLING = 0;
	private static int OFF_SERVER_POWER = 0;
	private static int OFF_RACK_UTILIZATION = 0;
	private static int OFF_RACK_COOLING = 0;
	private static int OFF_RACK_POWER = 0;

	public static List<VirtualMachine> updateVmsOnServer(Server s, VirtualMachine vm) {
		List<VirtualMachine> result = new ArrayList<VirtualMachine>();
		result = s.getCorrespondingVMs();
		result.remove(vm);
		return result;
	}
	
	public List<VirtualMachine> consolidationForServers(Server s) {
		float newRackUtilizationAfterServerTurnOff, newRackPowerConsumptionAfterAfterServerTurnOff, newRackEstimatedCoolingAfterAfterServerTurnOff;
		List<VirtualMachine> allVmsOnServer = new ArrayList<VirtualMachine>();
		List<VirtualMachine> allVmsOnServerToBeMigrated = new ArrayList<VirtualMachine>();


	//	System.out.println(s.toString());
		
		s.setUtilization(OFF_SERVER_UTILIZATION);
		//s.setCoolingValue(OFF_SERVER_COOLING);
		s.setPowerValue(s.getIdleEnergy());
		s.setState(ServerState.OFF.getValue());
		serverDAO.mergeSessionsForServer(s);
		
		Rack r = s.getRack();
		
		newRackUtilizationAfterServerTurnOff = utilization.computeSingleRackUtilization(r);
		//newRackEstimatedCoolingAfterAfterServerTurnOff = coolingSimulation.computeSingleRackCooling(r);
		newRackPowerConsumptionAfterAfterServerTurnOff = powerConsumption.computeSingleRackPowerConsumption(r);
		r.setUtilization(newRackUtilizationAfterServerTurnOff);
		//r.setCoolingValue(newRackEstimatedCoolingAfterAfterServerTurnOff);
		r.setPowerValue(newRackPowerConsumptionAfterAfterServerTurnOff);
		rackDAO.mergeSessionsForRack(r);

		/* apply our algorithms on all VMs from the turned off server */
		allVmsOnServer = s.getCorrespondingVMs();
		System.out.println("[SERVER CONSOLIDATION].........");
		for (VirtualMachine vmm : allVmsOnServer) {
			if (vmm.getState().equalsIgnoreCase("RUNNING")) {
				allVmsOnServerToBeMigrated.add(vmm);
			}
		}

//		System.out.println("allVmsOnServerToBeMigrated: "
//				+ allVmsOnServerToBeMigrated.size());

		return allVmsOnServerToBeMigrated;

	}
	
	public void consolidationOnDelete(List<VirtualMachine> vmsToBeDeleted) {
		//prima data pentru vm-uri, apoi servers, apoi racks ????????
		//TODO: must be discussed, nu cred ca e cea mai buna metoda -> pot ajunge in infinite loop
		float newServerUtilizationAfterVMDelete, newServerPowerConsumptionAfterVMDelete, newServerEstimatedCoolingAfterVMDelete;
		float newRackUtilizationAfterVMDelete, newRackPowerConsumptionAfterVMDelete, newRackEstimatedCoolingAfterVMDelete;
		
		List<VirtualMachine> allVmsOnServerToBeMigrated = new ArrayList<VirtualMachine>();
		List<VirtualMachine> allVmsOnRack = new ArrayList<VirtualMachine>();
		List<Server> allServersOnRack = new ArrayList<Server>();
		List<Server> allServersInDataCenter = new ArrayList<Server>();
		List<Server> serversThatBreakPolicy = new ArrayList<Server>();
		List<Server> serversThatDontBreakPolicy = new ArrayList<Server>();
		List<Rack> allRacks = new ArrayList<Rack>();
		List<Rack> racksThatAreOnAndDontBreakPolicy = new ArrayList<Rack>();
		List<Rack> racksThatBreakPolicy = new ArrayList<Rack>();
		List<Rack> racksThatDontBreakPolicy = new ArrayList<Rack>();

		allRacks = rackDAO.getAllRacks();
		allServersInDataCenter = serverDAO.getAllServers();
		
		for(VirtualMachine v: vmsToBeDeleted) {
			System.out.println("vmId:" + v.getVmId());
			
			//trebuie get-ul asta pentru ca sa stiu pe ce server a fost alocat vm-ul
			VirtualMachine selectedVm = new VirtualMachine();
			selectedVm = vmDAO.getVirtualMachineById(v.getVmId());
			System.out.println(selectedVm.getName());
			Server s = selectedVm.getServer();
			System.out.println("Server id:" + s.getServerId());
			System.out.println("Rack id: "+ s.getRack().getRackId());
			System.out.println("Server " + s.getServerId() + " placed on rack " + selectedVm.getServer().getRack().getRackId());
			Rack r = selectedVm.getServer().getRack();
		
			selectedVm.setState(VMState.DONE.getValue());
//			vmDAO.mergeSessionsForVirtualMachine(selectedVm);
			
//			System.out.println("[BEFORE VM DELETE FROM SERVER's LIST]VM STATE " + selectedVm.getState());	
//			System.out.println("[BEFORE VM DELETE FROM SERVER's LIST]Server " + s.getServerId() + " with vms: " + s.getCorrespondingVMs());	

			//remove VM to be deleted from server's corresponding VM list
			//TODO: !!!!!!!!figure out another way if possible. very very ugly
			//dar referintele in memorie sunt diferite, deci era necesar
			s.setCorrespondingVMs(updateVmsOnServer(s, selectedVm));
			for(Server sr: allServersInDataCenter) {
				if(sr.getServerId() == s.getServerId()) {
					sr.setCorrespondingVMs(s.getCorrespondingVMs());
				}
			}

			selectedVm.setServer(null);
			vmDAO.mergeSessionsForVirtualMachine(selectedVm);
			
			
		
		/* compute new resource state (for both server and rack) after vm is deleted  */
//		System.out.println("[AFTER VM DELETE FROM SERVER's LIST]Server " + s.getServerId() + " with vms: " + s.getCorrespondingVMs());	
		newServerUtilizationAfterVMDelete = utilization.computeUtilization(s);
		newServerPowerConsumptionAfterVMDelete = powerConsumption.computeSingleServerPowerConsumption(s);
		newServerEstimatedCoolingAfterVMDelete = coolingSimulation.computeSingleServerCooling(s);
		s.setUtilization(newServerUtilizationAfterVMDelete);
		s.setPowerValue(newServerPowerConsumptionAfterVMDelete);
		s.setCoolingValue(newServerEstimatedCoolingAfterVMDelete);
		serverDAO.mergeSessionsForServer(s);
		
		newRackPowerConsumptionAfterVMDelete = powerConsumption.computeSingleRackPowerConsumption(r);
		newRackUtilizationAfterVMDelete = utilization.computeSingleRackUtilization(r);
		newRackEstimatedCoolingAfterVMDelete = coolingSimulation.computeSingleRackCooling(r);
		r.setPowerValue(newRackPowerConsumptionAfterVMDelete);
		r.setCoolingValue(newRackEstimatedCoolingAfterVMDelete);
		r.setUtilization(newRackUtilizationAfterVMDelete);
		rackDAO.mergeSessionsForRack(r);
		}
		
		for(Server s: allServersInDataCenter) {
			if((s.getUtilization() < 0.2 || s.getUtilization() > 0.8) && s.getState().equalsIgnoreCase("on")) {
				serversThatBreakPolicy.add(s);
			} else {
				serversThatDontBreakPolicy.add(s);
			}
		}
		
//		System.out.println("Servers that break policy: " + serversThatBreakPolicy.size());
//		System.out.println("Servers that don't break policy: " + serversThatDontBreakPolicy.size());
		
		for(Server brokenPolicy: serversThatBreakPolicy) {
			
			allVmsOnServerToBeMigrated = consolidationForServers(brokenPolicy);
			
		//daca policy-ul serverului nu e broken => policy-ul rack-ului nu e broken ??? cred ca nu tot timpul
		//trebuie vazut dupa ce se calculeaza bine utilization peste tot
		//daca nu, iau rack-urile care sunt pornite si daca nu exsita niciunul, le iau pe toate
		for(Server srv : serversThatDontBreakPolicy) {
			racksThatAreOnAndDontBreakPolicy.add(srv.getRack());
		}
		
//		for(Rack rack : racksThatAreOnAndDontBreakPolicy) {
//			System.out.println("racksThatAreOnAndDontBreakPolicy" + rack.toString());
//		}
		
		Execution execution = new Execution();
		execution.executeNUR(allVmsOnServerToBeMigrated, racksThatAreOnAndDontBreakPolicy);
//		execution.executeRBR(allVmsOnServerToBeMigrated, racksThatAreOnAndDontBreakPolicy);
		}
		
		boolean singleRackOn = false;
		int i;
		
		String first = allRacks.get(0).getState();
//		System.out.println("State of the first:" + first);
		for(i = 1; i < allRacks.size(); i++) {
			if(!allRacks.get(i).getState().equalsIgnoreCase(first)) {
				singleRackOn = true;
			}
		}
		
	
		for(Rack r: allRacks) {
			if(r.getUtilization() < 40 && r.getState().equalsIgnoreCase("on") && singleRackOn) {
		//		System.out.println("Current case => no migration");
				racksThatDontBreakPolicy.add(r);
			} else if ((r.getUtilization() < 40 || r.getUtilization() > 80) && r.getState().equalsIgnoreCase("on")) {
				racksThatBreakPolicy.add(r);
			} else {
				racksThatDontBreakPolicy.add(r);
			}
		}
		
		for(Rack r: racksThatBreakPolicy) {
	//		System.out.println("Rack utilization before rack turn off:" + r.getUtilization());
		r.setUtilization(OFF_RACK_UTILIZATION);
		r.setState(RackState.OFF.getValue());
		rackDAO.mergeSessionsForRack(r);
		allServersOnRack = r.getServers();
		
	//	System.out.println("Rack utilization after rack turn off:" + r.getUtilization());
		for(Server srv : allServersOnRack) {
			allVmsOnRack = consolidationForServers(srv);
			
			Execution execution = new Execution();
			execution.executeNUR(allVmsOnRack, racksThatAreOnAndDontBreakPolicy);
		}
		
		}
	
		
	}
}
		

