package app.coolingSystems;

import java.util.List;

import app.model.Rack;

public class AirMass {

	public float getAirMass(List<Rack> rackList, float coolingPower, float tIn, float tOut){
		
		float totalPower = 0;
		for(Rack rack: rackList)
			totalPower += rack.getPowerValue();
		
		return totalPower/(coolingPower * ( tOut - tIn));
	}
	
}
