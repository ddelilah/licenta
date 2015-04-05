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
	List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();
	
	public Queue() {
		this.receivedMessage = new LinkedBlockingDeque<ContextData>();
		this.analysis = new Analysis();
		this.dao = new GenericDAOImpl();
		this.msg = new ArrayList<ContextData>();
		
	}
	
	@Override
	public void run() {
		//List<VirtualMachine> vmList = new ArrayList<VirtualMachine>();

		while (true) {
			while (!receivedMessage.isEmpty()) {
				try {
					//join();
					//Thread.yield();
					Thread.sleep(1000);
				} catch (Exception e) {}
				
				ContextData message = receivedMessage.pollFirst();
				System.out.println("\n\n\nMonitoring " + message.toString());
				
				vmList = updateDB(message);
			
				Thread.yield();
				statesChanged = true;
			}
			if (statesChanged) {
				System.out.println("\n\n\nStarting system analysis...");
				for(VirtualMachine vm: vmList){
					System.out.println(vm.getName()+"\n\n");
				}
				statesChanged = false;
				break;		
			}
		}
		Analysis analysis = new Analysis();
		
		analysis.performAnalysis(vmList);
		
	}

	private List<VirtualMachine> updateDB(ContextData message) {
		
		
		
		if(message.getCommand().equals("CREATE")){
			if(message.getType() instanceof VirtualMachine){

				System.out.println("\n\n\nPreparing to create VM.....");
				VirtualMachine vm = (VirtualMachine) message.getType();
				vm.setState(VMState.RUNNING.getValue());
				vmList.add(vm);
				System.out.println("vm's state is " + vm.getState());
				dao.createInstance(vm);
				
			}
			else if(message.getType() instanceof Server){
				System.out.println("Preparing to create Server");
			}
			else if(message.getType() instanceof Rack){}
		}
		else if(message.getCommand().equals("DELETE")){

			if(message.getType() instanceof VirtualMachine){
				VirtualMachine vm = (VirtualMachine) message.getType();
				dao.deleteInstance(vm);
			}
			else if(message.getType() instanceof Server){
				System.out.println("Preparing to delete VM");
			}
			else if(message.getType() instanceof Rack){}
		}
		return vmList;
	}


	public synchronized void addTOQueue(ContextData message) {
		this.receivedMessage.add(message);
	}
}