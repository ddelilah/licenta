package app.execution;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import app.GUI.ChartAirflow;
import app.GUI.Charts;
import app.GUI.RackUtilizationGUI;
import app.access.impl.GenericDAOImpl;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.algorithm.FFD;
import app.constants.VMState;
import app.coolingSystems.HACS;
import app.coolingSystems.ParallelPlacementStrategy;
import app.energy.*;
import app.hibernate.SessionFactoryUtil;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.scheduling.NUR;
import app.scheduling.RBR;

import org.LiveGraph.dataFile.write.DataStreamWriter;
import org.LiveGraph.dataFile.write.DataStreamWriterFactory;

public class Execution {
	public static final String DEMO_DIR = System.getProperty("user.dir");
	
	private static NUR nur; 
	private static RBR rackScheduling ;
	private static History history = new History();
//	private static final float CRAC_SUPPLIED_TEMPERATURE = 20;
	private Utilization util = new Utilization();
	private PowerConsumption power = new PowerConsumption();
	private CoolingSimulation cooling ;
	private String cracTemp;
	
	public Execution(String cracTemp){
		this.cracTemp = cracTemp;
		nur = new NUR(cracTemp);
		rackScheduling = new RBR(cracTemp);
		cooling = new CoolingSimulation(Integer.parseInt(cracTemp));
	}
	
	public static List<VirtualMachine> addVmsToServer(Server s, VirtualMachine vm) {
		List<VirtualMachine> result = new ArrayList<VirtualMachine>();
		result = s.getCorrespondingVMs();
		result.add(vm);
		return result;
	}
	
	public void executeNUR(List<VirtualMachine> allVMs,
			List<Rack> allRacks, Charts chart, ChartAirflow chartAirflow) {
		
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
		int ct=0;
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			ct++;
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

			util.setSingleServerUtilization(s);
			util.setSingleRackUtilization(s.getRack());
			power.setSingleServerPowerConsumption(s);
			power.setSingleRackPowerConsumption(s.getRack());
			cooling.setSingleServerCoolingValue(s);
			cooling.setSingleRackCoolingValue(s.getRack());
			HACS hacs = new HACS();
			ParallelPlacementStrategy pp = new ParallelPlacementStrategy();

			

			float hacsAirMassFlowRate = hacs.computeMinMassFlowRate(Integer.parseInt(cracTemp));
			float hacsVolumetricAirFlow = hacs.computeVolumetricAirFlow(hacsAirMassFlowRate);	
			
			float parallelAirMassFlowRate01 = pp.computeHeatRecirculation(0.1f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow01 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate01);	
			float parallelAirMassFlowRate02 = pp.computeHeatRecirculation(0.2f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow02 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate02);	
			float parallelAirMassFlowRate03 = pp.computeHeatRecirculation(0.3f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow03 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate03);	
			float parallelAirMassFlowRate04 = pp.computeHeatRecirculation(0.4f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow04 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate04);	
			float parallelAirMassFlowRate05 = pp.computeHeatRecirculation(0.5f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow05 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate05);	
			chartAirflow.updatChartAirflow(hacsVolumetricAirFlow, 0, parallelVolumetricAirFlow01, parallelVolumetricAirFlow02, parallelVolumetricAirFlow03, parallelVolumetricAirFlow04, parallelVolumetricAirFlow05);

			chart.updateChartPowerConsumption(getCurrentPowerConsumption(), getCurrentCoolingPowerConsumption(), ct);

		}
		MigrationEfficiency mEff = new MigrationEfficiency();
		
//		History history = new History();
//		history.writeToFile(allVMs, initialNumberOffServers, allServers, allocation, "historyRBR.txt");

		System.out.println("Allocation Success Ratio: "+ mEff.computeAllocationMigrationRatio(allocation.size(), allVMs.size()));

		System.out.println("[NUR] map size: " + allocation.size());
		displayPowerConsumptionAndCooling("[BEFORE DELETE] NUR");

		 Thread.yield();
	        try { Thread.sleep(1000); } catch (InterruptedException e) {}
	      
	}
		
//	}

	public void executeRBR(List<VirtualMachine> allVMs,
			List<Rack> allRacks, Charts chart,  ChartAirflow chartAirflow) {
		
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
		
		int ct =0;

		
		
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			ct++;
			int serverId = entry.getValue().getServerId();
			System.out.println("[RBR]vm " + entry.getKey().getName()
					+ " should be assigned to server with id " + serverId);
			Server s = entry.getValue();
			VirtualMachine vm = entry.getKey();
			vm.setServer(s);
			vm.setState(VMState.RUNNING.getValue());
			s.setCorrespondingVMs(addVmsToServer(s, vm));
			mergeSessionsForExecution(vm);
			
			util.setSingleServerUtilization(s);
			util.setSingleRackUtilization(s.getRack());
			power.setSingleServerPowerConsumption(s);
			power.setSingleRackPowerConsumption(s.getRack());
			cooling.setSingleServerCoolingValue(s);
			cooling.setSingleRackCoolingValue(s.getRack());
	
			HACS hacs = new HACS();
			ParallelPlacementStrategy pp = new ParallelPlacementStrategy();

			float hacsAirMassFlowRate = hacs.computeMinMassFlowRate(Integer.parseInt(cracTemp));
			float hacsVolumetricAirFlow = hacs.computeVolumetricAirFlow(hacsAirMassFlowRate);	
			
			float parallelAirMassFlowRate01 = pp.computeHeatRecirculation(0.1f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow01 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate01);	
			float parallelAirMassFlowRate02 = pp.computeHeatRecirculation(0.2f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow02 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate02);	
			float parallelAirMassFlowRate03 = pp.computeHeatRecirculation(0.3f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow03 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate03);	
			float parallelAirMassFlowRate04 = pp.computeHeatRecirculation(0.4f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow04 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate04);	
			float parallelAirMassFlowRate05 = pp.computeHeatRecirculation(0.5f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow05 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate05);	
			chartAirflow.updatChartAirflow(hacsVolumetricAirFlow, 0, parallelVolumetricAirFlow01, parallelVolumetricAirFlow02, parallelVolumetricAirFlow03, parallelVolumetricAirFlow04, parallelVolumetricAirFlow05);

			chart.updateChartPowerConsumption(getCurrentPowerConsumption(), getCurrentCoolingPowerConsumption(), ct);

			 Thread.yield();
		        try { Thread.sleep(1000); } catch (InterruptedException e) {}
		      
		}
		MigrationEfficiency mEff = new MigrationEfficiency();
		util.setServerUtilization();

		power.setServerPowerConsumption();
		power.setRackPowerConsumption();

		cooling.setServerCoolingValue();
		cooling.setRackCoolingPower();
		util.setRackUtilization();
		
		
		History history = new History();
		history.writeToFile(allVMs, initialNumberOffServers, allServers, allocation, "historyRBR.txt");
		displayPowerConsumptionAndCooling("[BEFORE DELETE] RBR");
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
	//	System.out.println("\n\n\n\n "+algorithm+"Power: "+power + "Cooling: "+cooling);
		
		
	}

	public float getCurrentPowerConsumption(){
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
			}
			
		}
		return power;
	}

	
	public float getCurrentCoolingPowerConsumption(){
		List<VirtualMachine> allVMs = new ArrayList<VirtualMachine>();
		List<Rack> allRacks = new ArrayList<Rack>();
		RackDAOImpl rackDAO = new RackDAOImpl();
		List<Server> allServers = new ArrayList<Server>();
		List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
		allRacks = rackDAO.getAllRacks();
		float cooling=0;
		for(Rack rack: allRacks){
			allServers = rack.getServers();
			for(Server server: allServers){
				cooling += server.getCoolingValue();
			}
			
		}
		return cooling;
	}
	public void performFFD(List<VirtualMachine> allVMs, Charts chart,  ChartAirflow chartAirflow) {
		FFD ffd = new FFD();
		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		Map<VirtualMachine, Server> sortedAllocation = new HashMap<VirtualMachine, Server>();
		Map<VirtualMachine, Server> tiny = new HashMap<VirtualMachine, Server>();
		Map<VirtualMachine, Server> small = new HashMap<VirtualMachine, Server>();
		Map<VirtualMachine, Server> medium = new HashMap<VirtualMachine, Server>();
		Map<VirtualMachine, Server> large = new HashMap<VirtualMachine, Server>();
		Map<VirtualMachine, Server> xlarge = new HashMap<VirtualMachine, Server>();

		allocation = ffd.performFFD(allVMs);
		
		Utilization util = new Utilization();
		PowerConsumption power = new PowerConsumption();


		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			if(entry.getKey().getVmMips() == 250){
				tiny.put(entry.getKey(), entry.getValue());
			}
			else if(entry.getKey().getVmMips() == 400){
				small.put(entry.getKey(), entry.getValue());
			}
			else if(entry.getKey().getVmMips() == 600){
				medium.put(entry.getKey(), entry.getValue());
			}
			else if(entry.getKey().getVmMips() == 800){
				large.put(entry.getKey(), entry.getValue());
			}
			else if(entry.getKey().getVmMips() == 1000){
				xlarge.put(entry.getKey(), entry.getValue());
			}	
		}
		
		 sortedAllocation.putAll(xlarge);
		 sortedAllocation.putAll(large);
		 sortedAllocation.putAll(medium);
		 sortedAllocation.putAll(small);
		 sortedAllocation.putAll(tiny);

	
		int ct=0;
		for (Entry<VirtualMachine, Server> entry : sortedAllocation.entrySet()) {
			ct++;
			int serverId = entry.getValue().getServerId();
			
			System.out.println("\n\n...... Deploying vm "+ entry.getKey().getName()+ " on server "+entry.getValue().getServerId());
//			rGUI.updateTextArea("\n...... Deploying vm "+ entry.getKey().getName()+ " on server "+entry.getValue().getServerId());
//			rGUI.redirectSystemStreams();
//			rGUI.getTextArea().append("\n...... Deploying vm "+ entry.getKey().getName()+ " on server "+entry.getValue().getServerId());
			Server s = entry.getValue();
			VirtualMachine vm = entry.getKey();
			vm.setServer(s);
			s.setCorrespondingVMs(addVmsToServer(s, vm));
			mergeSessionsForExecution(vm);
			
			util.setServerUtilization();
			util.setRackUtilization();
			ffd.setServerPowerConsumption();
			power.setRackPowerConsumption();
			cooling.setServerCoolingValue();
			cooling.setRackCoolingPower();
			
			HACS hacs = new HACS();
			ParallelPlacementStrategy pp = new ParallelPlacementStrategy();

			

			float hacsAirMassFlowRate = hacs.computeMinMassFlowRate(Integer.parseInt(cracTemp));
			float hacsVolumetricAirFlow = hacs.computeVolumetricAirFlow(hacsAirMassFlowRate);	
			
			float parallelAirMassFlowRate01 = pp.computeHeatRecirculation(0.1f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow01 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate01);	
			float parallelAirMassFlowRate02 = pp.computeHeatRecirculation(0.2f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow02 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate02);	
			float parallelAirMassFlowRate03 = pp.computeHeatRecirculation(0.3f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow03 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate03);	
			float parallelAirMassFlowRate04 = pp.computeHeatRecirculation(0.4f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow04 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate04);	
			float parallelAirMassFlowRate05 = pp.computeHeatRecirculation(0.5f, Integer.parseInt(cracTemp));
			float parallelVolumetricAirFlow05 = hacs.computeVolumetricAirFlow(parallelAirMassFlowRate05);	
			chartAirflow.updatChartAirflow(hacsVolumetricAirFlow, 0, parallelVolumetricAirFlow01, parallelVolumetricAirFlow02, parallelVolumetricAirFlow03, parallelVolumetricAirFlow04, parallelVolumetricAirFlow05);

			chart.updateChartPowerConsumption(getCurrentPowerConsumption(), getCurrentCoolingPowerConsumption(), ct);
//			System.out.println("\n\n\n\nCurrent power "+ getCurrentPowerConsumption());
//		    System.out.println(hacsVolumetricAirFlow+" parallel " +parallelVolumetricAirFlow01);
//		    		    
		     Thread.yield();
		        try { Thread.sleep(3000); } catch (InterruptedException e) {}
		      
		}
		MigrationEfficiency mEff = new MigrationEfficiency();

		
		util.setServerUtilization();
		ffd.setServerPowerConsumption();
		power.setRackPowerConsumption();
		util.setRackUtilization();
		util.setRackUtilization();
	//	power.comparePowerValues();
		
		CoolingSimulation cooling = new CoolingSimulation(Integer.parseInt(cracTemp));
		cooling.setServerCoolingValue();
		cooling.setRackCoolingPower();
				
		
		System.out.println("Allocation Success Ratio: "+ mEff.computeAllocationMigrationRatio(allocation.size(), allVMs.size()));
		displayPowerConsumptionAndCooling("[BEFORE DELETE] FFD ");
		System.out.println("Demo finished. Cheers.");
		System.out.println("Demo finished. Cheers.");
		
//		charts.finishChartExecution();
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

}
