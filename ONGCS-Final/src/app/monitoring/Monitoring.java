package app.monitoring;



public class Monitoring {

	private Queue queue;

	public Monitoring() {
		queue = new Queue();
	}

	public void addToQueue(Object type, String command) {
	
		ContextData contextData = new ContextData(type, command);	
		queue.addTOQueue(contextData);
		
	}
	public void startMonitoring(){
		queue.start();
	}
	
}
