package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import app.GUI.ChartAirflow;
import app.GUI.Charts;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.constants.RackState;
import app.constants.ServerState;
import app.constants.VMState;
import app.energy.CoolingSimulation;
import app.energy.PowerConsumption;
import app.energy.Utilization;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class ConsolidationUtil {
	
	private Utilization utilization = new Utilization();
	private PowerConsumption powerConsumption = new PowerConsumption();
	private CoolingSimulation coolingSimulation = new CoolingSimulation();
	private ServerDAOImpl serverDAO = new ServerDAOImpl();
	private RackDAOImpl rackDAO = new RackDAOImpl();
	private VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();

	
	private static int OFF_VALUE = 0;

	public void deleteForFFD(List<VirtualMachine> vmsToBeDeleted, Charts chart, ChartAirflow chartAirflow) {
	
		List<Server> allServers = serverDAO.getAllServers();
		List<Server> allModifiedServers = new ArrayList<Server>();
		float newServerUtilizationAfterVMDelete, newRackUtilizationAfterVMDelete, newServerPowerConsumptionAfterVMDelete, newServerCoolingAfterVMDelete, newRackPowerConsumptionAfterVMDelete, newRackCoolingAfterVMDelete;
		
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
				turnOffServer(sr);
			} else {
				serverDAO.mergeSessionsForServer(sr);
			}
		

			// System.out.println("[AFTER VM DELETE FROM SERVER]: " + sr.getUtilization());

			Rack correspondingRack = sr.getRack();

			// System.out.println("[OLD UTILIZATION]" + correspondingRack.toString());

			newRackUtilizationAfterVMDelete = utilization.computeSingleRackUtilization(correspondingRack);
			newRackPowerConsumptionAfterVMDelete = powerConsumption.computeSingleRackPowerConsumption(correspondingRack);
			newRackCoolingAfterVMDelete = coolingSimulation.computeSingleRackCooling(correspondingRack);
			correspondingRack.setUtilization(newRackUtilizationAfterVMDelete);
			correspondingRack.setPowerValue(newRackPowerConsumptionAfterVMDelete);
			correspondingRack.setCoolingValue(newRackCoolingAfterVMDelete);
		
			if (newRackUtilizationAfterVMDelete == 0) {
				turnOffRack(correspondingRack);
			} else {
				rackDAO.mergeSessionsForRack(correspondingRack);
			}
			// System.out.println("[NEW UTILIZATION]" + correspondingRack.toString());
		}
		
	}
	
	public Map<Integer, List<Server>> serverCategory (List<Server> allServers) {
		Map<Integer, List<Server>> serverCategorization = new HashMap<Integer, List<Server>>();	
		List<Server> serversThatAreOff = new ArrayList<Server>();
		List<Server> serversThatBreakPolicy = new ArrayList<Server>();
		List<Server> serversThatDontBreakPolicy = new ArrayList<Server>();

		boolean singleServerUnderUtilized = false;
		int increment = 0;

		for(Server s: allServers) {
			if(s.getUtilization() < 0.2 && s.getUtilization() > 0.0) {
				increment++;
			}
		}
		
		if(increment == 1) {
			singleServerUnderUtilized = true;
		}

		for(Server s: allServers) {
			if(s.getUtilization() == 0.0 && s.getState().equalsIgnoreCase("off")) {
				serversThatAreOff.add(s);
			} else if ((s.getUtilization() < 0.2 || s.getUtilization() > 0.8) && s.getState().equalsIgnoreCase("on") && !singleServerUnderUtilized ) {
				serversThatBreakPolicy.add(s);
			} else {
				serversThatDontBreakPolicy.add(s);
			}
		}
		
		serverCategorization.put(0, serversThatAreOff);
		serverCategorization.put(1, serversThatBreakPolicy);
		serverCategorization.put(2, serversThatDontBreakPolicy);
		
		return serverCategorization;
		
	}
	
	public void updatesToServerValues(Server s) {
		float newServerUtilizationAfterVMDelete, newServerPowerConsumptionAfterVMDelete, newServerEstimatedCoolingAfterVMDelete;
		
		newServerUtilizationAfterVMDelete = utilization.computeUtilization(s);
		newServerPowerConsumptionAfterVMDelete = powerConsumption.computeSingleServerPowerConsumption(s);
		newServerEstimatedCoolingAfterVMDelete = coolingSimulation.computeSingleServerCooling(s);
		s.setUtilization(newServerUtilizationAfterVMDelete);
		s.setPowerValue(newServerPowerConsumptionAfterVMDelete);
		s.setCoolingValue(newServerEstimatedCoolingAfterVMDelete);
		if(newServerUtilizationAfterVMDelete == 0) {
			s.setState(ServerState.OFF.getValue());
		}
		serverDAO.mergeSessionsForServer(s);
	}
	
	public void updatesToRackValues(Rack r) {
		float newRackPowerConsumptionAfterVMDelete, newRackUtilizationAfterVMDelete, newRackEstimatedCoolingAfterVMDelete;
		
		newRackPowerConsumptionAfterVMDelete = powerConsumption.computeSingleRackPowerConsumption(r);
		newRackUtilizationAfterVMDelete = utilization.computeSingleRackUtilization(r);
		newRackEstimatedCoolingAfterVMDelete = coolingSimulation.computeSingleRackCooling(r);
		r.setPowerValue(newRackPowerConsumptionAfterVMDelete);
		r.setCoolingValue(newRackEstimatedCoolingAfterVMDelete);
		r.setUtilization(newRackUtilizationAfterVMDelete);
		
		if(newRackUtilizationAfterVMDelete == 0) {
			r.setState(RackState.OFF.getValue());
		}
		rackDAO.mergeSessionsForRack(r);
	}
	
	public void turnOffServer(Server s) {
		s.setUtilization(OFF_VALUE);
		s.setCoolingValue(OFF_VALUE);
		s.setPowerValue(OFF_VALUE);
		s.setState(ServerState.OFF.getValue());
		serverDAO.mergeSessionsForServer(s);
	}
	
	public void turnOffRack(Rack r) {
		r.setUtilization(OFF_VALUE);
		r.setCoolingValue(OFF_VALUE);
		r.setPowerValue(OFF_VALUE);
		r.setState(RackState.OFF.getValue());
		rackDAO.mergeSessionsForRack(r);
	}

}
