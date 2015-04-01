package app.access;

import java.util.List;

import app.model.HDD;

public interface HddDAO {

	public List<HDD> getAllHDDs();

	public HDD getHDDById(int hddId);

	
	
}
