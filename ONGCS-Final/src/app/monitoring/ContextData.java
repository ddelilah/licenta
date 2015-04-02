package app.monitoring;


public class ContextData {

	private Object type;
	private String command; 
	
	public ContextData(Object type, String command) {
		
		this.type = type;
		this.command = command;
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

	@Override
	public String toString() {
		return "ContextData [type=" + type.getClass().getName() + ", command=" + command
				+ "]";
	}
}