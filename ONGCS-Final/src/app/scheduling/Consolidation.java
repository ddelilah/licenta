package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private SchedulingUtil schedulingUtil;
	
	private static int OFF_SERVER_UTILIZATION = 0;
	private static int OFF_SERVER_COOLING = 0;
	private static int OFF_SERVER_POWER = 0;
	private static int OFF_RACK_UTILIZATION = 0;
	private static int OFF_RACK_COOLING = 0;
	private static int OFF_RACK_POWER = 0;
	
	
	
	public List<VirtualMachine> consolidationForServers(Server s) {
		float newRackUtilizationAfterServerTurnOff, newRackPowerConsumptionAfterAfterServerTurnOff, newRackEstimatedCoolingAfterAfterServerTurnOff;
		List<VirtualMachine> allVmsOnServer = new ArrayList<VirtualMachine>();
		List<VirtualMachine> allVmsOnServerToBeMigrated = new ArrayList<VirtualMachine>();
		
		s.setUtilization(OFF_SERVER_UTILIZATION);
		s.setCoolingValue(OFF_SERVER_COOLING);
		s.setPowerValue(OFF_SERVER_UTILIZATION);
		s.setState(ServerState.OFF.getValue());
		serverDAO.mergeSessionsForServer(s);
		
		Rack r = s.getRack();
		
		newRackUtilizationAfterServerTurnOff = utilization.computeSingleRackUtilization(r);
		newRackEstimatedCoolingAfterAfterServerTurnOff = coolingSimulation.computeSingleRackCooling(r);
		newRackPowerConsumptionAfterAfterServerTurnOff = powerConsumption.computeSingleRackPowerConsumption(r);
		r.setUtilization(newRackUtilizationAfterServerTurnOff);
		r.setCoolingValue(newRackEstimatedCoolingAfterAfterServerTurnOff);
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

		return allVmsOnServerToBeMigrated;

	}
	
	public void consolidationOnDelete(List<VirtualMachine> vmsToBeDeleted) {
	
		float newServerUtilizationAfterVMDelete, newServerPowerConsumptionAfterVMDelete, newServerEstimatedCoolingAfterVMDelete;
		float newRackUtilizationAfterVMDelete, newRackPowerConsumptionAfterVMDelete, newRackEstimatedCoolingAfterVMDelete;
		
		List<VirtualMachine> allVmsOnServerToBeMigrated = new ArrayList<VirtualMachine>();
		List<VirtualMachine> allVmsOnRack = new ArrayList<VirtualMachine>();
		List<Server> allServersOnRack = new ArrayList<Server>();
		List<Server> allServersInDataCenter = new ArrayList<Server>();
		List<Server> serversThatBreakPolicy = new ArrayList<Server>();
		List<Server> serversThatAreOff = new ArrayList<Server>();
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
			s.setCorrespondingVMs(schedulingUtil.updateVmsOnServer(s, selectedVm));
			for(Server sr: allServersInDataCenter) {
				if(sr.getServerId() == s.getServerId()) {
					sr.setCorrespondingVMs(s.getCorrespondingVMs());
					newServerUtilizationAfterVMDelete = utilization.computeUtilization(sr);
					newServerPowerConsumptionAfterVMDelete = powerConsumption.computeSingleServerPowerConsumption(sr);
					newServerEstimatedCoolingAfterVMDelete = coolingSimulation.computeSingleServerCooling(sr);
					sr.setUtilization(newServerUtilizationAfterVMDelete);
					sr.setPowerValue(newServerPowerConsumptionAfterVMDelete);
					sr.setCoolingValue(newServerEstimatedCoolingAfterVMDelete);
					serverDAO.mergeSessionsForServer(sr);
					break;
				}
			}

			selectedVm.setServer(null);
			vmDAO.mergeSessionsForVirtualMachine(selectedVm);
			
			
		
		/* compute new resource state (for both server and rack) after vm is deleted  */
//		System.out.println("[AFTER VM DELETE FROM SERVER's LIST]Server " + s.getServerId() + " with vms: " + s.getCorrespondingVMs());	

		
		newRackPowerConsumptionAfterVMDelete = powerConsumption.computeSingleRackPowerConsumption(r);
		newRackUtilizationAfterVMDelete = utilization.computeSingleRackUtilization(r);
		newRackEstimatedCoolingAfterVMDelete = coolingSimulation.computeSingleRackCooling(r);
		r.setPowerValue(newRackPowerConsumptionAfterVMDelete);
		r.setCoolingValue(newRackEstimatedCoolingAfterVMDelete);
		r.setUtilization(newRackUtilizationAfterVMDelete);
		rackDAO.mergeSessionsForRack(r);
		}
		
		for(Server s: allServersInDataCenter) {
			if(s.getUtilization() == 0.0 && s.getState().equalsIgnoreCase("off")) {
				serversThatAreOff.add(s);
			} else if ((s.getUtilization() < 0.2 || s.getUtilization() > 0.8) && s.getState().equalsIgnoreCase("on")) {
				serversThatBreakPolicy.add(s);
			} else {
				serversThatDontBreakPolicy.add(s);
			}
		}
		
		System.out.println("Servers that break policy: " + serversThatBreakPolicy.size());
//		System.out.println("Servers that don't break policy: " + serversThatDontBreakPolicy.size());
		

		Server resultOfOBFD = new Server();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
	

		
		for(Server brokenPolicy: serversThatBreakPolicy) {
			
			allVmsOnServerToBeMigrated = consolidationForServers(brokenPolicy);
			
			for(VirtualMachine vm: allVmsOnServerToBeMigrated) {
				
				//trebuie get-ul asta pentru ca sa stiu pe ce server a fost alocat vm-ul
				VirtualMachine selectedVm = new VirtualMachine();
				selectedVm = vmDAO.getVirtualMachineById(vm.getVmId());
				System.out.println(selectedVm.getName());
				Server serverOnWhichTheVmIs = selectedVm.getServer();
				
				
				
				serverOnWhichTheVmIs.setCorrespondingVMs(schedulingUtil.updateVmsOnServer(serverOnWhichTheVmIs, selectedVm));
				for(Server sr: allServersInDataCenter) {
					if(sr.getServerId() == serverOnWhichTheVmIs.getServerId()) {
						sr.setCorrespondingVMs(serverOnWhichTheVmIs.getCorrespondingVMs());
						newServerUtilizationAfterVMDelete = utilization.computeUtilization(sr);
					//	newServerPowerConsumptionAfterVMDelete = powerConsumption.computeSingleServerPowerConsumption(sr);
					//	newServerEstimatedCoolingAfterVMDelete = coolingSimulation.computeSingleServerCooling(sr);
						sr.setUtilization(newServerUtilizationAfterVMDelete);
					//	sr.setPowerValue(newServerPowerConsumptionAfterVMDelete);
					//	sr.setCoolingValue(newServerEstimatedCoolingAfterVMDelete);
						serverDAO.mergeSessionsForServer(sr);
						break;
					}
				}
				
				OBFD obfd = new OBFD(serversThatDontBreakPolicy);
				resultOfOBFD = obfd.findAppropriateServerForConsolidationStep(selectedVm, allocation);
				
				System.out.println("[CONSOLIDATION] virtual Machine " + selectedVm.getVmId() + selectedVm.getName() + " should be placed on server " + resultOfOBFD.getServerId() + resultOfOBFD.getName());

				selectedVm.setServer(resultOfOBFD);
				vmDAO.mergeSessionsForVirtualMachine(selectedVm);

				resultOfOBFD.setCorrespondingVMs(schedulingUtil.addVmsToServer(resultOfOBFD, selectedVm));
				newServerUtilizationAfterVMDelete = utilization.computeUtilization(resultOfOBFD);
				newServerPowerConsumptionAfterVMDelete = powerConsumption.computeSingleServerPowerConsumption(resultOfOBFD);
				newServerEstimatedCoolingAfterVMDelete = coolingSimulation.computeSingleServerCooling(resultOfOBFD);
				resultOfOBFD.setUtilization(newServerUtilizationAfterVMDelete);
				resultOfOBFD.setPowerValue(newServerPowerConsumptionAfterVMDelete);
				resultOfOBFD.setCoolingValue(newServerEstimatedCoolingAfterVMDelete);
				serverDAO.mergeSessionsForServer(resultOfOBFD);
				System.out.println("Added VMs server's list of VMs: " + resultOfOBFD.getCorrespondingVMs());

			}
			
		//daca policy-ul serverului nu e broken => policy-ul rack-ului nu e broken ??? cred ca nu tot timpul
		//trebuie vazut dupa ce se calculeaza bine utilization peste tot
		//daca nu, iau rack-urile care sunt pornite si daca nu exsita niciunul, le iau pe toate
//		for(Server srv : serversThatDontBreakPolicy) {
//			racksThatAreOnAndDontBreakPolicy.add(srv.getRack());
//		}
		
//		for(Rack rack : racksThatAreOnAndDontBreakPolicy) {
//			System.out.println("racksThatAreOnAndDontBreakPolicy" + rack.toString());
//		}

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
				System.out.println("Current case => no migration");
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
		

