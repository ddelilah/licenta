package app.scheduling;

import java.util.List;

import app.model.Server;
import app.model.VirtualMachine;

public class SchedulingUtil {
	
	public boolean enoughResources(Server server, VirtualMachine vmToCheck) {
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float totalRequiredMips = 0;
		float totalCapacityRam = 0;
		float totalCapacityHdd = 0;

		for (VirtualMachine vm : vmList) {
			totalRequiredMips += vm.getVmMips();
			totalCapacityRam += vm.getRam().getCapacity();
			totalCapacityHdd += vm.getHdd().getCapacity();
		}
		if (server.getServerMIPS() - totalRequiredMips > vmToCheck.getVmMips()
				&& server.getRam().getCapacity() - totalCapacityRam > vmToCheck
						.getRam().getCapacity()
				&& server.getHdd().getCapacity() - totalCapacityHdd > vmToCheck
						.getHdd().getCapacity())
			return true;

		return false;
	}

}
