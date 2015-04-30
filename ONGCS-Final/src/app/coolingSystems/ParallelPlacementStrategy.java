package app.coolingSystems;

import java.util.ArrayList;
import java.util.List;

import app.access.impl.RackDAOImpl;
import app.energy.*;
import app.model.Rack;

public class ParallelPlacementStrategy {

	// Ce se intampla cand unul din racks are utilization=0?
	
	
	private RackDAOImpl rackDAO = new RackDAOImpl();
	private List<Rack> rackList = new ArrayList<>();
	private static final float Tmax = 27;
	private CoolingSimulation coolingSimulation = new CoolingSimulation() ;
	public ParallelPlacementStrategy(){
		this.rackList = rackDAO.getAllRacks();
	}
	
	/** Suppose the user already has a parallel alignment of racks*/
	/**
	 * Standard recommended inlet temperature [18C,27C] 
	 * */
	public void findMinAirMass(float inletTemperature){
		
		float airMass = Float.MAX_VALUE;
		boolean conditionSatisfied = true;
		float tOut = inletTemperature + 1;
		float newTout = tOut;
		float tIn;
		List<Float> tOUTs = new ArrayList<>();
		List<Float> finalTout = new ArrayList<>();
		while(conditionSatisfied ){
			tIn = inletTemperature;
			float cooling = coolingSimulation.getRackCoolingValueGivenInletTemperatureAndPowerValue(tIn, rackList.get(0).getPowerValue());
			float m = (float) rackList.get(0).getPowerValue()/((float)cooling * ( tOut - tIn));
			tOUTs = new ArrayList<>();
			for(int i=0; i<rackList.size() ; i++){
				if(rackList.get(i).getPowerValue() != 0){
					cooling = coolingSimulation.getRackCoolingValueGivenInletTemperatureAndPowerValue(tIn, rackList.get(i).getPowerValue());
					newTout =(float) (rackList.get(i).getPowerValue() / (float)(m * cooling)) + tIn;
					tIn = (float) Math.ceil(newTout);
					tOUTs.add(tIn);
				}
				// else what happens????????
				// ----------------------------	  ---------------------------	  --------------------------
				// |PowerConsumptionRack1 > 0 |&& |PowerConsumptionRack2 == 0| && |PowerConsumptionRack3 > 0|
				// ----------------------------	   --------------------------	  --------------------------
			}
			
// [Ask Marcel] modifica conditia => utlimul Tin sa fie in range, nu ultimul Tout!
			if(newTout > Tmax){
				conditionSatisfied = false;
			}
			else {
				if(m < airMass){
					airMass = m;
					tOut ++;
					finalTout = tOUTs;
				}
			}	
		}
		
		// ----- ce unitate de masura? --------------------
		System.out.println("-------------------------------------------- \n[Rack Layout][Parallel alignment]\nThe minimum needed air mass = "+airMass);
		System.out.print("Exhaust temperatures are ");
		for(int i=0; i< finalTout.size(); i++){
			System.out.print(finalTout.get(i)+" ");
		}
		System.out.println("\n--------------------------------------------");
	}
	
	public static void main(String [] args){
		ParallelPlacementStrategy pp = new ParallelPlacementStrategy();
		pp.findMinAirMass(18);
		
	}
}
