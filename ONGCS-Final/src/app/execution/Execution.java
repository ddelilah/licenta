package app.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.algorithm.FFD;
import app.constants.VMState;
import app.energy.CoolingSimulation;
import app.energy.PowerConsumption;
import app.energy.*;
import app.hibernate.SessionFactoryUtil;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.scheduling.NUR;
import app.scheduling.RBR;

public class Execution {

	private static NUR nur = new NUR();
	private static RBR rackScheduling = new RBR();
	private static History history = new History();
	
	public static List<VirtualMachine> addVmsToServer(Server s, VirtualMachine vm) {
		List<VirtualMachine> result = new ArrayList<VirtualMachine>();
		result = s.getCorrespondingVMs();
		result.add(vm);
		return result;
	}
	
	public void executeNUR(List<VirtualMachine> allVMs,
			List<Rack> allRacks) {
		
		ServerDAOImpl serverDAO = new ServerDAOImpl();
		List<Server> allServers = serverDAO.getAllServers();

		int initialNumberOnServers = serverDAO.getAllServersByState("on").size();
		int initialNumberOffServers = allServers.size() - initialNumberOnServers;
		
//		Learning l = new Learning();
//		boolean foundLearning = false;
//		try {
//			foundLearning = l.learning(allVMs, initialNumberOffServers, allServers, "historyRBR.txt");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("\n\n\n\n[Execution] foundLearning " +foundLearning);
//		
//		if(foundLearning){
//			System.out.println("\n\n\n ----------------- Experiment already done! --------------------------");
//			displayPowerConsumptionAndCooling("NUR");
//			}
//		
//	else{
			System.out.println("\n\n\n ----------------- Performing experiment! --------------------------");
		
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		allocation = nur.placeVMsInNoneUnderutilizedRack(allVMs, allRacks);
		
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			int serverId = entry.getValue().getServerId();
			System.out.println("[NUR]vm " + entry.getKey().getName()
					+ " should be assigned to server with id " + serverId);
			Server s = entry.getValue();
			VirtualMachine vm = entry.getKey();
			vm.setServer(s);
			vm.setState(VMState.RUNNING.getValue());
			s.setCorrespondingVMs(addVmsToServer(s, vm));
			System.out.println("Added VMs server's list of VMs: " + s.getCorrespondingVMs());
			mergeSessionsForExecution(vm);
	
		}
		MigrationEfficiency mEff = new MigrationEfficiency();

		//trebuie ca de dinainte sa fie valorile ok
		
		System.out.println("\n\n Utilization Computation for all servers..............");
		Utilization util = new Utilization();
		util.setServerUtilization();
		
		System.out.println("\n\n Power consumption Computation for all servers..............");
		PowerConsumption power = new PowerConsumption();
		power.setServerPowerConsumption();
		
		System.out.println("\n\n Power consumption Computation for all racks..............");
		power.setRackPowerConsumption();
		
		
		CoolingSimulation cooling = new CoolingSimulation();
		System.out.println("\n\n Cooling Computation for all servers..............");
		cooling.setServerCoolingValue();
		
		System.out.println("\n\n Cooling Computation for all racks..............");
		cooling.setRackCoolingPower();
		
//		History history = new History();
//		history.writeToFile(allVMs, initialNumberOffServers, allServers, allocation, "historyRBR.txt");
		
		System.out.println("\n\n Utilization Computation for all racks..............");
		util.setRackUtilization();
		System.out.println("Allocation Success Ratio: "+ mEff.computeAllocationMigrationRatio(allocation.size(), allVMs.size()));

		System.out.println("[NUR] map size: " + allocation.size());
		

	}
		
//	}

	public void executeRBR(List<VirtualMachine> allVMs,
			List<Rack> allRacks) {
		
		ServerDAOImpl serverDAO = new ServerDAOImpl();
		List<Server> allServers = serverDAO.getAllServers();

		int initialNumberOnServers = serverDAO.getAllServersByState("on").size();
		int initialNumberOffServers = allServers.size() - initialNumberOnServers;
		
//		Learning l = new Learning();
//		boolean foundLearning = false;
//		try {
//			foundLearning = l.learning(allVMs, initialNumberOffServers, allServers, "historyRBR.txt");
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		System.out.println("\n\n\n\n[Execution] foundLearning " +foundLearning);
//		
//
//		if(foundLearning){
//			System.out.println("\n\n\n ----------------- Experiment already done! --------------------------");
//			displayPowerConsumptionAndCooling("RBR");
//			}
//		
//	else{
			System.out.println("\n\n\n ----------------- Performing experiment! --------------------------");
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		allocation = rackScheduling.placeVMsRackByRack(allVMs, allRacks);
		
		int unsuccessfulMigrations = allVMs.size() - allocation.size();
		int successfulMigrations = allocation.size();
		
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			int serverId = entry.getValue().getServerId();
			System.out.println("[RBR]vm " + entry.getKey().getName()
					+ " should be assigned to server with id " + serverId);
			Server s = entry.getValue();
			VirtualMachine vm = entry.getKey();
			vm.setServer(s);
			vm.setState(VMState.RUNNING.getValue());
			mergeSessionsForExecution(vm);
		}
		MigrationEfficiency mEff = new MigrationEfficiency();
		Utilization util = new Utilization();
		util.setServerUtilization();

		PowerConsumption power = new PowerConsumption();
		power.setServerPowerConsumption();
		power.setRackPowerConsumption();

		CoolingSimulation cooling = new CoolingSimulation();
		cooling.setServerCoolingValue();
		cooling.setRackCoolingPower();
		util.setRackUtilization();
		
		
		History history = new History();
		history.writeToFile(allVMs, initialNumberOffServers, allServers, allocation, "historyRBR.txt");
		displayPowerConsumptionAndCooling("RBR");
		System.out.println("Allocation Success Ratio: "+ mEff.computeAllocationMigrationRatio(allocation.size(), allVMs.size()));
//		History history = new History();
//		history.writeToFile(allocation, "historyRBR.txt");

	}	

//	}

	public static void displayPowerConsumptionAndCooling(String algorithm){
		
		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
		List<Rack> allRacks = new ArrayList<Rack>();
		RackDAOImpl rackDAO = new RackDAOImpl();
		List<Server> allServers = new ArrayList<Server>();
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		allRacks = rackDAO.getAllRacks();
		float power=0, cooling=0;
		for(Rack rack: allRacks){
			allServers = rack.getServers();
			for(Server server: allServers){
				power+=server.getPowerValue();
				cooling += server.getCoolingValue();
			}
			
		}
		System.out.println("\n\n\n\n "+algorithm+"Power: "+power + "Cooling: "+cooling);
		
		
	}

	public void performFFD(List<VirtualMachine> allVMs) {
		FFD ffd = new FFD();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		allocation = ffd.performFFD(allVMs);
		
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			int serverId = entry.getValue().getServerId();
			Server s = entry.getValue();
			VirtualMachine vm = entry.getKey();
			vm.setServer(s);
			mergeSessionsForExecution(vm);
		}
		MigrationEfficiency mEff = new MigrationEfficiency();

		Utilization util = new Utilization();
		util.setServerUtilization();
		
		PowerConsumption power = new PowerConsumption();
		power.setServerPowerConsumption();
		power.setRackPowerConsumption();
		power.comparePowerValues();
		
		CoolingSimulation cooling = new CoolingSimulation();
		cooling.setServerCoolingValue();
		cooling.setRackCoolingPower();
		System.out.println("Allocation Success Ratio: "+ mEff.computeAllocationMigrationRatio(allocation.size(), allVMs.size()));

	/*	History history = new History();
		history.writeToFile(allocation, "historyRBR.txt");
	*/	
	}
	
	private static String mergeSessionsForExecution(VirtualMachine vm) {
		Session session = SessionFactoryUtil.getInstance().openSession();
		Query query = session
				.createQuery("from VirtualMachine vm where vm.vmId=:vm_id");
		List<VirtualMachine> queryList = query.setParameter("vm_id",
				vm.getVmId()).list();
		session.close();
		Session session2 = SessionFactoryUtil.getInstance().openSession();
		try {
			if (queryList.size() > 0) {
				session2.beginTransaction();
				VirtualMachine v = (VirtualMachine) session2.get(
						VirtualMachine.class, new Integer(213));
				session2.merge(vm);
				// session2.update(vm);
			} else {
				session2.beginTransaction();
				session2.save(vm);
			}
		} catch (HibernateException e) {
			session2.getTransaction().rollback();
			System.out
					.println("Getting Exception : " + e.getLocalizedMessage());
		} finally {
			session2.getTransaction().commit();
			session2.close();
		}

		return "Successfully data updated into table";

	}

	public void initialConsolidationRBR(){
		
		Utilization util = new Utilization();
		util.setServerUtilization();
		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
		List<Rack> allRacks = new ArrayList<Rack>();
		RackDAOImpl rackDAO = new RackDAOImpl();
		List<Server> allServers = new ArrayList<Server>();
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		allRacks = rackDAO.getAllRacks();
		
		for(Rack rack: allRacks){
			allServers = rack.getServers();
			for(Server server: allServers){
				vmList = server.getCorrespondingVMs();
				for(VirtualMachine vm1: vmList)
				allVMs.add(vm1);
			}
		}
		
		executeRBR(allVMs, allRacks);
	}
	
public void initialConsolidationFFD(){
		
		Utilization util = new Utilization();
		util.setServerUtilization();
		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
		List<Rack> allRacks = new ArrayList<Rack>();
		RackDAOImpl rackDAO = new RackDAOImpl();
		List<Server> allServers = new ArrayList<Server>();
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		allRacks = rackDAO.getAllRacks();
		
		for(Rack rack: allRacks){
			allServers = rack.getServers();
			for(Server server: allServers){
				vmList = server.getCorrespondingVMs();
				for(VirtualMachine vm1: vmList)
				allVMs.add(vm1);
			}
		}
		
		performFFD(allVMs);
	}
	
public void initialConsolidationNUR(){
		
		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
		List<Rack> allRacks = new ArrayList<Rack>();
		RackDAOImpl rackDAO = new RackDAOImpl();
		List<Server> allServers = new ArrayList<Server>();
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		allRacks = rackDAO.getAllRacks();
		
		for(Rack rack: allRacks){
			allServers = rack.getServers();
			for(Server server: allServers){
				vmList = server.getCorrespondingVMs();
				for(VirtualMachine vm1: vmList)
				allVMs.add(vm1);
			}
		}
		
		Utilization util = new Utilization();
		util.setServerUtilization();
		executeNUR(allVMs, allRacks);
	}

	public static void main(String []args){
		Execution execution = new Execution();
		RackDAOImpl rackDAO = new RackDAOImpl();
		VirtualMachineDAOImpl vmDAO= new VirtualMachineDAOImpl();
		
		List<Rack> rackList = rackDAO.getAllRacks();
		List<VirtualMachine> vmList = vmDAO.getAllVMs();
		
		execution.executeRBR(vmList, rackList);
		
	}

}
