package app.energy;

import java.util.ArrayList;
import java.util.List;

import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.model.Rack;
import app.model.Server;

public class CoolingSimulation {

	/* suppose temperature is 25 degrees */
	private static final float COP = (float) (0.0068 * Math.pow(25, 2) + 0.0008 * 25 + 0.458);

	public float computeSingleServerCooling(Server s) {
		return s.getPowerValue() / COP;
	}

	public float computeSingleRackCooling(Rack r) {
		float cooling = 0;
		List<Server> allServers = new ArrayList<Server>();

		allServers = r.getServers();
		for (Server server : allServers) {
			cooling += server.getCoolingValue();
		}
		return cooling;
	}

	public void setServerCoolingValue() {

		List<Server> allServers = new ArrayList<Server>();
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		ServerDAOImpl serverDAO = new ServerDAOImpl();

		allServers = serverDAO.getAllServers();

		if (!allServers.isEmpty())
			for (Server server : allServers) {
				float cooling = server.getPowerValue() / COP;
				server.setCoolingValue(cooling);
				genericDAO.updateInstance(server);
			}
	}

	public void setRackCoolingPower() {

		List<Rack> allRacks = new ArrayList<Rack>();
		List<Server> allServers = new ArrayList<Server>();
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		RackDAOImpl rackDAO = new RackDAOImpl();
		float cooling = 0;

		allRacks = rackDAO.getAllRacks();
		for (Rack rack : allRacks) {
			cooling = 0;
			allServers = rack.getServers();
			if (!allServers.isEmpty()) {
				for (Server server : allServers) {
					cooling += server.getCoolingValue();
				}
			}
			rack.setCoolingValue(cooling);
			genericDAO.updateInstance(rack);
		}
	}


	public float getRackCoolingValueGivenInletTemperatureAndPowerValue(float inletTemperature, float powerValue){
		
		float coefficientOfPerformance = (float) (0.0068 * Math.pow(inletTemperature, 2) + 0.0008 * inletTemperature + 0.458);
		
		float cooling = powerValue / coefficientOfPerformance;
					
		return cooling;
		
	}


}
