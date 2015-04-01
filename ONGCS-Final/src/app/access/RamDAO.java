package app.access;

import java.util.List;

import app.model.RAM;

public interface RamDAO {

	public List<RAM> getAllRAMs();

	public RAM getRAMById(int ramId);

}
