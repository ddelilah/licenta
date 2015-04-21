package app.monitoring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingDeque;

import app.access.GenericDAO;
import app.access.VirtualMachineDAO;
import app.access.impl.GenericDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.analysis.Analysis;
import app.constants.VMState;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;
import app.scheduling.Consolidation;

public class Queue extends Thread {

	private LinkedBlockingDeque<ContextData> receivedMessage;
	private Analysis analysis;
	boolean statesChanged = false;
	private GenericDAO dao;
	private VirtualMachineDAO vmDAO;
	private List<ContextData> msg;
	List<VirtualMachine> newlyCreatedVmList = new ArrayList<VirtualMachine>();
	List<VirtualMachine> toBeDeployedVmList = new ArrayList<VirtualMachine>();
	List<VirtualMachine> toBeDeletedVmList = new ArrayList<VirtualMachine>();
	List<VirtualMachine> toBeDeployedVmListFinal = new ArrayList<VirtualMachine>();
	List<VirtualMachine> toBeDeletedVmListFinal = new ArrayList<VirtualMachine>();
	
	private Consolidation c;

	Map<String, List<VirtualMachine>> map = new HashMap<>();
	
	List<VirtualMachine> newlyCreatedVmListTestDelete = new ArrayList<VirtualMachine>();
	List<VirtualMachine> newlyCreatedVmListTestShutdown = new ArrayList<VirtualMachine>();
	List<VirtualMachine> newlyCreatedVmListTestDeploy = new ArrayList<VirtualMachine>();

	public Queue() {
		this.receivedMessage = new LinkedBlockingDeque<ContextData>();
		this.analysis = new Analysis();
		this.dao = new GenericDAOImpl();
		this.msg = new ArrayList<ContextData>();
		this.c = new Consolidation();

	}

	@Override
	public void run() {

		while (true) {
			while (!receivedMessage.isEmpty()) {
				try {

					// Thread.sleep(4000);
				} catch (Exception e) {
				}

				ContextData message = receivedMessage.pollFirst();
				System.out.println("\n\n\n.............Monitoring "
						+ message.toString() + ".........");

				map = updateDB(message);

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
		Analysis analysis = new Analysis();

		for (Entry<String, List<VirtualMachine>> entry : map.entrySet()) {
			if(entry.getKey().equals("toBeDeployedVmList")){
				toBeDeployedVmList = entry.getValue();
			}
			else{
				toBeDeletedVmList = entry.getValue();
			}
		}
		
//		System.out.println("To be deployed");
//		for(VirtualMachine vm: toBeDeployedVmList)
//			System.out.println(vm.toString());
//		System.out.println("To be deleted");
//		for(VirtualMachine vm: toBeDeletedVmList)
//			System.out.println(vm.toString());
		analysis.performAnalysis(toBeDeployedVmList);
		System.out.println("........\n\n End of deploymemnt..........");
		
		c.consolidationOnDelete(toBeDeletedVmList);
		
	}

	private Map<String,List<VirtualMachine>> updateDB(ContextData message) {

		VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();

		if (message.getCommand().equals("CREATE")) {
			if (message.getType() instanceof VirtualMachine) {

				System.out
						.println("\n\n\n...........Preparing to create VM............");
				VirtualMachine vm = (VirtualMachine) message.getType();
				vm.setState(VMState.PENDING.getValue());
				vm.setServer(null);
				System.out.println("vm's state is " + vm.getState());
				vmDAO.createInstance(vm);
				System.out.println(vm.getVmId());
				VirtualMachine vmToAdd = vmDAO.getVirtualMachineById(vm
						.getVmId());
				System.out.println(vmToAdd);
				newlyCreatedVmList.add(vmToAdd);
				newlyCreatedVmListTestDelete.add(vmToAdd);
				newlyCreatedVmListTestShutdown.add(vmToAdd);
				newlyCreatedVmListTestDeploy.add(vmToAdd);
			} else if (message.getType() instanceof Server) {
				System.out.println("Preparing to create Server");
			}
		} else if (message.getCommand().equals("DEPLOY")) {
			VirtualMachine vm = (VirtualMachine) message.getType();
			VirtualMachine vmToDeploy = (VirtualMachine) message.getType();

			System.out
					.println("\n\n\n...........Preparing to deploy VM............"
							+ vm.getVmId());
			boolean modifyNewlyCreated = false;

			vm.setState(VMState.DEPLOY.getValue());
			int pos = -1;
			for (VirtualMachine virtualM : newlyCreatedVmListTestDeploy) {
				pos++;
				;
				if (virtualM.getName().equals(vm.getName())) {// &&
																// !virtualM.getState().equals(VMState.SHUT_DOWN.getValue())){
					vmToDeploy.setVmId(virtualM.getVmId());
					vmToDeploy.setCpu(virtualM.getCpu());
					vmToDeploy.setHdd(virtualM.getHdd());
					vmToDeploy.setName(virtualM.getName());
					vmToDeploy.setRam(virtualM.getRam());
					vmToDeploy.setState(virtualM.getState());
					vmToDeploy.setVmMips(virtualM.getVmMips());
					toBeDeployedVmList.add(vmToDeploy);
					modifyNewlyCreated = true;
					System.out.println("Found in list");
					break;
				}
			}
			if (pos != -1)
				newlyCreatedVmListTestDeploy.remove(pos);

			if (modifyNewlyCreated)
				dao.updateInstance(vm);
			else {
				System.out.println("Not found in list");

				if (vmDAO.getVirtualMachineById(vm.getVmId()) != null) {
					System.out.println("Found in db");
					toBeDeployedVmList.add(vm);
					dao.updateInstance(vm);
				} else
					System.out.println("Not Found in db");

			}

		} else if (message.getCommand().equals("SHUTDOWN")) {
			VirtualMachine vm = (VirtualMachine) message.getType();
			boolean modifyNewlyCreated = false;
			vm.setState(VMState.SHUT_DOWN.getValue());
			vm.setServer(null);

			int pos = -1;
			for (VirtualMachine virtualM : newlyCreatedVmListTestShutdown) {
				pos++;
				if (virtualM.getName().equals(vm.getName())) {// &&
																// !virtualM.getState().equals(VMState.SHUT_DOWN.getValue())){
					vm.setVmId(virtualM.getVmId());
					modifyNewlyCreated = true;
					System.out.println("Found in list");
					break;
				}
			}

			if (pos != -1)
				newlyCreatedVmListTestShutdown.remove(pos);
			if (modifyNewlyCreated)
				dao.updateInstance(vm);
			else {
				System.out.println("Not found in list");

				if (vmDAO.getVirtualMachineById(vm.getVmId()) != null) {
					System.out.println("Found in db");

					dao.updateInstance(vm);
				} else
					System.out.println("Not Found in db");

			}

		}

		else if (message.getCommand().equals("DELETE")) {

			if (message.getType() instanceof VirtualMachine) {
				VirtualMachine vm = (VirtualMachine) message.getType();
				System.out
						.println("\n\n\n...........Preparing to delete VM............");
				VirtualMachine vmToDelete = new VirtualMachine();

				boolean startDelete = false;
				int pos = -1;
				for (VirtualMachine virtualM : newlyCreatedVmListTestDelete) {
					pos++;
					if (virtualM.getName().equals(vm.getName())) {// &&
																	// !virtualM.getState().equals(VMState.SHUT_DOWN.getValue())){
						vmToDelete.setVmId(virtualM.getVmId());
						vmToDelete.setCpu(virtualM.getCpu());
						vmToDelete.setHdd(virtualM.getHdd());
						vmToDelete.setName(virtualM.getName());
						vmToDelete.setRam(virtualM.getRam());
						vmToDelete.setState(virtualM.getState());
						vmToDelete.setVmMips(virtualM.getVmMips());
						System.out.println("virtualM is " + virtualM.getVmId());
						startDelete = true;
						toBeDeletedVmList.add(vmToDelete);
						break;
					}
				}

				int removeFromDeploy = -1;
				int removeFromShutdown = -1;
				int removeFromToBeDeployedVmList=-1;
				/* delete from the other lists too */
				for (VirtualMachine virtualM : newlyCreatedVmListTestDeploy) {
					removeFromDeploy++;
					if(virtualM.getVmId() == vmToDelete.getVmId()){
						break;
					}
				}

//				if (removeFromDeploy != -1)
//					newlyCreatedVmListTestDeploy.remove(removeFromDeploy);
				
				
				for(VirtualMachine virtualM: toBeDeployedVmList){
					removeFromToBeDeployedVmList++;
					if(virtualM.getVmId() == vmToDelete.getVmId()){
						break;
					}}
//				if(removeFromToBeDeployedVmList!=-1)
//					toBeDeployedVmList.remove(removeFromToBeDeployedVmList);
				
				
				for (VirtualMachine virtualM : newlyCreatedVmListTestShutdown) {
					removeFromShutdown++;
					if(virtualM.getVmId() == vmToDelete.getVmId()){
						break;
					}
				}

				if (removeFromShutdown != -1)
					newlyCreatedVmListTestShutdown.remove(removeFromShutdown);

				if (pos != -1)
					newlyCreatedVmListTestDelete.remove(pos);
				else {
					if (vmDAO.getVirtualMachineById(vm.getVmId()) != null) {
						vmToDelete.setVmId(vm.getVmId());
						toBeDeletedVmList.add(vmToDelete);
						startDelete = true;
					} else {
						startDelete = false;
					}
				}

				System.out.println("\n\n\n...........VM "
						+ vmToDelete.getVmId()
						+ " has been deleted............");
			} else if (message.getType() instanceof Server) {
				System.out.println("Preparing to delete VM");
			}
		}
		
		for(VirtualMachine v : toBeDeployedVmList) {
			VirtualMachine getVM = new VirtualMachine();
			getVM = vmDAO.getVirtualMachineById(v.getVmId());
			toBeDeployedVmListFinal.add(getVM);
		}
		
		for(VirtualMachine v : toBeDeletedVmList) {
			VirtualMachine getVMToDelete = new VirtualMachine();
			getVMToDelete = vmDAO.getVirtualMachineById(v.getVmId());
			toBeDeletedVmListFinal.add(getVMToDelete);
		}
		
		
		map.put("toBeDeployedVmList",toBeDeployedVmList);
		map.put("toBeDeletedVmList",toBeDeletedVmList);
		return map;
	}

	public synchronized void addTOQueue(ContextData message) {
		this.receivedMessage.add(message);
	}
}