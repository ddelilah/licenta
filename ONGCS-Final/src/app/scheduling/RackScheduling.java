package app.scheduling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.access.ServerDAO;
import app.model.CPU;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class RackScheduling implements Serializable{

	private List<Rack> rackList;
	private List<VirtualMachine> vmList;
	private List<Server> serverList;
	private VMProcessor vmProcessor;
	private RackProcessor rackProcessor;

	
	public RackScheduling( List<Rack> rackList, List<VirtualMachine> vmList){
		this.rackList= rackList;
		this.vmList= vmList;
		
		serverList=new ArrayList<Server>();
		
		vmProcessor=new VMProcessor(vmList);
		rackProcessor = new RackProcessor(rackList);
		
	}
	
	@SuppressWarnings("unchecked")
	public void placeVMsRackByRack(){

		Map<Server,List<VirtualMachine>> allocation = new HashMap<Server,List<VirtualMachine>>();

		Server allocatedServer = new Server();
		rackList = rackProcessor.sortRackListDescending();
		vmList = vmProcessor.sortVMListDescending();
		Rack rack = new Rack();

		for(VirtualMachine vm: vmList){
			rack = selectSuitableRack(rackList, vm);
			
			serverList = rack.getServers();
	//		for(Server s: serverList){
				/*find appropriate sever*/
				OBFD obfd = new OBFD(serverList);
				allocatedServer = (Server) obfd.findAppropriateServer(vm).get(0);
				
		//	}
			List<VirtualMachine> updateList = null;
			if(allocatedServer!=null){
				updateList = allocation.get(allocatedServer);
				updateList.add(vm);
				allocation.put(allocatedServer,updateList);
				
			}
			
		}
	}
	
	public Rack selectSuitableRack(List<Rack> rackList, VirtualMachine vm){
		List<Server> serverList = new ArrayList<Server>();
		
			for(Rack rack: rackList){
				serverList= rack.getServers();
				for(Server server : serverList)
					if(enoughResources(server, vm))
						return rack;
			}
		return null;
	}
	
	public boolean enoughResources(Server server, VirtualMachine vmToCheck){
		List<VirtualMachine> vmList = server.getCorrespondingVMs();
		float totalNbCores =0;
		float totalCapacityRam=0;
		float totalCapacityHdd=0;
		
		for(VirtualMachine vm : vmList){
			totalNbCores += vm.getCpu().getNr_cores();
			totalCapacityRam += vm.getRam().getCapacity();
			totalCapacityHdd += vm.getHdd().getCapacity();
		}
		if( server.getCpu().getNr_cores() - totalNbCores > vmToCheck.getCpu().getNr_cores()
				&& server.getRam().getCapacity() - totalCapacityRam > vmToCheck.getRam().getCapacity()
				&& server.getHdd().getCapacity() -totalCapacityHdd > vmToCheck.getHdd().getCapacity() )
			return true;
		
		return false;
	}
	
	/*
	public static void main(String []args){
	
	Rack rack1= new Rack();
	rack1.setUtilization(79);
	Rack rack2= new Rack();
	rack2.setUtilization(50);
	Rack rack3= new Rack();
	rack3.setUtilization(99);
	Rack rack4= new Rack();
	rack4.setUtilization(1);
	List<Rack> rList = new ArrayList<Rack>();
	rList.add(rack1);
	rList.add(rack2);		
	rList.add(rack3);
	rList.add(rack4);
	CPU cpu1 = new CPU();
	cpu1.setCpu_utilization(12);
	CPU cpu2 = new CPU();
	cpu2.setCpu_utilization(55);
	CPU cpu3 = new CPU();
	cpu3.setCpu_utilization(15);
	VirtualMachine vm1 =new VirtualMachine();
	vm1.setCpu(cpu1);
	VirtualMachine vm2 =new VirtualMachine();
	vm2.setCpu(cpu2);
	VirtualMachine vm3 =new VirtualMachine();
	vm3.setCpu(cpu3);

	List<VirtualMachine> vList = new ArrayList<VirtualMachine>();
	vList.add(vm1);
	vList.add(vm2);		
	vList.add(vm3);
	RackScheduling rSch = new RackScheduling(rList, vList);
	rSch.placeVMsRackByRack();
}
*/
	
	
}
