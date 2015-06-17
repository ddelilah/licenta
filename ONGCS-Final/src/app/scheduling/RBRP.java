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
import java.util.TreeMap;

import app.access.RackDAO;
import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.constants.VMState;
import app.execution.Execution;
import app.execution.History;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.util.RackProcessor;
import app.util.SchedulingUtil;
import app.util.ServerProcessor;
import app.util.VMProcessor;

public class RBRP implements Serializable {

	private List<Rack> rackList= new ArrayList<Rack>();
	private List<VirtualMachine> vmList= new ArrayList<VirtualMachine>();
	private List<Server> serverList = new ArrayList<Server>();
	private VMProcessor vmProcessor= new VMProcessor(); 
	private RackProcessor rackProcessor = new RackProcessor();
	private SchedulingUtil schedulingUtil= new SchedulingUtil();
	public GenericDAOImpl dao = new GenericDAOImpl();
	public VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
	private String cracTemp;
	private ServerProcessor serverProc;
	private PABFD selectMostPowerEfficientHost;
	
	public RBRP(String cracTemp){
		
		this.cracTemp = cracTemp;
	}

	
	
public Map<VirtualMachine, Server> placeVMsRackByRack(List<VirtualMachine> vmList, List<Rack> rackList) {
		 Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		 Server allocatedServer = new Server();
		 Rack rack = new Rack();
		 Map<Server, List<Float>> resultOfOBFD = new HashMap<Server, List<Float>>();
		 ServerProcessor serverProcessor;
		 
		rackProcessor = new RackProcessor(rackList);
		vmProcessor = new VMProcessor(vmList);
		
		rackList = rackProcessor.sortRackListDescending();
		vmList = vmProcessor.sortVMListDescending();

		for (VirtualMachine vm : vmList) {
			// take all on servers;
			List<Server> onServerList = getAllOnServers(allocation);
			System.out.println("\n\n\n\n---------------- Virtual machine "+ vm.getName());

			if(! onServerList.isEmpty() && enoughResources(onServerList,vm,allocation)){
				
				serverProcessor = new ServerProcessor(onServerList);
				onServerList = serverProcessor.sortServerListDescending();
				
				allocatedServer = null;
				PABFD obfd = new PABFD(onServerList, cracTemp);
				if (!obfd.findAppropriateServer(vm,allocation).isEmpty()) {
					resultOfOBFD = obfd.findAppropriateServer(vm, allocation);

					for (Entry<Server, List<Float>> entry : resultOfOBFD.entrySet()) {
						allocatedServer = entry.getKey();
					}
				}
				
				if (allocatedServer != null) {
					allocation.put(vm, allocatedServer);
				}
				
			}else{
								
			if(selectSuitableRack(rackList, vm,allocation) != null){
				rack = selectSuitableRack(rackList, vm, allocation);

			serverList = rack.getServers();

			serverProcessor = new ServerProcessor(serverList);
			serverList = serverProcessor.sortServerListDescending();
			
			if(allocation.isEmpty())
			allocatedServer = null;
			PABFD obfd = new PABFD(serverList, cracTemp);
			if (!obfd.findAppropriateServer(vm,allocation).isEmpty()) {
				resultOfOBFD = obfd.findAppropriateServer(vm, allocation);

				for (Entry<Server, List<Float>> entry : resultOfOBFD.entrySet()) {
					allocatedServer = entry.getKey();
				}
			}
			else
				allocatedServer = getFirstOFFServer(rack, allocation);
			
			if (allocatedServer != null) {
				allocation.put(vm, allocatedServer);
			}
			else{
				vm.setState(VMState.FAILED.getValue());
				vmDAO.updateInstance(vm);
			}
		
		}
			else{
				vm.setState(VMState.FAILED.getValue());
				vmDAO.updateInstance(vm);
			}
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
	
//	public void getServerRemainingResources(Server server, Map<VirtualMachine, Server> allocation){
//		
//		List<VirtualMachine> vmList = server.getCorrespondingVMs();
//		int remainingMIPS = server.getServerMIPS();
//		int remainingCores = server.getCpu().getNr_cores();
//		float remainingHDD = server.getHdd().getCapacity();
//		float remainingRam = server.getRam().getCapacity();
////		System.out.println("Server's resources before allocation are: \n MIPS "+remainingMIPS+"\n Cores "+ remainingCores +"\n HDD "+remainingHDD+" MB \n Ram"
////				+remainingRam);
//		for(VirtualMachine vm: vmList){
//			remainingMIPS -= vm.getVmMips();
//			remainingCores -= vm.getCpu().getNr_cores();
//			remainingHDD -= vm.getHdd().getCapacity();
//			remainingRam -= vm.getRam().getCapacity();
//		}
//		for(Entry<VirtualMachine, Server> entry: allocation.entrySet()){
//			if(entry.getValue().getServerId() == server.getServerId())
//				remainingMIPS -= entry.getKey().getVmMips();
//				remainingCores -= entry.getKey().getCpu().getNr_cores();
//				remainingHDD -= entry.getKey().getHdd().getCapacity();
//				remainingRam -= entry.getKey().getRam().getCapacity();
//		}
////		System.out.println("Server's remaining resources after allocation are: \n MIPS "+remainingMIPS+"\n Cores "+ remainingCores +"\n HDD "+remainingHDD+" MB \n Ram"
////				+remainingRam);
//	}
	
	public List<Server> getAllOnServers(Map<VirtualMachine, Server> allocation){
		ServerDAOImpl serverDAO = new ServerDAOImpl();

		List<Server> onServerList = new ArrayList<>();
		onServerList = serverDAO.getAllServersByState("On");
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			boolean add = true;
			for(Server server : onServerList)
				if(server.getServerId() == entry.getValue().getServerId()){
					add = false;
					break;
				}
			if(add)
				onServerList.add(entry.getValue());
		}
		return onServerList;
	}
	
	public boolean enoughResources(List<Server> serverList, VirtualMachine vmToCheck,Map<VirtualMachine, Server> allocation){
		Server s = new Server();
		for(Server server: serverList )
			if(!schedulingUtil.enoughResources(server, vmToCheck, allocation))
				return false;
		return true;
	}
	
	public Server getFirstOFFServer(Rack rack, Map<VirtualMachine, Server> allocation){
		
		List<Server> serverListToCheck = rack.getServers();
		List<Server> offServerList = new ArrayList<>();
		
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			boolean off = true;
			for(Server server: serverListToCheck){
				if(server.getServerId() == entry.getValue().getServerId()){
					off =false;
					break;
				}
					if(off)
						offServerList.add(server);
			}
		}
		if(offServerList.get(0)!=null)
			return offServerList.get(0);
		return null;
	}
	
}
