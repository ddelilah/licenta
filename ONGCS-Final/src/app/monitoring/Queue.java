package app.monitoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import app.GUI.ChartAirflow;
import app.GUI.Charts;
import app.GUI.RackUtilizationGUI;
import app.access.GenericDAO;
import app.access.impl.GenericDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.analysis.Analysis;
import app.constants.VMState;
import app.execution.Execution;
import app.execution.History;
import app.execution.Time;
import app.model.Server;
import app.model.VirtualMachine;
import app.scheduling.Consolidation;
import app.util.ConsolidationUtil;
import app.util.SchedulingUtil;

public class Queue extends Thread {
	private ChartAirflow chartAirflow = new ChartAirflow();

	private Charts chart = new Charts();
	
	private LinkedBlockingDeque<ContextData> dequeueReceivedMessage;
	private Analysis analysis;
	boolean statesChanged = false;
	private GenericDAO dao;
	private List<VirtualMachine> newlyCreatedVmList = new ArrayList<VirtualMachine>();
	private List<VirtualMachine> toBeDeployedVmList = new ArrayList<VirtualMachine>();
	private List<VirtualMachine> toBeDeletedVmList = new ArrayList<VirtualMachine>();
	
	private ConsolidationUtil cUtil;
	private Consolidation c;
	private SchedulingUtil sUtil;
	private Execution e;
	
	private Map<String, List<VirtualMachine>> map = new HashMap<>();

	private List<VirtualMachine> taskList = new ArrayList<VirtualMachine>();

	public Queue() {
		this.dequeueReceivedMessage = new LinkedBlockingDeque<ContextData>();
		this.analysis = new Analysis();
		this.dao = new GenericDAOImpl();
		this.sUtil = new SchedulingUtil();
	}

	@Override
	public void run() {

		Time t = new Time();
		t.setStartTime(System.nanoTime());
		String algorithm = "";
		String cracTemp = "";
		
		while (true) {
			while (!dequeueReceivedMessage.isEmpty()) {

				ContextData message = dequeueReceivedMessage.pollFirst();


				System.out.println("\n\n\n.............Monitoring "
						+ message.toString() + ".........");
				map = updateDB(message);
				algorithm = message.getAlg();
				cracTemp = message.getCracTemp();
				Thread.yield();
				statesChanged = true;
				
			}
			if (statesChanged) {

				System.out
						.println("\n\n\n............Starting system analysis..............");
				statesChanged = false;
				break;
			}
		}

		c = new Consolidation(cracTemp);
		cUtil = new ConsolidationUtil(cracTemp);
		e = new Execution(cracTemp);
		
		for (Entry<String, List<VirtualMachine>> entry : map.entrySet()) {
			if (entry.getKey().equals("toBeDeployedVmList")) {
				toBeDeployedVmList = entry.getValue();
			} else {
				toBeDeletedVmList = entry.getValue();
			}
		}
		

//		System.out.println("To be deployed");
//		for (VirtualMachine vm : toBeDeployedVmList)
//			System.out.println(vm.toString());
//		System.out.println("To be deleted");
//		for (VirtualMachine vm : toBeDeletedVmList)
//			System.out.println(vm.toString());

		ServerDAOImpl serverDAO = new ServerDAOImpl();
		List<Server> allServers = serverDAO.getAllServers();

		int initialNumberOnServers = serverDAO.getAllServersByState("on").size();
		int initialNumberOffServers = allServers.size() - initialNumberOnServers;
			
		analysis.performAnalysis(toBeDeployedVmList, algorithm, chart, chartAirflow, cracTemp);


		if (!toBeDeletedVmList.isEmpty()) {
			if (algorithm.equalsIgnoreCase("FFD")) {
				cUtil.deleteForFFD(toBeDeletedVmList, chart, chartAirflow);
			} else {
				c.consolidationOnDelete(toBeDeletedVmList, chart, chartAirflow,algorithm);
			}
		} else {
			System.out.println("No workload to be deleted.");
		}
		
		sUtil.displayPowerConsumptionAndCooling("[AFTER DELETE] " + algorithm);
		
		String filename="history"+algorithm+".txt";
		History history = new History();
		history.writeToFileAfterConsolidation( initialNumberOffServers, allServers, filename);

		System.out.println("\n\n........ End of deployment..........");

		t.setEndTime(System.nanoTime());
		long elapsedTime = t.getExecutionTime();
		System.out.println("[Execution Time] "
				+ TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS)
				+ " sec");
	
		System.out.println("[Execution Time] "
				+ TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS)
				+ " sec");

		chart.finishChartExecution();
		chartAirflow.finishChartExecution();
	}

	private boolean checkIfInstanceAlreadyAdded(VirtualMachine vmToCheck,
			List<VirtualMachine> toBeDeployedVmList) {

		for (VirtualMachine vm : toBeDeployedVmList) {
			if (vm.getVmId() == vmToCheck.getVmId())
				return true;
		}
		return false;

	}

	private Map<String, List<VirtualMachine>> updateDB(ContextData message) {


		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
		// ---------------------------------- CREATE COMMAND
		// --------------------------------------------------------------------------------
		if (message.getCommand().equals("CREATE")) {
			if (message.getType() instanceof VirtualMachine) {

				System.out.println("\n\n\n...........Preparing to create "
						+ message.getNumberOfInstances() + " VMs...........");

				for (int i = 0; i < message.getNumberOfInstances(); i++) {

					VirtualMachine vm = (VirtualMachine) message.getType();

					System.out
							.println("\n\n\n...........Preparing to create VM "
									+ vm.getName() + "...........");
//					 try {
//					 Thread.sleep(1000);
//					 } catch (InterruptedException e) {}

					vm.setState(VMState.PENDING.getValue());
					vm.setServer(null);

					vmDAO.createInstance(vm);
					VirtualMachine vmToAdd = vmDAO.getVirtualMachineById(vm
							.getVmId());
					System.out.println("\n[CREATED VM] " + vmToAdd);
					vmToAdd.setState(VMState.PENDING.getValue());
					newlyCreatedVmList.add(vmToAdd);
					taskList.add(vmToAdd);
				}
			}
		}
		// ---------------------------------- DEPLOY COMMAND
		// --------------------------------------------------------------------------------

		else if (message.getCommand().equals("DEPLOY")) {

			System.out.println("\n\n\n...........Preparing to deploy "
					+ message.getNumberOfInstances() + " VMs...........");

			for (int i = 0; i < message.getNumberOfInstances(); i++) {

				VirtualMachine vm = (VirtualMachine) message.getType();
				VirtualMachine vmToDeploy = (VirtualMachine) message.getType();

				System.out
						.println("\n\n\n...........Preparing to deploy VM............"
								+ vm.getVmId());
				
//				 try {
//				 Thread.sleep(1000);
//				 } catch (InterruptedException e) {}

				boolean modifyNewlyCreated = false;

				int pos = -1;
				/*
				 * first check if there exists a vm that is in Pending state
				 */
				for (VirtualMachine virtualM : taskList) {
					pos++;
					if (virtualM.getName().equals(vm.getName())
							&& virtualM.getState().equalsIgnoreCase(
									VMState.PENDING.getValue())) {
						vmToDeploy = virtualM;
						vmToDeploy.setState(VMState.DEPLOY.getValue());
						if (!checkIfInstanceAlreadyAdded(vmToDeploy,
								toBeDeployedVmList))
							toBeDeployedVmList.add(vmToDeploy);
						modifyNewlyCreated = true;
//						System.out.println("Found in list 1");
						break;
					}
				}
				/*
				 * Check if we wanted to change a vm's state from SHUT_DOWN to
				 * DEPLOY
				 */
				pos = -1;
				if (!modifyNewlyCreated)
					for (VirtualMachine virtualM : taskList) {
						pos++;
						if (virtualM.getName().equals(vm.getName())
								&& !virtualM.getState().equalsIgnoreCase(
										VMState.DEPLOY.getValue())) {
							vmToDeploy = virtualM;
							vmToDeploy.setState(VMState.DEPLOY.getValue());
							if (!checkIfInstanceAlreadyAdded(vmToDeploy,
									toBeDeployedVmList))
								toBeDeployedVmList.add(vmToDeploy);
							modifyNewlyCreated = true;
//							System.out.println("Found in list 2");
							break;
						}
					}

				if (pos != -1) {
					taskList.set(pos, vmToDeploy);
				}

//				System.out.println("taskList " + taskList);

				if (modifyNewlyCreated)
					dao.updateInstance(vmToDeploy);
				else {
//					System.out.println("Not found in list");

					if (vmDAO.getVirtualMachineById(vm.getVmId()) != null) {
//						System.out.println("Found in db");
						if (!checkIfInstanceAlreadyAdded(vmToDeploy,
								toBeDeployedVmList))
							toBeDeployedVmList.add(vmToDeploy);
						vmToDeploy.setState(VMState.DEPLOY.getValue());
						dao.updateInstance(vmToDeploy);
					} 
//					else
//						System.out.println("Not Found in db");
				}

			}
//			System.out.println("toBeDeployedVmList " + toBeDeployedVmList);
		}
		// ---------------------------------- SHUTDOWN COMMAND
		// --------------------------------------------------------------------------------
		else if (message.getCommand().equals("SHUTDOWN")) {
			System.out.println("\n\n\n...........Preparing to shutdown "
					+ message.getNumberOfInstances() + " VMs...........");

			for (int i = 0; i < message.getNumberOfInstances(); i++) {
				// try {
				// Thread.sleep(4000);
				// } catch (InterruptedException e) {}

				VirtualMachine vm = (VirtualMachine) message.getType();
				System.out
						.println("\n\n\n...........Preparing to shutdown VM............"
								+ vm.getVmId());

				boolean modifyNewlyCreated = false;
				vm.setState(VMState.SHUT_DOWN.getValue());
				vm.setServer(null);

				/* Check if there exists a vm that is in Pending state */
				int pos = -1;
				for (VirtualMachine virtualM : taskList) {
					pos++;
					if (virtualM.getName().equals(vm.getName())
							&& virtualM.getState().equalsIgnoreCase(
									VMState.PENDING.getValue())) {
						vm = virtualM;
						vm.setState(VMState.SHUT_DOWN.getValue());
						modifyNewlyCreated = true;
//						System.out.println("Found in list");
						break;
					}
				}
				/*
				 * Check if we wanted to change a vm's state from DEPLOY to
				 * SHUT_DOWN
				 */
				pos = -1;
				if (!modifyNewlyCreated)
					for (VirtualMachine virtualM : taskList) {
						pos++;
						if (virtualM.getName().equals(vm.getName())
								&& !virtualM.getState().equalsIgnoreCase(
										VMState.SHUT_DOWN.getValue())) {
							vm = virtualM;
							vm.setState(VMState.SHUT_DOWN.getValue());
							modifyNewlyCreated = true;
//							System.out.println("Found in list");
							break;
						}
					}

				/* if a deployed vm is shut down => remove from toBeDeployedList */
				int removeFromToBeDeployedVmList = -1;
				boolean deleteFromDeployedList = false;
				for (VirtualMachine virtualM : toBeDeployedVmList) {
					removeFromToBeDeployedVmList++;
					if (virtualM.getVmId() == vm.getVmId()) {
						deleteFromDeployedList = true;
						break;
					}
				}
				if (removeFromToBeDeployedVmList != -1
						&& deleteFromDeployedList)
					toBeDeployedVmList.remove(removeFromToBeDeployedVmList);

				if (pos != -1) {
					taskList.set(pos, vm);
				}
//				System.out.println("taskList " + taskList);
				if (modifyNewlyCreated)
					dao.updateInstance(vm);
				else {
//					System.out.println("Not found in list");

					if (vmDAO.getVirtualMachineById(vm.getVmId()) != null) {
//						System.out.println("Found in db");

						dao.updateInstance(vm);
					} 
//					else
//						System.out.println("Not Found in db");
				}
			}
//			System.out.println("toBeDeployedVmList " + toBeDeployedVmList);

		}
		// ---------------------------------- DELETE COMMAND
		// --------------------------------------------------------------------------------
		else if (message.getCommand().equals("DELETE")) {

			if (message.getType() instanceof VirtualMachine) {
				System.out.println("\n\n\n...........Preparing to delete "
						+ message.getNumberOfInstances() + " VMs...........");

				for (int i = 0; i < message.getNumberOfInstances(); i++) {
//					 try {
//					 Thread.sleep(1000);
//					 } catch (InterruptedException e) {}

					VirtualMachine vm = (VirtualMachine) message.getType();
					System.out
							.println("\n\n\n...........Preparing to delete VM............"
									+ vm.getName());
					VirtualMachine vmToDelete = new VirtualMachine();

					boolean startDelete = false;
					int pos = -1;
					for (VirtualMachine virtualM : taskList) {
						pos++;
						if (virtualM.getName().equals(vm.getName())) {
							vmToDelete = virtualM;
							// System.out.println("virtualM is "+ virtualM
							// +"\n"+vmToDelete.getServer() +"\n"+vm);
							startDelete = true;
							toBeDeletedVmList.add(virtualM);
							break;
						}
					}
//					System.out
//							.println("toBeDeletedVmList " + toBeDeletedVmList);
//					rGUI.getTextArea().append("\n...........Will be deleted "
//							+toBeDeletedVmList + " ...........");
//					
//					rGUI.getTextArea().setCaretPosition(rGUI.getTextArea().getDocument().getLength());

					if (pos != -1) {
						taskList.remove(pos);
					}
					int removeFromToBeDeployedVmList = -1;
					boolean deleteFromDeployedList = false;
					for (VirtualMachine virtualM : toBeDeployedVmList) {
						removeFromToBeDeployedVmList++;
						if (virtualM.getVmId() == vmToDelete.getVmId()) {
//							System.out
//									.println((virtualM.getVmId() == vmToDelete
//											.getVmId())
//											+ " "
//											+ virtualM.getVmId()
//											+ " "
//											+ vmToDelete.getVmId());
							deleteFromDeployedList = true;
							break;
						}
					}

					// if(removeFromToBeDeployedVmList!=-1 &&
					// deleteFromDeployedList)
					// toBeDeployedVmList.remove(removeFromToBeDeployedVmList);
					//

					// if (startDelete){
					// vmToDelete.setState(VMState.DONE.getValue());
					// dao.updateInstance(vmToDelete);
					// }
					// else {
					// System.out.println("Not found in list");
					// if(!startDelete){
					// if (vmDAO.getVirtualMachineById(vm.getVmId()) != null) {
					// vmToDelete = vmDAO.getVirtualMachineById(vm.getVmId());
					// toBeDeletedVmList.add(vmToDelete);
					// startDelete = true;
					// System.out.println("Found in db"+ vmToDelete);
					// vmToDelete.setState(VMState.DONE.getValue());
					// // dao.updateInstance(vmToDelete);
					// } else
					// System.out.println("Not Found in db");
					//

					System.out.println("\n\n\n...........VM "
							+ vmToDelete.getVmId()
							+ " has been deleted............");

				}
			}
		}
		map.put("toBeDeployedVmList", toBeDeployedVmList);
		map.put("toBeDeletedVmList", toBeDeletedVmList);
		return map;
	}

	public synchronized void addTOQueue(ContextData message) {
		this.dequeueReceivedMessage.add(message);
	}
}