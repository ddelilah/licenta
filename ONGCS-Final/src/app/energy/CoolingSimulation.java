package app.energy;

import java.util.ArrayList;
import java.util.List;

import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.model.Rack;
import app.model.Server;

public class CoolingSimulation {

	private float tSuppliedByCRAC;
	private float COP;
	private ServerDAOImpl serverDAO = new ServerDAOImpl();
	private RackDAOImpl rackDAO = new RackDAOImpl();

	
	public CoolingSimulation(float tSuppliedByCRAC) {
		this.tSuppliedByCRAC = tSuppliedByCRAC;
		this.COP = (float) (0.0068 * Math.pow(tSuppliedByCRAC, 2) + 0.0008 * tSuppliedByCRAC + 0.458);
	}

	public float getCOP() {
		return COP;
	}

	public void setCOP(float cOP) {
		COP = cOP;
	}

	public float computeSingleServerCooling(Server s) {
		return s.getPowerValue() / COP;
	}
	
	public float computeSingleServerCoolingGivenPowerValue(Server s, float power) {
		return power / COP;
	}
	
	public void setSingleServerCoolingValueGivenPowerConsumption(Server s, float power) {
		float cooling = computeSingleServerCoolingGivenPowerValue(s, power);
		s.setCoolingValue(cooling);
		serverDAO.mergeSessionsForServer(s);
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
	
	public void setSingleRackCoolingValue(Rack r) {
		float cooling = computeSingleRackCooling(r);	
		r.setCoolingValue(cooling);
		rackDAO.mergeSessionsForRack(r);
	}

	public float getCOP(float temperature) {
		return (float) (0.0068 * Math.pow(temperature, 2) + 0.0008 * temperature + 0.458);
	}
	
	public float getRackCoolingValueGivenInletTemperatureAndPowerValue(float inletTemperature, float powerValue) {
		float coefficientOfPerformance = (float) (0.0068 * Math.pow(inletTemperature, 2) + 0.0008 * inletTemperature + 0.458);
		float cooling = powerValue / coefficientOfPerformance;
		
		return cooling;
		
	}

	public float getSystemCoolingPower(List<Rack> rackList, float temperature) {
		float totalPower = 0;
		for(Rack rack: rackList)
			totalPower += rack.getPowerValue();
		
		return (float) totalPower/getCOP(temperature);
	}
	

}
