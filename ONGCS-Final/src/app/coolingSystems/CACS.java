//package app.coolingSystems;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import app.access.impl.RackDAOImpl;
//import app.energy.CoolingSimulation;
//import app.model.Rack;
//
//public class CACS {
//	// Cold Aisle Containment
//	// Uncontained Area Temperature = Tout => need to find Tin 
//	private RackDAOImpl rackDAO = new RackDAOImpl();
//	private List<Rack> rackList = new ArrayList<>();
//	private static final float Tmax = 27;
//	private CoolingSimulation coolingSimulation = new CoolingSimulation() ;
//	public CACS(){
//		this.rackList = rackDAO.getAllRacks();
//	}
//	
//	public float computeSystemCoolingPowerGivenInletTemperature(float inletTemperature){
//		float systemCoolingPower =0;
//		for(Rack rack: rackList){
//			float cooling = coolingSimulation.getRackCoolingValueGivenInletTemperatureAndPowerValue(inletTemperature, rack.getPowerValue());
//			systemCoolingPower += cooling;
//		}
//		return systemCoolingPower;
//	}
//
//	public float computeSystemCoolingPowerGivenUncontainedAreaTemperature(float uncontainedAreaTemperature){
//		return 0;
//	}
//}
