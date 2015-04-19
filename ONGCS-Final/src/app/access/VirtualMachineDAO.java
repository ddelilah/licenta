package app.access;

import java.util.List;

import app.model.VirtualMachine;

public interface VirtualMachineDAO {
	
	public List<VirtualMachine> getAllVMs();

	public VirtualMachine getVirtualMachineById(int virtualMachineId);
	
	public List<VirtualMachine> getAllVMsByState(String state);
}
