package app.scheduling;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.model.Server;
import app.model.VirtualMachine;

public class SchedulingUtil {
	
	public boolean enoughResources(Server server, VirtualMachine vmToCheck,Map<VirtualMachine, Server> map) {
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float totalRequiredMips = 0;
		float totalCapacityRam = 0;
		float totalCapacityHdd = 0;

		for (VirtualMachine vm : vmList) {
			totalRequiredMips += vm.getVmMips();
			totalCapacityRam += vm.getRam().getCapacity();
			totalCapacityHdd += vm.getHdd().getCapacity();
		}
		
		if(!map.isEmpty())
		for (Entry<VirtualMachine, Server> entry : map.entrySet()) {
			if(entry.getValue() != null)
			if (server.getServerId() == entry.getValue().getServerId()) {
				totalRequiredMips += entry.getKey().getVmMips();
				totalCapacityRam += entry.getKey().getRam().getCapacity();
				totalCapacityHdd += entry.getKey().getHdd().getCapacity();
			}
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
