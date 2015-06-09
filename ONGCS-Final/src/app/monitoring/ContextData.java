package app.monitoring;


public class ContextData {

	private Object type;
	private String command; 
	private int numberOfInstances;
	private String alg;
	private String cracTemp;
	
	public ContextData(Object type, int numberOfInstances, String command, String alg, String cracTemp) {
		this.numberOfInstances = numberOfInstances;
		this.type = type;
		this.command = command;
		this.alg = alg;
		this.cracTemp = cracTemp;
	}

	public String getCracTemp() {
		return cracTemp;
	}

	public void setCracTemp(String cracTemp) {
		this.cracTemp = cracTemp;
	}

	public String getAlg() {
		return alg;
	}

	public void setAlg(String alg) {
		this.alg = alg;
	}

	public Object getType() {
		return type;
	}

	public void setType(Object type) {
		this.type = type;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getNumberOfInstances() {
		return numberOfInstances;
	}

	public void setNumberOfInstances(int numberOfInstances) {
		this.numberOfInstances = numberOfInstances;
	}

	@Override
	public String toString() {
		return "ContextData [type=" + type.getClass().getName() + ", command=" + command
				+ "]";
	}
}
