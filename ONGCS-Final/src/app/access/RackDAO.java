package app.access;

import java.util.List;

import app.model.Rack;

public interface RackDAO {

	public List<Rack> getAllRacks();

	public Rack getRackById(int rackId);

}
