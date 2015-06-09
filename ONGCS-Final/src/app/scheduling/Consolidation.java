package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import app.GUI.Charts;
import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.constants.PolicyType;
import app.constants.RackState;
import app.constants.ServerState;
import app.constants.VMState;
import app.energy.CoolingSimulation;
import app.energy.MigrationEfficiency;
import app.energy.PowerConsumption;
import app.energy.Utilization;
import app.GUI.ChartAirflow;
import app.execution.Execution;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.policies.RackPolicy;
import app.policies.ServerPolicy;

public class Consolidation {

	private Utilization utilization = new Utilization();
	private PolicyType p;
	private RackDAOImpl rackDAO = new RackDAOImpl();
	private VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
	private ServerDAOImpl serverDAO = new ServerDAOImpl();
	private static VMProcessor vmProcessor;
	private PowerConsumption powerConsumption = new PowerConsumption();
	private CoolingSimulation coolingSimulation;
	private Execution exec ;
	private int numberOfReleasedNodes = 0;
	private int numberOfSuccessfulMigrations = 0;

	private String cracTemp;
	private ConsolidationUtil consolidationUtil ;
	
	private SchedulingUtil sUtil = new SchedulingUtil();

	private static List<Server> resultOfServerAllocation = new ArrayList<Server>();
	private static List<Rack> resultOfRackReallocation = new ArrayList<Rack>();

	public Consolidation(String cracTemp){
		this.cracTemp = cracTemp;
		coolingSimulation = new CoolingSimulation(Integer.parseInt(cracTemp));
		exec = new Execution (cracTemp);
		consolidationUtil = new ConsolidationUtil(cracTemp);
	}
	
	public String getCracTemp() {
		return cracTemp;
	}

	public void setCracTemp(String cracTemp) {
		this.cracTemp = cracTemp;
	}

	public void canMoveAllVMsSomewhereElse(List<VirtualMachine> VMs,
			List<Server> servers) {
		boolean canMove = false;
		Server resultOfOBFD = new Server();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		VirtualMachine vmToBeMoved = new VirtualMachine();
		List<Server> releasedNodes = new ArrayList<Server>();
		Server serverToBePlacedOn = new Server();
		int numberOfVms = 0;

		List<VirtualMachine> sortedVMs = new ArrayList<VirtualMachine>();

		vmProcessor = new VMProcessor(VMs);
		sortedVMs = vmProcessor.sortVMListDescending();

		numberOfVms = sortedVMs.size();

		for (VirtualMachine selectedVm : sortedVMs) {
			OBFD obfd = new OBFD(servers, cracTemp);
			resultOfOBFD = obfd.findAppropriateServerForConsolidationStep(selectedVm, allocation);

			if (resultOfOBFD != null && resultOfOBFD.getServerId() != 0) {
				allocation.put(selectedVm, resultOfOBFD);
				canMove = true;
			} else {
				canMove = false;
			}

		}

		if (canMove && allocation.entrySet().size() == numberOfVms) {
			for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
				vmToBeMoved = entry.getKey();
				serverToBePlacedOn = entry.getValue();

				System.out.println("[RACK CONSOLIDATION] virtual Machine "
						+ vmToBeMoved.getVmId() + vmToBeMoved.getName()
						+ " should be placed on server "
						+ serverToBePlacedOn.getServerId()
						+ serverToBePlacedOn.getName());
				 Thread.yield();
			        try { Thread.sleep(1000); } catch (InterruptedException e) {}
				Rack oldRack = vmToBeMoved.getServer().getRack();
				Server oldServer = vmToBeMoved.getServer();

				vmToBeMoved.setServer(serverToBePlacedOn);
				vmDAO.mergeSessionsForVirtualMachine(vmToBeMoved);
				numberOfSuccessfulMigrations++;
				serverToBePlacedOn.setCorrespondingVMs(SchedulingUtil.addVmsToServer(serverToBePlacedOn, vmToBeMoved));
				consolidationUtil.turnOffServer(oldServer);
				releasedNodes.add(oldServer);
				
				consolidationUtil.updatesToServerValues(serverToBePlacedOn);
//				System.out.println("Added VMs server's list of VMs: "
//						+ serverToBePlacedOn.getCorrespondingVMs());
				resultOfServerAllocation.add(serverToBePlacedOn);
				Rack newRack = serverToBePlacedOn.getRack();
				consolidationUtil.updatesToRackValues(oldRack);
				resultOfRackReallocation.add(oldRack);
				consolidationUtil.updatesToRackValues(newRack);
				resultOfRackReallocation.add(newRack);
			}

		} else {
//			System.out.println("[SERVER CAN'T BE TURNED OFF => LET IT BE]");
		}
		
		int firstId = 0;
		
		if(!releasedNodes.isEmpty()) {
			firstId = releasedNodes.get(0).getServerId();
			if(firstId != 0) {
				numberOfReleasedNodes = 1;
			}
		}
		
		for(Server s: releasedNodes) {
			if(firstId != s.getServerId()) {
				numberOfReleasedNodes++;
			}
		}
	}

	public void tryToMoveAllVMsFromARack(Rack r) {
		List<Server> serversList = r.getServers();

		for (Server s : serversList) {
			if (s.getState().equalsIgnoreCase("ON")) {
				tryToMoveAllVMsFromAServer(s);
			}
		}
	}

	public void tryToMoveAllVMsFromAServer(Server s) {

		List<VirtualMachine> allVmsOnServer = new ArrayList<VirtualMachine>();
		List<VirtualMachine> allVmsOnServerToBeMigrated = new ArrayList<VirtualMachine>();
		List<VirtualMachine> allVMsOnRackToBeMigrated = new ArrayList<VirtualMachine>();
		List<Server> allServersInDataCenter = new ArrayList<Server>();
		List<Server> serversThatAreOff = new ArrayList<Server>();
		List<Server> serversThatBreakPolicy = new ArrayList<Server>();
		List<Server> serversThatDontBreakPolicy = new ArrayList<Server>();
		List<Server> serversThatAreOffOnRack = new ArrayList<Server>();
		List<Server> serversThatBreakPolicyOnRack = new ArrayList<Server>();
		List<Server> serversThatDontBreakPolicyOnRack = new ArrayList<Server>();
		List<Server> serversOnTheSameRack = new ArrayList<Server>();
		
		List<Rack> allRacks = rackDAO.getAllRacks();
		Server underUtilizedServerFromAllocationStep = new Server();
		Rack underUtilizedRackFromAllocationStep = new Rack();


		Map<Integer, List<Server>> serverTaxonomy = new HashMap<Integer, List<Server>>();
		Map<Integer, List<Server>> serverTaxonomyOnRack = new HashMap<Integer, List<Server>>();

		allServersInDataCenter = serverDAO.getAllServers();
		for (Server srv : allServersInDataCenter) {
			if (srv.getServerId() == s.getServerId()) {
				srv.setUtilization(s.getUtilization());
				srv.setPowerValue(s.getPowerValue());
				srv.setCoolingValue(s.getCoolingValue());
				srv.setState(s.getState());
				break;
			}
		}
		
		for (Server sr : allServersInDataCenter) {
			ServerPolicy serverPolicy = new ServerPolicy(p.SERVER_POLICY, false, sr);
			if (serverPolicy.checkServerUtilizationViolation(sr.getUtilization())) {
				underUtilizedServerFromAllocationStep = sr;
				break;
			}
		}

		for (Rack r : allRacks) {
			RackPolicy rackPolicy = new RackPolicy(p.RACK_POLICY, false, r);
			if (rackPolicy.checkRackUtilizationViolation(r.getUtilization())) {
				underUtilizedRackFromAllocationStep = r;
				break;
			}
		}

		serverTaxonomy = consolidationUtil
				.serverCategory(allServersInDataCenter);

		serversThatAreOff = serverTaxonomy.get(0);
		
		serversThatBreakPolicy = serverTaxonomy.get(1);
		serversThatDontBreakPolicy = serverTaxonomy.get(2);

		Rack r = s.getRack();
		serversOnTheSameRack = r.getServers();
		serverTaxonomyOnRack = consolidationUtil.serverCategory(serversOnTheSameRack);
		serversThatAreOffOnRack = serverTaxonomyOnRack.get(0);
		serversThatBreakPolicyOnRack = serverTaxonomyOnRack.get(1);
		serversThatDontBreakPolicyOnRack = serverTaxonomyOnRack.get(2);

		Iterator<Server> iterator = serversThatDontBreakPolicy.iterator();
		// Iterator<Server> rackIterator =
		// serversThatDontBreakPolicyOnRack.iterator();
		while (iterator.hasNext()) {
			Server sr1 = iterator.next();
			for (Server sr2 : serversThatDontBreakPolicyOnRack) {
				if (sr1.getServerId() == sr2.getServerId()) {
					iterator.remove();
				}
			}
		}

		RackPolicy rackPolicy = new RackPolicy(p.RACK_POLICY, false, r);

		allVmsOnServer = s.getCorrespondingVMs();
		System.out.println("[SERVER CONSOLIDATION].........");
		 Thread.yield();
	        try { Thread.sleep(1000); } catch (InterruptedException e) {}
		for (VirtualMachine vmm : allVmsOnServer) {
			if (vmm.getState().equalsIgnoreCase("RUNNING")) {
				allVmsOnServerToBeMigrated.add(vmm);
			}
		}

		for (VirtualMachine vmm : allVmsOnServerToBeMigrated) {
			s.setCorrespondingVMs(SchedulingUtil.updateVmsOnServer(s, vmm));
		}

		for (Server s1 : serversOnTheSameRack) {
			List<VirtualMachine> virtualMachinesOnServersFromTheSameRack = s1.getCorrespondingVMs();
			for (VirtualMachine v1 : virtualMachinesOnServersFromTheSameRack) {
				allVMsOnRackToBeMigrated.add(v1);
			}
		}
		
		boolean singleRackOn = true;
			int i;
				
				String first = r.getState();
		//		System.out.println("State of the first:" + first);
				if(first.equalsIgnoreCase("ON")) {
				for(i = 1; i < allRacks.size(); i++) {
					if(allRacks.get(i).getState().equalsIgnoreCase(first)) {
						singleRackOn = false;
						break;
					}
				}
			}
				
		for(Rack realloc: resultOfRackReallocation) {
			if(realloc.getRackId() == r.getRackId()) {
				r.setUtilization(realloc.getUtilization());
				r.setCoolingValue(realloc.getCoolingValue());
				r.setPowerValue(realloc.getPowerValue());
			}
		}
		
		if(r.getRackId() == underUtilizedRackFromAllocationStep.getRackId() && singleRackOn) {
			System.out.println("[RACK POLICY IS VIOLATED, BUT THIS IS THE ONLY TURNED ON RACK => MIGRATE ALL VMS FROM THE UNDERUTILIZED SERVER TO SERVERS ON THE SAME RACK");
			canMoveAllVMsSomewhereElse(allVmsOnServerToBeMigrated, serversThatDontBreakPolicyOnRack);
			 Thread.yield();
		        try { Thread.sleep(1000); } catch (InterruptedException e) {}
		} else
		if (r.getState().equalsIgnoreCase("ON") && rackPolicy.checkRackUtilizationViolation(r.getUtilization())) {
			System.out.println("[RACK POLICY IS VIOLATED => MIGRATE ALL VMS FROM THE UNDERUTILIZED SERVER ON RACK ]" + r.getName() + " " + r.getUtilization() + " TO SERVERS ON OTHER RACKS");
			canMoveAllVMsSomewhereElse(allVmsOnServerToBeMigrated, serversThatDontBreakPolicy);
			 Thread.yield();
		        try { Thread.sleep(1000); } catch (InterruptedException e) {}
		} else {
			System.out.println("[RACK POLICY IS NOT VIOLATED => MIGRATE ALL VMS TO OTHER SERVERS ON THE SAME RACK");
			canMoveAllVMsSomewhereElse(allVmsOnServerToBeMigrated, serversThatDontBreakPolicyOnRack);
			 Thread.yield();
		        try { Thread.sleep(1000); } catch (InterruptedException e) {}
		}
	}

	public void consolidationOnDelete(List<VirtualMachine> vmsToBeDeleted, Charts chart,  ChartAirflow chartAirflow,String algorithm) {

		float newServerUtilizationAfterVMDelete, newRackUtilizationAfterVMDelete, newServerPowerConsumptionAfterVMDelete, newServerCoolingAfterVMDelete, newRackPowerConsumptionAfterVMDelete, newRackCoolingAfterVMDelete;
		List<Server> allServers = serverDAO.getAllServers();
		List<Server> allModifiedServers = new ArrayList<Server>();
		List<Rack> allRacks = rackDAO.getAllRacks();
		Server underUtilizedServerFromAllocationStep = new Server();
		Rack underUtilizedRackFromAllocationStep = new Rack();

		for (Server sr : allServers) {
			ServerPolicy serverPolicy = new ServerPolicy(p.SERVER_POLICY, false, sr);
			if (serverPolicy.checkServerUtilizationViolation(sr.getUtilization())) {
				underUtilizedServerFromAllocationStep = sr;
				break;
			}
		}

		for (Rack r : allRacks) {
			RackPolicy rackPolicy = new RackPolicy(p.RACK_POLICY, false, r);
			if (rackPolicy.checkRackUtilizationViolation(r.getUtilization())) {
				underUtilizedRackFromAllocationStep = r;
				break;
			}
		}

		for (VirtualMachine selectedVm : vmsToBeDeleted) {
			Server correspondingServer = selectedVm.getServer();

			selectedVm.setState(VMState.DONE.getValue());
			selectedVm.setServer(null);
			vmDAO.mergeSessionsForVirtualMachine(selectedVm);
//			System.out.println("[BEFORE VM DELETE FROM SERVER's LIST]Server "
//					+ correspondingServer.getServerId() + " with vms: "
//					+ correspondingServer.getCorrespondingVMs());

			for (Server sr : allServers) {
				if (sr.getServerId() == correspondingServer.getServerId()) {
					sr.setCorrespondingVMs(SchedulingUtil.updateVmsOnServer(correspondingServer, selectedVm));
					allModifiedServers.add(sr);
					break;

					// System.out.println("[AFTER VM DELETE FROM SERVER's LIST]Server "
					// + correspondingServer.getServerId() + " with vms: " +
					// correspondingServer.getCorrespondingVMs());
				}

				ListIterator<Server> iter = allModifiedServers.listIterator();
				while (iter.hasNext()) {
					if (iter.next().getServerId() == sr.getServerId()) {
						iter.set(sr);
					}
				}
			}
		}

		for (Server sr : allModifiedServers) {

			// System.out.println("[BEFORE VM DELETE FROM SERVER]: " + sr.getUtilization());
			newServerUtilizationAfterVMDelete = utilization.computeUtilization(sr);
			newServerPowerConsumptionAfterVMDelete = powerConsumption.computeSingleServerPowerConsumptionGivenUtilization(sr, newServerUtilizationAfterVMDelete);
			newServerCoolingAfterVMDelete = coolingSimulation.computeSingleServerCoolingGivenPowerValue(sr, newServerPowerConsumptionAfterVMDelete);
			sr.setPowerValue(newServerPowerConsumptionAfterVMDelete);
			sr.setCoolingValue(newServerCoolingAfterVMDelete);
			sr.setUtilization(newServerUtilizationAfterVMDelete);
	
			if (newServerUtilizationAfterVMDelete == 0) {
				consolidationUtil.turnOffServer(sr);
			} else {
				serverDAO.mergeSessionsForServer(sr);
			}

			// System.out.println("[AFTER VM DELETE FROM SERVER]: " + sr.getUtilization());

			Rack correspondingRack = sr.getRack();

			// System.out.println("[OLD UTILIZATION]" + correspondingRack.toString());


			newRackPowerConsumptionAfterVMDelete = powerConsumption.computeSingleRackPowerConsumption(correspondingRack);
			newRackUtilizationAfterVMDelete = utilization.computeSingleRackUtilization(correspondingRack);
			newRackCoolingAfterVMDelete = coolingSimulation.computeSingleRackCooling(correspondingRack);
			correspondingRack.setPowerValue(newRackPowerConsumptionAfterVMDelete);
			correspondingRack.setCoolingValue(newRackCoolingAfterVMDelete);
			correspondingRack.setUtilization(newRackUtilizationAfterVMDelete);

			if (newRackUtilizationAfterVMDelete == 0) {
				consolidationUtil.turnOffRack(correspondingRack);
			} else {
				rackDAO.mergeSessionsForRack(correspondingRack);
			}
			// System.out.println("[NEW UTILIZATION]" + correspondingRack.toString());
		}
		
		sUtil.displayPowerConsumptionAndCooling("[AFTER DELETE, BEFORE ACTUAL CONSOLIDATION]");
		
		

		// RECONSOLIDATION FOR SERVERS
		if (underUtilizedServerFromAllocationStep != null && underUtilizedServerFromAllocationStep.getServerId() != 0
				&& !underUtilizedServerFromAllocationStep.getState().equalsIgnoreCase(ServerState.OFF.getValue())) {
			// move all vms from that server on other servers
			System.out.println("[SERVER RECONSOLIDATION]");
			tryToMoveAllVMsFromAServer(underUtilizedServerFromAllocationStep);
		}
		
		
		if(underUtilizedServerFromAllocationStep != null && underUtilizedRackFromAllocationStep.getRackId() != 0) {
			if(underUtilizedServerFromAllocationStep.getRack().getRackId() != underUtilizedRackFromAllocationStep.getRackId()) {
				// RECONSOLIDATION FOR RACKS
				if (underUtilizedRackFromAllocationStep != null
						&& !underUtilizedRackFromAllocationStep.getState().equalsIgnoreCase(RackState.OFF.getValue())) {
					for (Server srv : underUtilizedRackFromAllocationStep.getServers()) {
						for (Server sr2 : resultOfServerAllocation) {
							if (srv.getServerId() == sr2.getServerId()) {
								srv.setCorrespondingVMs(sr2.getCorrespondingVMs());
								srv.setUtilization(sr2.getUtilization());
								srv.setPowerValue(sr2.getPowerValue());
								srv.setCoolingValue(sr2.getCoolingValue());
								break;
							}
			 					}
			}
					
				// move all vms from that rack on other servers from other racks
				System.out.println("[RACK RECONSOLIDATION]");
				 Thread.yield();
			        try { Thread.sleep(1000); } catch (InterruptedException e) {}
				tryToMoveAllVMsFromARack(underUtilizedRackFromAllocationStep);
			}
		} else {
			System.out.println("[UNDERUTILIZED SERVER IS ON THE UNDERUTILIZED RACK => CORNER CASE PREVIOUSLY CHECKED]");
			 Thread.yield();
		        try { Thread.sleep(1000); } catch (InterruptedException e) {}
		}
	}
		

		for (Server sr : allModifiedServers) {
			for (Server sr2 : resultOfServerAllocation) {
				if (sr.getServerId() == sr2.getServerId()) {
					sr.setCorrespondingVMs(sr2.getCorrespondingVMs());
					sr.setUtilization(sr2.getUtilization());
					sr.setPowerValue(sr2.getPowerValue());
					sr.setCoolingValue(sr2.getCoolingValue());
					break;
				}
			}
			
			

			ServerPolicy serverPolicy = new ServerPolicy(p.SERVER_POLICY, false, sr);
			Rack correspondingRack = sr.getRack();
			
			for(Rack realloc: resultOfRackReallocation) {
				if(realloc.getRackId() == correspondingRack.getRackId()) {
					correspondingRack.setUtilization(realloc.getUtilization());
					correspondingRack.setCoolingValue(realloc.getCoolingValue());
					correspondingRack.setPowerValue(realloc.getPowerValue());
				}
			}
			
			RackPolicy rackPolicy = new RackPolicy(p.RACK_POLICY, false, correspondingRack);

			if (sr.getState().equalsIgnoreCase("ON") && serverPolicy.checkServerUtilizationViolation(sr.getUtilization())) {
				System.out.println("[SERVER POLICY IS VIOLATED => MIGRATE ALL VMS FROM SERVER TO OTHER SERVERS]");
				 Thread.yield();
			        try { Thread.sleep(1000); } catch (InterruptedException e) {}
				tryToMoveAllVMsFromAServer(sr);
			
			} else {
				// Server does not break policy => check rack
				if (correspondingRack.getState().equalsIgnoreCase("ON") && rackPolicy.checkRackUtilizationViolation(correspondingRack.getUtilization())) {
					 Thread.yield();
				        try { Thread.sleep(1000); } catch (InterruptedException e) {}
					System.out.println("[RACK POLICY IS VIOLATED => MIGRATE ALL VMS FROM THE RACK TO OTHER RACKS");
					tryToMoveAllVMsFromARack(correspondingRack);
					
				} else {
//					System.out.println("[HAPPY FLOW]");
				}
			}
		}
		
		MigrationEfficiency mEff = new MigrationEfficiency();
		System.out.println("Migration efficiency: "+ mEff.computeMigrationEfficiency(numberOfReleasedNodes, numberOfSuccessfulMigrations));		
		System.out.println("[CONSOLIDATION] #Released Nodes: " + numberOfReleasedNodes);
		System.out.println("[CONSOLIDATION] #Migrations: " + numberOfSuccessfulMigrations);
		 Thread.yield();
	        try { Thread.sleep(1000); } catch (InterruptedException e) {}
		
	//	handleTheFailedVMs(chart, chartAirflow, algorithm);
}
			
		
public void handleTheFailedVMs(Charts chart,  ChartAirflow chartAirflow,String algorithm) {
		List<VirtualMachine> failedVMs = vmDAO.getAllVMsByState(VMState.FAILED.getValue());
		List<Rack> allRacks = rackDAO.getAllRacks();	
		if(failedVMs.size() != 0) {
			switch(algorithm) {
			case "RBR":
				exec.executeRBR(failedVMs, allRacks, chart, chartAirflow);
				break;
			case "NUR":
				exec.executeNUR(failedVMs, allRacks, chart, chartAirflow);
				break;
			case "FFD":
				exec.performFFD(failedVMs, chart, chartAirflow);
				break;
			
		}
			
		}
}
}
