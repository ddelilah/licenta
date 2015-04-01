package app.access;

import java.util.List;

import app.model.Cooling;

public interface CoolingDAO {
	
	public List<Cooling> getAllCooling();

	public Cooling getCoolingById(int coolingId);


}
