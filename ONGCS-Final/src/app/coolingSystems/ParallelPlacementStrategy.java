package app.coolingSystems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.access.impl.RackDAOImpl;
import app.energy.*;
import app.model.Rack;
import app.model.VirtualMachine;

import java.util.Arrays;

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
	
	public ParallelPlacementStrategy(){
		this.rackList = rackDAO.getAllRacks();
	}
	
	private float computeMinMassFlowRate(float tIn){
		boolean conditionSatisfied = false;
		float m=(float) 0.1;
		
		do{
			float newTin = tIn;
			float tOut = tIn;
			System.out.println("\n\n\n\n----------- m is "+ (float)m+" -----------------------");
			for(int i =0; i<rackList.size() && tOut < T_MAX; i++ ){
				System.out.println("tIn is "+newTin);
				powerConsumption = rackList.get(i).getPowerValue();
				tOut = (float)(powerConsumption / (m*SPECIFIC_HEAT)) + newTin;
				newTin = tOut;				
				System.out.println("powerConsumption is "+powerConsumption);
				System.out.println("tOut is "+tOut);
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
	
	private float computeVolumetricAirFlow(float airMassFlowRate){
		System.out.println("volumetricAirFlow="+(float)(airMassFlowRate / DENSITY)+"[m^3/s]");
		return (float)(airMassFlowRate/DENSITY);
	}
	private float computeAirVelocity(float volumetricAirFLow){
		System.out.println("airVelocity = "+ (float)(volumetricAirFLow/AREA)+"[m/s]");
		return (float) volumetricAirFLow/AREA;
 	}
	
	public void computeFanPowerConsumption(float tIn){
		
		float airMassFlowRate = computeMinMassFlowRate(tIn);
		float volumetricAirFlow = computeVolumetricAirFlow(airMassFlowRate);
		float airVelocity = computeAirVelocity(volumetricAirFlow);
		
		//----------- COMPUTE FAN POWER CONSUMPTION ----------------------------
	}
	
	public float computeHeatRecirculation(float airLossPercentage, float tIn){
		
		float idealSystemAirMassFlowRate = computeMinMassFlowRate(tIn);
		
		float recirculatedTin = (float)(tIn + (float)(1/ (1-airLossPercentage)) * ( (float)(rackList.get(0).getPowerValue()/ (idealSystemAirMassFlowRate * SPECIFIC_HEAT))));
		
		boolean conditionSatisfied = false;
		float m=(float) 0.1;
		
		do{
			float newTin = recirculatedTin;
			float tOut = recirculatedTin;
			System.out.println("\n\n\n\n----------- m is "+ (float)m+" -----------------------");
			for(int i =0; i<rackList.size() && tOut < T_MAX; i++ ){
				System.out.println("tIn is "+newTin);
				powerConsumption = rackList.get(i).getPowerValue();
				tOut = (float)(powerConsumption / (m*SPECIFIC_HEAT)) + newTin;
				newTin = (float)(tOut + (float)(1/ (1-airLossPercentage)) * ( (float)(rackList.get(i).getPowerValue()/ (m * SPECIFIC_HEAT))));				
				System.out.println("powerConsumption is "+powerConsumption);
				System.out.println("tOut is "+tOut);
			}

			if(tOut < T_MAX){
				airMassFlowRate = m;
				conditionSatisfied =  true;
			}
			else
				m = (float)(m+(float)0.01);
		}while(!conditionSatisfied);
		
		System.out.println("Min m is "+ m +"[kg/s]");
		
		return (float)(tIn + (float)(1/ (1-airLossPercentage)) * ( (float)(rackList.get(0).getPowerValue()/ (idealSystemAirMassFlowRate * SPECIFIC_HEAT))));
	}
	public static void main(String []args){
		
		ParallelPlacementStrategy pp = new ParallelPlacementStrategy();
		
//		pp.computeFanPowerConsumption(20);
//		pp.computeMinMassFlowRate(20);
		System.out.println(pp.computeHeatRecirculation( (float)0.5, 20));
	}
	
	
}
