package app.monitoring;

import app.constants.VMState;
import app.model.CPU;
import app.model.HDD;
import app.model.RAM;
import app.model.Server;
import app.model.VirtualMachine;



public class Monitoring {

	private Queue queue;

	public Monitoring() {
		queue = new Queue();
		queue.start();
	}

	public void addToQueue(Object type, String command) {
	
		ContextData contextData = new ContextData(type, command);	
		queue.addTOQueue(contextData);
		
	}
	/*
	public static void main(String []args){
		Monitoring monitoring = new Monitoring();
		
		Server server = new Server();
		
		VirtualMachine vm = new VirtualMachine();
				
		monitoring.addToQueue( vm, "CREATE");
		monitoring.addToQueue(server, "CREATE");		
		monitoring.addToQueue(server, "DELETE");
		monitoring.addToQueue(vm, "CREATE");
		
	}*/

}
