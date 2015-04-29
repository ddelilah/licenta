package app.energy;

import java.util.ArrayList;
import java.util.List;

import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.constants.ServerState;
import app.model.Rack;
import app.model.Server;

public class PowerConsumption {

	/** Pmax = 500W */
	private static final int MAXIMUM_POWER = 500;
	/** fraction of power consumption of an idle server */
	private static final int K = 70/100;
	
	public float computeSingleServerPowerConsumption(Server s) {
		float power = 0;
		float utilization = s.getUtilization();
		power = s.getIdleEnergy()
				+ (MAXIMUM_POWER - s.getIdleEnergy())
				* utilization;
		
		return power;
	}
	
	public float computeSingleRackPowerConsumption(Rack r) {
		float power = 0;
		List<Server> allServers = new ArrayList<Server>();
		
		allServers = r.getServers();
		if(!allServers.isEmpty()) {
			for(Server server: allServers){
				power += server.getPowerValue();
			}
		}
		
		return power;
	}
	
	public void setServerPowerConsumption() {

		List<Rack> allRacks = new ArrayList<Rack>();
		List<Server> allServers = new ArrayList<Server>();
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		ServerDAOImpl serverDAO = new ServerDAOImpl();

		allServers = serverDAO.getAllServers();

		if (!allServers.isEmpty())
			for (Server server : allServers) {
				if (server.getState().equalsIgnoreCase("ON")) {
					float utilization = server.getUtilization();
					float power = server.getIdleEnergy()
							+ (MAXIMUM_POWER - server.getIdleEnergy())
							* utilization;

					server.setPowerValue(power);
					genericDAO.updateInstance(server);
				} else {
					server.setPowerValue(0);
					genericDAO.updateInstance(server);
				}
			}
	}
	
	public void setRackPowerConsumption() {
		
		List<Rack> allRacks = new ArrayList<Rack>();
		List<Server> allServers = new ArrayList<Server>();
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		RackDAOImpl rackDAO = new RackDAOImpl();
		float power = 0;
		
		allRacks = rackDAO.getAllRacks();
		for(Rack rack: allRacks) {
			power=0;
			allServers = rack.getServers();
			if(!allServers.isEmpty()) {
				for(Server server: allServers) {
					power += server.getPowerValue();
				}
			}
			rack.setPowerValue(power);
			genericDAO.updateInstance(rack);
		}
	}
	
	public void comparePowerValues(){

		List<Rack> allRacks = new ArrayList<Rack>();
		List<Server> allServers = new ArrayList<Server>();
		RackDAOImpl rackDAO = new RackDAOImpl();
		float power1=0, power2=0, power3=0;
		allRacks = rackDAO.getAllRacks();
		
		for(Rack rack: allRacks){
			allServers = rack.getServers();
			if(!allServers.isEmpty())
				for(Server server: allServers){
					power1 += server.getPowerValue();
					power2 += (float)K*MAXIMUM_POWER+(1-K)*MAXIMUM_POWER*server.getUtilization();
					System.out.println("\n\n\nPower Value For Server: "+server.getServerId());
					System.out.println("Power Value: "+server.getPowerValue());
					System.out.println("Power Value Draft: "+(float)(K*MAXIMUM_POWER+(1-K)*MAXIMUM_POWER*server.getUtilization()));
				}
			
			
		}
		
		System.out.println("\n\n\n\nSystem's power value1: " + power1);
		System.out.println("System's power value2: " + power2);

	}
	
	public static void main(String []args){
		PowerConsumption pc = new PowerConsumption();
		
		pc.comparePowerValues();
	}
}
