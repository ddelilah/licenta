package app.energy;

import java.util.ArrayList;
import java.util.List;

import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.constants.RackState;
import app.model.Rack;
import app.model.Server;

public class PowerConsumption {

	private static final int MAXIMUM_POWER = 1023;

	/** fraction of power consumption of an idle server */
	private static final int K = 70 / 100;

	private ServerDAOImpl serverDAO = new ServerDAOImpl();
	private RackDAOImpl rackDAO = new RackDAOImpl();

	public float computeSingleServerPowerConsumptionGivenUtilization(Server s,
			float utilization) {
		return s.getIdleEnergy() + (MAXIMUM_POWER - s.getIdleEnergy())
				* utilization;
	}

	public void setSingleServerPowerConsumptionGivenUtilization(Server s,
			float utilization) {
		if (utilization != 0) {
			float power = computeSingleServerPowerConsumptionGivenUtilization(
					s, utilization);
			s.setPowerValue(power);
			serverDAO.mergeSessionsForServer(s);
		} else {
			s.setPowerValue(0);
			serverDAO.mergeSessionsForServer(s);
		}
	}

	public float computeSingleRackPowerConsumption(Rack r) {
		float power = 0;
		List<Server> allServers = new ArrayList<Server>();

		allServers = r.getServers();
		if (!allServers.isEmpty()) {
			for (Server server : allServers) {
				power += server.getPowerValue();
			}
		}

		return power;
	}

	public void setSingleRackPowerConsumption(Rack r) {
		float power = 0;
		power = computeSingleRackPowerConsumption(r);
		if (power != 0) {
			r.setState(RackState.ON.getValue());
		}

		r.setPowerValue(power);
		rackDAO.mergeSessionsForRack(r);
	}
}
