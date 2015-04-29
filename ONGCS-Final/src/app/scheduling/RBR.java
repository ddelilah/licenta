package app.scheduling;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import app.access.RackDAO;
import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.algorithm.FFD;
import app.constants.VMState;
import app.execution.Execution;
import app.execution.History;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class RBR implements Serializable {

	private List<Rack> rackList= new ArrayList<Rack>();
	private List<VirtualMachine> vmList= new ArrayList<VirtualMachine>();
	private List<Server> serverList = new ArrayList<Server>();
	private VMProcessor vmProcessor= new VMProcessor(); 
	private RackProcessor rackProcessor = new RackProcessor();
	private SchedulingUtil schedulingUtil= new SchedulingUtil();
	public GenericDAOImpl dao = new GenericDAOImpl();
	public VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
	public RBR(){}

	@SuppressWarnings("unchecked")
	public Map<VirtualMachine, Server> placeVMsRackByRack(List<VirtualMachine> vmList, List<Rack> rackList) {
		
		
		
		rackProcessor = new RackProcessor(rackList);
		vmProcessor = new VMProcessor(vmList);
		Map<Server, List<Float>> resultOfOBFD = new HashMap<Server, List<Float>>();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		
		Server allocatedServer = new Server();
		rackList = rackProcessor.sortRackListDescending();
		vmList = vmProcessor.sortVMListDescending();
		Rack rack = new Rack();

		System.out.println("........... RBR ...................."+vmList.size());
	
		
		for (VirtualMachine vm : vmList) {
			
			if(selectSuitableRack(rackList, vm,allocation) != null){
				rack = selectSuitableRack(rackList, vm, allocation);

			serverList = rack.getServers();
			

			allocatedServer = null;
			OBFD obfd = new OBFD(serverList);
			if (!obfd.findAppropriateServer(vm,allocation).isEmpty()) {
				resultOfOBFD = obfd.findAppropriateServer(vm, allocation);

				for (Entry<Server, List<Float>> entry : resultOfOBFD
						.entrySet()) {
					allocatedServer = entry.getKey();
				}

			}
			

			if (allocatedServer != null) {
				allocation.put(vm, allocatedServer);

			}
			else{
				System.out.println("Allocation failed "+ vm.getName()+ vm.getVmId()+ vm.getState());
				vm.setState(VMState.FAILED.getValue());
				vmDAO.updateInstance(vm);
			}
		
		}
			else{
				System.out.println("Allocation failed "+ vm.getName()+ vm.getVmId()+ vm.getState());
				vm.setState(VMState.FAILED.getValue());
				vmDAO.updateInstance(vm);
			}
		}
		return allocation;
	}

	
	public Rack selectSuitableRack(List<Rack> rackList, VirtualMachine vm, Map<VirtualMachine, Server> allocation) {
		List<Server> serverList = new ArrayList<Server>();

		for (Rack rack : rackList) {
			serverList = rack.getServers();
			for (Server server : serverList)
				if (schedulingUtil.enoughResources(server, vm, allocation))
					return rack;
		}
		return null;
	}
	
	
	public static void main(String []args){
		RackDAO rackDAO = new RackDAOImpl();
		List<Rack> allRacks  = new ArrayList<Rack>();
		List<VirtualMachine> allVMs  = new ArrayList<VirtualMachine>();
		allRacks = rackDAO.getAllRacks();
		
		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();

		allVMs = vmDAO.getAllVMs();
		allRacks = rackDAO.getAllRacks();
		
		RBR rbr = new RBR();
		rbr.placeVMsRackByRack(allVMs, allRacks);
		
	}
}
