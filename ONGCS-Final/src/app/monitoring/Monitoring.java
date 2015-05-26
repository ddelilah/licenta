package app.monitoring;



public class Monitoring {

	private Queue queue;

	public Monitoring() {
		queue = new Queue();
	}

	public void addToQueue(Object type, int numberOfInstances, String command, String alg) {
	
		ContextData contextData = new ContextData(type, numberOfInstances, command, alg);	
		queue.addTOQueue(contextData);
		
	}
	public void startMonitoring(){
		queue.start();
	}
	
}
