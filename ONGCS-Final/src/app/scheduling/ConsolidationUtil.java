package app.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.constants.RackState;
import app.constants.ServerState;
import app.energy.CoolingSimulation;
import app.energy.PowerConsumption;
import app.energy.Utilization;
import app.model.Rack;
import app.model.Server;

public class ConsolidationUtil {
	
	private Utilization utilization = new Utilization();
	private PowerConsumption powerConsumption = new PowerConsumption();
	private CoolingSimulation coolingSimulation = new CoolingSimulation();
	private ServerDAOImpl serverDAO = new ServerDAOImpl();
	private RackDAOImpl rackDAO = new RackDAOImpl();
	
	private static int OFF_VALUE = 0;

	
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
