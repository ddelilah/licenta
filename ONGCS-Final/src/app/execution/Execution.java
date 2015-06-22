package app.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import app.GUI.ChartAirflow;
import app.GUI.Charts;
import app.access.impl.RackDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.analysis.Cache;
import app.constants.VMState;
import app.coolingSystems.HACS;
import app.coolingSystems.ParallelPlacementStrategy;
import app.energy.CoolingSimulation;
import app.energy.MigrationEfficiency;
import app.energy.PowerConsumption;
import app.energy.Utilization;
import app.hibernate.SessionFactoryUtil;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.scheduling.FFD;
import app.scheduling.NURF;
import app.scheduling.RBRP;
import app.util.SchedulingUtil;

public class Execution {
	public static final String DEMO_DIR = System.getProperty("user.dir");

	private static NURF nur;
	private static RBRP rackScheduling;
	private static History history = new History();
	private Utilization util = new Utilization();
	private PowerConsumption power = new PowerConsumption();

	private CoolingSimulation cooling;
	private String cracTemp;

	public Execution(String cracTemp) {
		this.cracTemp = cracTemp;
		nur = new NURF(cracTemp);
		rackScheduling = new RBRP(cracTemp);
		cooling = new CoolingSimulation(Integer.parseInt(cracTemp));
	}

	private SchedulingUtil sUtil = new SchedulingUtil();

	public void executeNURFailed(List<VirtualMachine> allVMs,
			List<Rack> allRacks, Charts chart, ChartAirflow chartAirflow) {

		ServerDAOImpl serverDAO = new ServerDAOImpl();
		List<Server> allServers = serverDAO.getAllServers();

		System.out
				.println("\n\n\n ----------------- Performing experiment! --------------------------");

		Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
		allocation = nur.placeVMsNURAfterFailed(allVMs, allRacks);
		int ct = 0;
		for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
			ct++;
			Server s = entry.getValue();
			VirtualMachine vm = entry.getKey();
			vm.setServer(s);
			vm.setState(VMState.RUNNING.getValue());
			s.setCorrespondingVMs(sUtil.addVmsToServer(s, vm));
			mergeSessionsForExecution(vm);
			util.setSingleServerUtilization(s);
			util.setSingleRackUtilization(s.getRack());
			power.setSingleServerPowerConsumptionGivenUtilization(s,
					s.getUtilization());
			power.setSingleRackPowerConsumption(s.getRack());
			cooling.setSingleServerCoolingValueGivenPowerConsumption(s,
					s.getPowerValue());
			cooling.setSingleRackCoolingValue(s.getRack());
			chart.updateChartPowerConsumption(sUtil.getCurrentPowerConsumption(),
					sUtil.getCurrentCoolingPowerConsumption(), ct);

		}
		MigrationEfficiency mEff = new MigrationEfficiency();

		System.out.println("Allocation Success Ratio: "
				+ mEff.computeAllocationMigrationRatio(allocation.size(),
						allVMs.size()));

		System.out.println("[NUR] map size: " + allocation.size());
		sUtil.displayPowerConsumptionAndCooling("[BEFORE DELETE] NUR");

	}

	public void executeNUR(List<VirtualMachine> allVMs, List<Rack> allRacks,
			Charts chart, ChartAirflow chartAirflow) {

		ServerDAOImpl serverDAO = new ServerDAOImpl();
		List<Server> allServers = serverDAO.getAllServers();

		int initialNumberOnServers = serverDAO.getAllServersByState("on")
				.size();
		int initialNumberOffServers = allServers.size()
				- initialNumberOnServers;

		Cache l = new Cache();
		boolean foundLearning = false;
		try {
			foundLearning = l.learning(allVMs, initialNumberOffServers,
					allServers, "historyNUR.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out
				.println("\n\n\n\n[Execution] foundLearning " + foundLearning);

		if (foundLearning) {
			System.out
					.println("\n\n\n ----------------- Experiment already done! --------------------------");

			sUtil.displayPowerConsumptionAndCooling("NUR ");

			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(1);
		}

		else {
			System.out
					.println("\n\n\n ----------------- Performing experiment! --------------------------");

			Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
			allocation = nur.placeVMsInNoneUnderutilizedRack(allVMs, allRacks);
			VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
			List<VirtualMachine> vmList = vmDAO.getAllVMsByState("Running");
			int ct = vmList.size();

			for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
				ct++;
				int serverId = entry.getValue().getServerId();
				Server s = entry.getValue();
				VirtualMachine vm = entry.getKey();
				vm.setServer(s);
				vm.setState(VMState.RUNNING.getValue());
				s.setCorrespondingVMs(sUtil.addVmsToServer(s, vm));
				mergeSessionsForExecution(vm);

				util.setSingleServerUtilization(s);
				util.setSingleRackUtilization(s.getRack());
				power.setSingleServerPowerConsumptionGivenUtilization(s,
						s.getUtilization());
				power.setSingleRackPowerConsumption(s.getRack());
				cooling.setSingleServerCoolingValueGivenPowerConsumption(s,
						s.getPowerValue());
				cooling.setSingleRackCoolingValue(s.getRack());
				HACS hacs = new HACS();
				ParallelPlacementStrategy pp = new ParallelPlacementStrategy();

				float hacsAirMassFlowRate = hacs.computeMinMassFlowRate(Integer
						.parseInt(cracTemp));
				float hacsVolumetricAirFlow = hacs
						.computeVolumetricAirFlow(hacsAirMassFlowRate);

				float parallelAirMassFlowRate01 = pp.computeHeatRecirculation(
						0.1f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow01 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate01);
				float parallelAirMassFlowRate02 = pp.computeHeatRecirculation(
						0.2f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow02 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate02);
				float parallelAirMassFlowRate03 = pp.computeHeatRecirculation(
						0.3f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow03 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate03);
				float parallelAirMassFlowRate04 = pp.computeHeatRecirculation(
						0.4f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow04 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate04);
				float parallelAirMassFlowRate05 = pp.computeHeatRecirculation(
						0.5f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow05 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate05);
				chartAirflow.updatChartAirflow(hacsVolumetricAirFlow, 0,
						parallelVolumetricAirFlow01,
						parallelVolumetricAirFlow02,
						parallelVolumetricAirFlow03,
						parallelVolumetricAirFlow04,
						parallelVolumetricAirFlow05);

				chart.updateChartPowerConsumption(sUtil.getCurrentPowerConsumption(),
						sUtil.getCurrentCoolingPowerConsumption(), ct);

			}
			MigrationEfficiency mEff = new MigrationEfficiency();

			System.out.println("Allocation Success Ratio: "
					+ mEff.computeAllocationMigrationRatio(allocation.size(),
							allVMs.size()));

			System.out.println("[NUR] map size: " + allocation.size());
			sUtil.displayPowerConsumptionAndCooling("[BEFORE DELETE] NUR");
			History history = new History();
			history.writeToFile(allVMs, initialNumberOffServers, allServers,
					allocation, "historyNUR.txt");

			Thread.yield();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}


	public void executeRBR(List<VirtualMachine> allVMs, List<Rack> allRacks,
			Charts chart, ChartAirflow chartAirflow) {

		ServerDAOImpl serverDAO = new ServerDAOImpl();
		List<Server> allServers = serverDAO.getAllServers();

		int initialNumberOnServers = serverDAO.getAllServersByState("on")
				.size();
		int initialNumberOffServers = allServers.size()
				- initialNumberOnServers;

		Cache l = new Cache();
		boolean foundLearning = false;
		try {
			foundLearning = l.learning(allVMs, initialNumberOffServers,
					allServers, "historyRBR.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out
				.println("\n\n\n\n[Execution] foundLearning " + foundLearning);

		if (foundLearning) {

			System.out
					.println("\n\n\n ----------------- Experiment already done! --------------------------");

			sUtil.displayPowerConsumptionAndCooling("RBR ");
			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(1);
		}

		else {
			System.out
					.println("\n\n\n ----------------- Performing experiment! --------------------------");
			Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();
			allocation = rackScheduling.placeVMsRackByRack(allVMs, allRacks);

			VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
			List<VirtualMachine> vmList = vmDAO.getAllVMsByState("Running");
			int ct = vmList.size();

			for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
				ct++;
				int serverId = entry.getValue().getServerId();
				System.out.println("[RBR] vm " + entry.getKey().getName()
						+ " is assigned to server with id " + serverId);
				Server s = entry.getValue();
				VirtualMachine vm = entry.getKey();
				vm.setServer(s);
				vm.setState(VMState.RUNNING.getValue());
				s.setCorrespondingVMs(sUtil.addVmsToServer(s, vm));
				mergeSessionsForExecution(vm);

				util.setSingleServerUtilization(s);
				util.setSingleRackUtilization(s.getRack());
				power.setSingleServerPowerConsumptionGivenUtilization(s,
						s.getUtilization());
				power.setSingleRackPowerConsumption(s.getRack());
				cooling.setSingleServerCoolingValueGivenPowerConsumption(s,
						s.getPowerValue());
				cooling.setSingleRackCoolingValue(s.getRack());

				HACS hacs = new HACS();
				ParallelPlacementStrategy pp = new ParallelPlacementStrategy();

				float hacsAirMassFlowRate = hacs.computeMinMassFlowRate(Integer
						.parseInt(cracTemp));
				float hacsVolumetricAirFlow = hacs
						.computeVolumetricAirFlow(hacsAirMassFlowRate);

				float parallelAirMassFlowRate01 = pp.computeHeatRecirculation(
						0.1f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow01 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate01);
				float parallelAirMassFlowRate02 = pp.computeHeatRecirculation(
						0.2f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow02 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate02);
				float parallelAirMassFlowRate03 = pp.computeHeatRecirculation(
						0.3f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow03 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate03);
				float parallelAirMassFlowRate04 = pp.computeHeatRecirculation(
						0.4f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow04 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate04);
				float parallelAirMassFlowRate05 = pp.computeHeatRecirculation(
						0.5f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow05 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate05);
				chartAirflow.updatChartAirflow(hacsVolumetricAirFlow, 0,
						parallelVolumetricAirFlow01,
						parallelVolumetricAirFlow02,
						parallelVolumetricAirFlow03,
						parallelVolumetricAirFlow04,
						parallelVolumetricAirFlow05);

				chart.updateChartPowerConsumption(sUtil.getCurrentPowerConsumption(),
						sUtil.getCurrentCoolingPowerConsumption(), ct);

				Thread.yield();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

			}
			MigrationEfficiency mEff = new MigrationEfficiency();

			History history = new History();
			history.writeToFile(allVMs, initialNumberOffServers, allServers,
					allocation, "historyRBR.txt");
			sUtil.displayPowerConsumptionAndCooling("[BEFORE DELETE] RBR");
			System.out.println("Allocation Success Ratio: "
					+ mEff.computeAllocationMigrationRatio(allocation.size(),
							allVMs.size()));

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

	}

	public void performFFD(List<VirtualMachine> allVMs, Charts chart,
			ChartAirflow chartAirflow) {
		FFD ffd = new FFD();

		ServerDAOImpl serverDAO = new ServerDAOImpl();
		List<Server> allServers = serverDAO.getAllServers();

		int initialNumberOnServers = serverDAO.getAllServersByState("on")
				.size();
		int initialNumberOffServers = allServers.size()
				- initialNumberOnServers;

		Cache l = new Cache();
		boolean foundLearning = false;
		try {
			foundLearning = l.learning(allVMs, initialNumberOffServers,
					allServers, "historyFFD.txt");

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out
				.println("\n\n\n\n[Execution] foundLearning " + foundLearning);

		if (foundLearning) {
			System.out
					.println("\n\n\n ----------------- Experiment already done! --------------------------");

			sUtil.displayPowerConsumptionAndCooling("FFD ");

			try {
				Thread.sleep(50000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.exit(1);
		}

		else {
			System.out
					.println("\n\n\n ----------------- Performing experiment! --------------------------");

			Map<VirtualMachine, Server> allocation = new HashMap<VirtualMachine, Server>();

			allocation = ffd.performFFD(allVMs);

			Utilization util = new Utilization();
			PowerConsumption power = new PowerConsumption();

			VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
			List<VirtualMachine> vmList = vmDAO.getAllVMsByState("Running");
			int ct = vmList.size();

			for (Entry<VirtualMachine, Server> entry : allocation.entrySet()) {
				ct++;
				int serverId = entry.getValue().getServerId();

				Server s = entry.getValue();
				VirtualMachine vm = entry.getKey();
				vm.setServer(s);
				s.setCorrespondingVMs(sUtil.addVmsToServer(s, vm));
				mergeSessionsForExecution(vm);

				util.setSingleServerUtilization(s);
				util.setSingleRackUtilization(s.getRack());
				power.setSingleServerPowerConsumptionGivenUtilization(s,
						s.getUtilization());
				power.setSingleRackPowerConsumption(s.getRack());
				cooling.setSingleServerCoolingValueGivenPowerConsumption(s,
						s.getPowerValue());
				cooling.setSingleRackCoolingValue(s.getRack());

				HACS hacs = new HACS();
				ParallelPlacementStrategy pp = new ParallelPlacementStrategy();

				float hacsAirMassFlowRate = hacs.computeMinMassFlowRate(Integer
						.parseInt(cracTemp));
				float hacsVolumetricAirFlow = hacs
						.computeVolumetricAirFlow(hacsAirMassFlowRate);

				float parallelAirMassFlowRate01 = pp.computeHeatRecirculation(
						0.1f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow01 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate01);
				float parallelAirMassFlowRate02 = pp.computeHeatRecirculation(
						0.2f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow02 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate02);
				float parallelAirMassFlowRate03 = pp.computeHeatRecirculation(
						0.3f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow03 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate03);
				float parallelAirMassFlowRate04 = pp.computeHeatRecirculation(
						0.4f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow04 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate04);
				float parallelAirMassFlowRate05 = pp.computeHeatRecirculation(
						0.5f, Integer.parseInt(cracTemp));
				float parallelVolumetricAirFlow05 = hacs
						.computeVolumetricAirFlow(parallelAirMassFlowRate05);
				chartAirflow.updatChartAirflow(hacsVolumetricAirFlow, 0,
						parallelVolumetricAirFlow01,
						parallelVolumetricAirFlow02,
						parallelVolumetricAirFlow03,
						parallelVolumetricAirFlow04,
						parallelVolumetricAirFlow05);

				chart.updateChartPowerConsumption(
						sUtil.getCurrentPowerConsumption(),
						sUtil.getCurrentCoolingPowerConsumption(), ct);
				System.out.println("\n\n\n\nCurrent power "
						+ sUtil.getCurrentPowerConsumption());
				System.out.println(hacsVolumetricAirFlow + " parallel "
						+ parallelVolumetricAirFlow01);
				Thread.yield();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}

			}
			MigrationEfficiency mEff = new MigrationEfficiency();

			System.out.println("Allocation Success Ratio: "
					+ mEff.computeAllocationMigrationRatio(allocation.size(),
							allVMs.size()));
			sUtil.displayPowerConsumptionAndCooling("[BEFORE DELETE] FFD ");

			History history = new History();
			history.writeToFile(allVMs, initialNumberOffServers, allServers,
					allocation, "historyFFD.txt");

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

}
