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
import app.energy.Utilization;
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
	
	public static void executeNUR(List<VirtualMachine> allVMs,
			List<Rack> allRacks) {
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
			mergeSessionsForExecution(vm);
		}

		Utilization util = new Utilization();
		util.setServerUtilization();
		
		PowerConsumption power = new PowerConsumption();
		power.setServerPowerConsumption();
		power.setRackPowerConsumption();
		
		CoolingSimulation cooling = new CoolingSimulation();
		cooling.setServerCoolingValue();
		cooling.setRackCoolingPower();
		
		System.out.println("[NUR] map size: " + allocation.size());
		history.writeToFile(allocation, "historyNUR.txt");
	}

	public static void executeRBR(List<VirtualMachine> allVMs,
			List<Rack> allRacks) {
		
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		// create new instance of rackScheduling when having the vms required
/*		System.out.println(" size rbr"
				+ rackScheduling.placeVMsRackByRack(allVMs, allRacks).size());
	*/	allocation = rackScheduling.placeVMsRackByRack(allVMs, allRacks);
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			int serverId = entry.getValue().getServerId();
	/*		System.out.println("[RBR]vm " + entry.getKey().getName()
					+ " should be assigned to server with id " + serverId);
	*/		Server s = entry.getValue();
			VirtualMachine vm = entry.getKey();
			vm.setServer(s);
			mergeSessionsForExecution(vm);

		}
	
	
		Utilization util = new Utilization();
		util.setServerUtilization();
		
		turnOffUnusedServersAndRacks();
		
		PowerConsumption power = new PowerConsumption();
		power.setServerPowerConsumption();
		power.setRackPowerConsumption();
		power.comparePowerValues();
		
		CoolingSimulation cooling = new CoolingSimulation();
		cooling.setServerCoolingValue();
		cooling.setRackCoolingPower();
		
		History history = new History();
		history.writeToFile(allocation, "historyRBR.txt");
	}

	public void performFFD(List<VirtualMachine> allVMs){
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
	
		Utilization util = new Utilization();
		util.setServerUtilization();
		
		turnOffUnusedServersAndRacks();
		
		PowerConsumption power = new PowerConsumption();
		power.setServerPowerConsumption();
		power.setRackPowerConsumption();
		power.comparePowerValues();
		
		CoolingSimulation cooling = new CoolingSimulation();
		cooling.setServerCoolingValue();
		cooling.setRackCoolingPower();
		
	/*	History history = new History();
		history.writeToFile(allocation, "historyRBR.txt");
	*/	
	}

	private static void turnOffUnusedServersAndRacks(){
		
		List<Rack> allRacks = new ArrayList<Rack>();
		List<Server> allServers = new ArrayList<Server>();
		GenericDAOImpl genericDAO = new GenericDAOImpl();
		RackDAOImpl rackDAO = new RackDAOImpl();
		
		allRacks = rackDAO.getAllRacks();
		
		for(Rack rack: allRacks){
			allServers = rack.getServers();
			for(Server server: allServers){
					if(server.getUtilization() == 0){
						server.setState("off");
						genericDAO.updateInstance(server);
					}
					else{
						server.setState("on");
						genericDAO.updateInstance(server);
					}
				}
			if(rack.getPowerValue()!=0){
				rack.setState("on");
				genericDAO.updateInstance(rack);
			}
		}
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
	
	public static void main(String[] args) {
		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
		List<Rack> allRacks = new ArrayList<Rack>();
		RackDAOImpl rackDAO = new RackDAOImpl();

		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
		allVMs = vmDAO.getAllVMs();
		
		
		Utilization util = new Utilization();
		util.setServerUtilization();
		


		allRacks = rackDAO.getAllRacks();
		// rackScheduling = new RackScheduling(allRacks, allVMs);
		executeNUR(allVMs, allRacks);
	//	executeRBR(allVMs, allRacks);
		// executeRBR();
	
	
		
	}

}
