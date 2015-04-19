package app.monitoring;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import org.hibernate.Session;
import org.hibernate.Transaction;

import app.access.GenericDAO;
import app.access.ServerDAO;
import app.access.VirtualMachineDAO;
import app.access.impl.GenericDAOImpl;
import app.access.impl.ServerDAOImpl;
import app.access.impl.VirtualMachineDAOImpl;
import app.analysis.Analysis;
import app.constants.PolicyType;
import app.constants.VMState;
import app.hibernate.SessionFactoryUtil;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class Queue extends Thread {

	private LinkedBlockingDeque<ContextData> receivedMessage;
	private Analysis analysis;
	boolean statesChanged = false;
	private GenericDAO dao;
	private List<ContextData> msg;
	List<VirtualMachine> newlyCreatedVmList = new ArrayList<VirtualMachine>();
	List<VirtualMachine> toBeDeployedVmList = new ArrayList<VirtualMachine>();

	public Queue() {
		this.receivedMessage = new LinkedBlockingDeque<ContextData>();
		this.analysis = new Analysis();
		this.dao = new GenericDAOImpl();
		this.msg = new ArrayList<ContextData>();
		
	}
	
	@Override
	public void run() {

		while (true) {
			while (!receivedMessage.isEmpty()) {
				try {
			
			//		Thread.sleep(4000);
				} catch (Exception e) {}
				
				ContextData message = receivedMessage.pollFirst();
				System.out.println("\n\n\n.............Monitoring " + message.toString()+".........");
				
				toBeDeployedVmList = updateDB(message);
			
				Thread.yield();
				statesChanged = true;
			}
			if (statesChanged) {
				System.out.println("\n\n\n............Starting system analysis..............");
				
				statesChanged = false;
				break;		
			}
		}
		Analysis analysis = new Analysis();
		
		analysis.performAnalysis(toBeDeployedVmList);
		
	}

	private List<VirtualMachine> updateDB(ContextData message) {
		
		 VirtualMachineDAOImpl vmDAO = new VirtualMachineDAOImpl();
		
		if(message.getCommand().equals("CREATE")) {
			if(message.getType() instanceof VirtualMachine) {

				System.out.println("\n\n\n...........Preparing to create VM............");
				VirtualMachine vm = (VirtualMachine) message.getType();
				vm.setState(VMState.PENDING.getValue());
				vm.setServer(null);
				System.out.println("vm's state is " + vm.getState());
				vmDAO.createInstance(vm);
				System.out.println(vm.getVmId());
				VirtualMachine vmToAdd = vmDAO.getVirtualMachineById(vm.getVmId());
				System.out.println(vmToAdd);
				newlyCreatedVmList.add(vmToAdd);
				
			}
			else if(message.getType() instanceof Server){
				System.out.println("Preparing to create Server");
			}
		}
		else if(message.getCommand().equals("DEPLOY")) {
			VirtualMachine vm = (VirtualMachine) message.getType();
			System.out.println("\n\n\n...........Preparing to deploy VM............"+vm.getVmId());
			boolean modifyNewlyCreated = false;
			
			vm.setState(VMState.DEPLOY.getValue());
			
			for(VirtualMachine virtualM: newlyCreatedVmList){
			if(virtualM.getName().equals(vm.getName())){// && !virtualM.getState().equals(VMState.SHUT_DOWN.getValue())){
				vm.setVmId(virtualM.getVmId());
				toBeDeployedVmList.add(vm);
				modifyNewlyCreated = true;
				System.out.println("Found in list");
				break;
				}
			}
			
			if(modifyNewlyCreated)
				dao.updateInstance(vm);
			else{
				System.out.println("Not found in list");

				if(vmDAO.getVirtualMachineById(vm.getVmId())!=null){
					System.out.println("Found in db");
					toBeDeployedVmList.add(vm);
					dao.updateInstance(vm);
				}
				else 		
					System.out.println("Not Found in db");

			}
			
			

		}
		else if(message.getCommand().equals("SHUTDOWN")) {
			VirtualMachine vm = (VirtualMachine) message.getType();
			boolean modifyNewlyCreated = false;
			vm.setState(VMState.SHUT_DOWN.getValue());
			vm.setServer(null);
			
			for(VirtualMachine virtualM: newlyCreatedVmList){
			if(virtualM.getName().equals(vm.getName())){// && !virtualM.getState().equals(VMState.SHUT_DOWN.getValue())){
				vm.setVmId(virtualM.getVmId());
				modifyNewlyCreated = true;
				System.out.println("Found in list");
				break;
				}
			}
			
			if(modifyNewlyCreated)
				dao.updateInstance(vm);
			else{
				System.out.println("Not found in list");

				if(vmDAO.getVirtualMachineById(vm.getVmId())!=null){
					System.out.println("Found in db");

					dao.updateInstance(vm);
				}
				else 				System.out.println("Not Found in db");

			}
			
			
		}
		
		else if(message.getCommand().equals("DELETE")) {

			if(message.getType() instanceof VirtualMachine) {
				VirtualMachine vm = (VirtualMachine) message.getType();
				System.out.println("\n\n\n...........Preparing to delete VM............");
				VirtualMachine vmToDelete = new VirtualMachine();

				boolean startDelete = false;
				int pos=-1;
				for(VirtualMachine virtualM: newlyCreatedVmList){
					pos++;
				if(virtualM.getName().equals(vm.getName())){// && !virtualM.getState().equals(VMState.SHUT_DOWN.getValue())){
						vmToDelete.setVmId(virtualM.getVmId());
						System.out.println("virtualM is "+ virtualM.getVmId());
						startDelete = true;
						break;
					}
				}
				
				if(pos!=-1)
					newlyCreatedVmList.remove(pos);
				else{
					if(vmDAO.getVirtualMachineById(vm.getVmId()) != null){
						vmToDelete.setVmId(vm.getVmId());
						startDelete = true;
					}
					else {
						startDelete = false;
					}
				}
				if(startDelete)
					dao.deleteInstance(vmToDelete);
				
				System.out.println("\n\n\n...........VM "+ vmToDelete.getVmId()+" has been deleted............");
			}
			else if(message.getType() instanceof Server){
				System.out.println("Preparing to delete VM");
			}
		}
		return toBeDeployedVmList;
	}


	public synchronized void addTOQueue(ContextData message) {
		this.receivedMessage.add(message);
	}
}