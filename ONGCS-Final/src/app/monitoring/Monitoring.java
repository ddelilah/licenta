package app.monitoring;



public class Monitoring {

	private Queue queue;
	private ContextData receivedData;
	
	public Monitoring() {
	
		queue = new Queue();
	}

	public void addToQueue(Object type, int numberOfInstances, String command, String alg, String cracTemp) {
	
		 receivedData = new ContextData(type, numberOfInstances, command, alg, cracTemp);	
		queue.addTOQueue(receivedData);
		
	}
	public void startMonitoring(){
		queue.start();
	}
	
}
