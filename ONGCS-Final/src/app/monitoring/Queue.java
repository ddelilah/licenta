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
					//join();
					//Thread.yield();
					Thread.sleep(1000);
				} catch (Exception e) {}
				
				ContextData message = receivedMessage.pollFirst();
				System.out.println("Monitoring " + message.toString());
				
					updateDB(message);
			
				Thread.yield();
				statesChanged = true;
			}
			if (statesChanged) {
				System.out.println("Starting system analysis...");
				statesChanged = false;
				break;		
			}
		}
		Analysis analysis = new Analysis();
		
		analysis.performAnalysis();
		
	}

	private void updateDB(ContextData message) {
		if(message.getCommand().equals("CREATE")){
			if(message.getType() instanceof VirtualMachine){

				System.out.println("Preparing to create VM.....");
				VirtualMachine vm = (VirtualMachine) message.getType();
				vm.setState(VMState.RUNNING.getValue());
				
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
		
	}


	public synchronized void addTOQueue(ContextData message) {
		this.receivedMessage.add(message);
	}
}