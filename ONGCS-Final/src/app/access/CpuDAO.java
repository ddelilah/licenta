package app.access;

import java.util.List;

import app.model.CPU;

public interface CpuDAO {

	public List<CPU> getAllCPUs();

	public CPU getCPUById(int cpuId);

}
