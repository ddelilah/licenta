package app.scheduling;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.model.Server;
import app.model.VirtualMachine;

public class SchedulingUtil {
	
	public boolean enoughResources(Server server, VirtualMachine vmToCheck, Map<VirtualMachine, Server> map) {
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float totalRequiredMips = 0;
		float totalCapacityRam = 0;
		float totalCapacityHdd = 0;
		float potentialUtilization = 0;
		
		//nu stiu daca mai trebuie aici pentru ca lista de vms a serverului se updateaza in clipa in care se scrie in db => valorile din db
		//sunt salvate in functie de masinile virtuale pe care le gazduieste

//		for (VirtualMachine vm : vmList) {
//			totalRequiredMips += vm.getVmMips();
//			totalCapacityRam += vm.getRam().getCapacity();
//			totalCapacityHdd += vm.getHdd().getCapacity();
//		}
		
		//de valorile astea trebuie tinut cont pentru ca nu exista deja in db
		if(!map.isEmpty())
		for (Entry<VirtualMachine, Server> entry : map.entrySet()) {
			if(entry.getValue() != null)
			if (server.getServerId() == entry.getValue().getServerId()) {
				totalRequiredMips += entry.getKey().getVmMips();
				totalCapacityRam += entry.getKey().getRam().getCapacity();
				totalCapacityHdd += entry.getKey().getHdd().getCapacity();
			}
		}
		
		potentialUtilization = (totalRequiredMips + vmToCheck.getVmMips()) / server.getServerMIPS();

		if(server.getUtilization() > 0.2) {
			if (potentialUtilization > 0.2 && potentialUtilization < 0.8
					&& server.getRam().getCapacity() - totalCapacityRam > vmToCheck
							.getRam().getCapacity()
					&& server.getHdd().getCapacity() - totalCapacityHdd > vmToCheck
							.getHdd().getCapacity())
				return true;
			else return false;
		} else {
			if (potentialUtilization < 0.8
					&& server.getRam().getCapacity() - totalCapacityRam > vmToCheck
							.getRam().getCapacity()
					&& server.getHdd().getCapacity() - totalCapacityHdd > vmToCheck
							.getHdd().getCapacity())
				return true;
			else return false;
		}
	}

}
