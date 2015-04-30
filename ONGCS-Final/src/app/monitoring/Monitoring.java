package app.monitoring;



public class Monitoring {

	private Queue queue;

	public Monitoring() {
		queue = new Queue();
	}

	public void addToQueue(Object type, int numberOfInstances, String command) {
	
		ContextData contextData = new ContextData(type, numberOfInstances, command);	
		queue.addTOQueue(contextData);
		
	}
	public void startMonitoring(){
		queue.start();
	}
	
}
