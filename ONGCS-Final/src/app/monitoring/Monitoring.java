package app.monitoring;



public class Monitoring {

	private Queue queue;
	private String algorithmToStart;
	private String cracTemp;
	
	public Monitoring() {
	
		queue = new Queue();
	}

	public void addToQueue(Object type, int numberOfInstances, String command, String alg, String cracTemp) {
	
		ContextData contextData = new ContextData(type, numberOfInstances, command, alg, cracTemp);	
		queue.addTOQueue(contextData);
		
	}
	public void startMonitoring(){
		queue.start();
	}
	
}
