package app.coolingSystems;

import java.util.ArrayList;
import java.util.List;

import app.access.impl.RackDAOImpl;
import app.energy.CoolingSimulation;
import app.model.Rack;

public class HACS {

	// Hot Aisle Containment - the uncontained area = inlet temperature (cold
	// area)

	private static final float T_MAX = 25; // [degrees Celsius]
	private static final float SPECIFIC_HEAT = 1005; // [ J/(kg* C) ] Specific
														// Heat
	private static final float DENSITY = (float) 1.225; // [ kg/m^3 ] air
														// density
	private static final float AREA = 36;

	private RackDAOImpl rackDAO = new RackDAOImpl();
	private List<Rack> rackList = new ArrayList<>();

	// private float tIn;
	private float tOut;
	private float totalPowerConsumption; // [W]
	private float airMassFlowRate; // [kg/s]
	private float airVelocity; // [m/s]
	private float fanPowerConsumption; // [W]
	private float volumeFlowRate; // [m^3/s]
	private float area; // [m^2]
	private AirMass airMassObject;
	private VolumetricAirFlowRate volumetricAirFlowObject;

	public HACS() {
		this.rackList = rackDAO.getAllRacks();
		totalPowerConsumption = 0;

	}

	public float computeMinMassFlowRate(float tIn) {
		boolean conditionSatisfied = false;
		float m = (float) 0.1;
		totalPowerConsumption = 0;
		for (Rack rack : rackList)
			totalPowerConsumption += rack.getPowerValue();

		do {
			float newTin = tIn;
			float tOut = tIn;
			tOut = (float) (totalPowerConsumption / (m * SPECIFIC_HEAT))
					+ newTin;
			newTin = tOut;

			if (tOut < T_MAX) {
				airMassFlowRate = m;
				conditionSatisfied = true;
			} else
				m = (float) (m + (float) 0.01);
		} while (!conditionSatisfied);

		return m;
	}

	public float computeVolumetricAirFlow(float airMassFlowRate) {
		return (float) (airMassFlowRate / DENSITY);
	}

	public float computeAirVelocity(float volumetricAirFLow) {
		return (float) volumetricAirFLow / AREA;
	}

	public void computeFanPowerConsumption(float tIn) {

		float airMassFlowRate = computeMinMassFlowRate(tIn);
		float volumetricAirFlow = computeVolumetricAirFlow(airMassFlowRate);
		float airVelocity = computeAirVelocity(volumetricAirFlow);

	}
}
