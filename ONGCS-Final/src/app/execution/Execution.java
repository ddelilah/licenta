package app.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.access.impl.CpuDAOImpl;
import app.access.impl.HddDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.RamDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.constants.VMState;
import app.model.CPU;
import app.model.HDD;
import app.model.RAM;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.scheduling.NUR;
import app.scheduling.RackScheduling;

public class Execution {
	
	private NUR nur = new NUR();
	private static RackScheduling rackScheduling;
	
	public void executeNUR(List<VirtualMachine> allVMs, List<Rack> allRacks) {
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		allocation = nur.placeVMsInNoneUnderutilizedRack(allVMs, allRacks);
		
		for(Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			int serverId = entry.getValue().getServerId();
			System.out.println("vm " + entry.getKey().getName() + " should be assigned to server with id " + serverId);
			
		}
	}
	
	public void executeRBR() {
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		//create new instance of rackScheduling when having the vms required for scheduling
		allocation = rackScheduling.placeVMsRackByRack();
		for(Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			int serverId = entry.getValue().getServerId();
			System.out.println("[RBR]vm " + entry.getKey().getName() + " should be assigned to server with id " + serverId);
			
		}
		
	}
	
//	public static void main(String[] args) {
//		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
//		List<Rack> allRacks = new ArrayList<Rack>();
//
//		CPU cpu1, cpu2;
//		HDD hdd1, hdd2;
//		RAM ram1, ram2;
//		
//		CpuDAOImpl cpuDAO = new CpuDAOImpl();
//		HddDAOImpl hddDAO = new HddDAOImpl();
//		RamDAOImpl ramDAO = new RamDAOImpl();
//		
//		cpu1 = cpuDAO.getCPUById(1);
//		cpu2 = cpuDAO.getCPUById(2);
//		
//		hdd1 = hddDAO.getHDDById(1);
//		hdd2 = hddDAO.getHDDById(2);
//		
//		ram1 = ramDAO.getRAMById(1);
//		ram2 = ramDAO.getRAMById(2);
//		
//		VirtualMachine vm = new VirtualMachine();
//		vm.setCpu(cpu1);
//		vm.setHdd(hdd1);
//		vm.setRam(ram1);
//		vm.setName("vm1_testScheduling");
//		vm.setVmMips(250);
//		vm.setState(VMState.SHUT_DOWN);
//		
//		VirtualMachine vm2 = new VirtualMachine();
//		vm2.setCpu(cpu2);
//		vm2.setHdd(hdd2);
//		vm2.setRam(ram2);
//		vm2.setName("vm2_testScheduling");
//		vm2.setVmMips(250);
//		vm2.setState(VMState.SHUT_DOWN);
//		
//		allVMs.add(vm);
//		allVMs.add(vm2);
//
//		RackDAOImpl rackDAO = new RackDAOImpl();
//		allRacks = rackDAO.getAllRacks();
//		rackScheduling = new RackScheduling(allRacks, allVMs);
//	//	executeNUR(allVMs, allRacks);
//		executeRBR();
//	}

}
