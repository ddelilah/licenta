package app.monitoring;

import java.util.concurrent.LinkedBlockingDeque;

import app.analysis.Analysis;
import app.model.Rack;
import app.model.Server;
import app.model.VirtualMachine;

public class Queue extends Thread {

	private LinkedBlockingDeque<ContextData> receivedMessage;
	private Analysis analysis;
	boolean statesChanged = false;

	public Queue() {
		this.receivedMessage = new LinkedBlockingDeque<ContextData>();
		this.analysis = new Analysis();
	}

	@Override
	public void run() {
		while (true) {
			while (!receivedMessage.isEmpty()) {
				try {
					Thread.sleep(1000);
					System.out.println("thread waiting");
				} catch (Exception e) {}
				
				ContextData message = receivedMessage.pollFirst();
				System.out.println("Processing " + message.toString());
				
				updateDB(message);
				
				statesChanged = true;
			}
			if (statesChanged) {
				System.out.println("starting system analysis");
				statesChanged = false;
			}
		}
	}

	private void updateDB(ContextData message) {

		if(message.getCommand().equals("CREATE")){
			if(message.getType() instanceof VirtualMachine){
				//create vm
				System.out.println("Preparing to create VM");
			}
			else if(message.getType() instanceof Server){
				System.out.println("Preparing to create Server");
			}
			else if(message.getType() instanceof Rack){}
		}
		else if(message.getCommand().equals("DELETE")){
			if(message.getType() instanceof VirtualMachine){
				//check if vm exists
				//delete vm
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