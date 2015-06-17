package app.coolingSystems;

import java.util.ArrayList;
import java.util.List;

import app.access.impl.RackDAOImpl;
import app.model.Rack;

public class ParallelPlacementStrategy {
	
	private static final float T_MAX = 25; // [degrees Celsius]
	private static final float SPECIFIC_HEAT = 1005; // [ J/(kg* C) ] Specific Heat
	private static final float DENSITY = (float) 1.225; // [ kg/m^3 ] air density
	private static final float AREA = 36;
	
	private RackDAOImpl rackDAO = new RackDAOImpl();
	private List<Rack> rackList = new ArrayList<>();

//	private float tIn; 
	private float tOut;
	private float powerConsumption; // [W]
	private float airMassFlowRate;		// [kg/s]
	private float airVelocity;		// [m/s]
	private float fanPowerConsumption; // [W]
	private float volumeFlowRate;	   // [m^3/s]
	private float area;				   // [m^2]
	private AirMass airMassObject;
	private VolumetricAirFlowRate volumetricAirFlowObject;
	
	public ParallelPlacementStrategy(){
		this.rackList = rackDAO.getAllRacks();
	}
	
	private float computeMinMassFlowRate(float tIn){
		boolean conditionSatisfied = false;
		float m=(float) 0.1;
		
		do{
			float newTin = tIn;
			float tOut = tIn;
			for(int i =0; i<rackList.size() && tOut < T_MAX; i++ ){
				powerConsumption = rackList.get(i).getPowerValue();
				tOut = (float)(powerConsumption / (m*SPECIFIC_HEAT)) + newTin;
				newTin = tOut;				

			}

			if(tOut < T_MAX){
				airMassFlowRate = m;
				conditionSatisfied =  true;
			}
			else
				m = (float)(m+(float)0.01);
		}while(!conditionSatisfied);
		
		System.out.println("Min m is "+ m +"[kg/s]");
		return m;
	}
	
	public float computeVolumetricAirFlow(float airMassFlowRate){
		return (float)(airMassFlowRate/DENSITY);
	}
	public float computeAirVelocity(float volumetricAirFLow){
		return (float) volumetricAirFLow/AREA;
 	}
	
	
public float computeHeatRecirculation(float airLossPercentage, float tCRACin){
		boolean conditionSatisfied = false;
		float m=(float) 0.1;
		do{
			float oldTout = tCRACin;
			float tIn = (float)(tCRACin + (float)(airLossPercentage/ (1-airLossPercentage)) * 
					( (float)(rackList.get(0).getPowerValue()/ (m * SPECIFIC_HEAT))));
			float tOut = tIn;
			for(int i =0; i<rackList.size() && tOut < T_MAX; i++ ){
		
				powerConsumption = rackList.get(i).getPowerValue();
				tOut = (float)(oldTout + (float)(1/ (1-airLossPercentage)) * 
						( (float)(rackList.get(i).getPowerValue()/ (m * SPECIFIC_HEAT))));	
				
				oldTout=tOut;
				tIn = (float)(tOut + (float)(airLossPercentage/ (1-airLossPercentage)) *
						( (float)(rackList.get(i).getPowerValue()/ (m * SPECIFIC_HEAT))));				
	
			}

			if(tOut < T_MAX){
				airMassFlowRate = m;
				conditionSatisfied =  true;
			}
			else
				m = (float)(m+(float)0.01);
			
		
		}while(!conditionSatisfied);
	
		return m;
	}
	 
	public static void main(String []args){
		
		ParallelPlacementStrategy pp = new ParallelPlacementStrategy();
		
//		pp.computeFanPowerConsumption(20);
//		pp.computeMinMassFlowRate(20);
//		System.out.println(pp.computeHeatRecirculation( (float)0.1, 20));
	//	pp.computeDecreaseInFanPowerConsumption(20, 0.1f);
	}
	
	
}
