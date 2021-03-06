package app.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.access.impl.RackDAOImpl;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class SchedulingUtil {

	public static List<VirtualMachine> addVmsToServer(Server s,
			VirtualMachine vm) {
		List<VirtualMachine> result = new ArrayList<VirtualMachine>();
		result = s.getCorrespondingVMs();
		result.add(vm);
		return result;
	}

	public static List<VirtualMachine> updateVmsOnServer(Server s,
			VirtualMachine vm) {
		List<VirtualMachine> result = new ArrayList<VirtualMachine>();
		result = s.getCorrespondingVMs();
		result.remove(vm);
		return result;
	}

	public boolean enoughResources(Server server, VirtualMachine vmToCheck,
			Map<VirtualMachine, Server> map) {
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float totalRequiredMips = 0;
		float totalCapacityRam = 0;
		float totalCapacityHdd = 0;
		float potentialUtilization = 0;

		for (VirtualMachine vm : vmList) {
			totalRequiredMips += vm.getVmMips();
			totalCapacityRam += vm.getRam().getCapacity();
			totalCapacityHdd += vm.getHdd().getCapacity();
		}

		if (!map.isEmpty()) {
			for (Entry<VirtualMachine, Server> entry : map.entrySet()) {
				if (entry.getValue() != null)
					if (server.getServerId() == entry.getValue().getServerId()) {
						totalRequiredMips += entry.getKey().getVmMips();
						totalCapacityRam += entry.getKey().getRam()
								.getCapacity();
						totalCapacityHdd += entry.getKey().getHdd()
								.getCapacity();
					}
			}
		}

		potentialUtilization = (totalRequiredMips + vmToCheck.getVmMips())
				/ server.getServerMIPS();

		if (server.getUtilization() > 0.2) {
			if (potentialUtilization > 0.2
					&& potentialUtilization < 0.8
					&& server.getRam().getCapacity() - totalCapacityRam > vmToCheck
							.getRam().getCapacity()
					&& server.getHdd().getCapacity() - totalCapacityHdd > vmToCheck
							.getHdd().getCapacity())
				return true;
			else
				return false;
		} else {
			if (potentialUtilization < 0.8
					&& server.getRam().getCapacity() - totalCapacityRam > vmToCheck
							.getRam().getCapacity()
					&& server.getHdd().getCapacity() - totalCapacityHdd > vmToCheck
							.getHdd().getCapacity())
				return true;
			else
				return false;
		}
	}

	public static void displayPowerConsumptionAndCooling(String algorithm) {

		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
		List<Rack> allRacks = new ArrayList<Rack>();
		RackDAOImpl rackDAO = new RackDAOImpl();
		List<Server> allServers = new ArrayList<Server>();
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		allRacks = rackDAO.getAllRacks();
		float power = 0, cooling = 0;
		for (Rack rack : allRacks) {
			allServers = rack.getServers();
			for (Server server : allServers) {
				power += server.getPowerValue();
				cooling += server.getCoolingValue();
			}

		}
		System.out.println("\n\n\n\n " + algorithm + "Power: " + power
				+ "Cooling: " + cooling);

	}

	public float getCurrentPowerConsumption() {
		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
		List<Rack> allRacks = new ArrayList<Rack>();
		RackDAOImpl rackDAO = new RackDAOImpl();
		List<Server> allServers = new ArrayList<Server>();
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		allRacks = rackDAO.getAllRacks();
		float power = 0, cooling = 0;
		for (Rack rack : allRacks) {
			allServers = rack.getServers();
			for (Server server : allServers) {
				power += server.getPowerValue();
			}

		}
		return power;
	}

	public float getCurrentCoolingPowerConsumption() {
		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
		List<Rack> allRacks = new ArrayList<Rack>();
		RackDAOImpl rackDAO = new RackDAOImpl();
		List<Server> allServers = new ArrayList<Server>();
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		allRacks = rackDAO.getAllRacks();
		float cooling = 0;
		for (Rack rack : allRacks) {
			allServers = rack.getServers();
			for (Server server : allServers) {
				cooling += server.getCoolingValue();
			}

		}
		return cooling;
	}

}
