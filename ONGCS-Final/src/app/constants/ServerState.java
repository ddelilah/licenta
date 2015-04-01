package app.constants;

public enum ServerState {

	ON("On"), OFF("Off");

	private final String value;

	private ServerState(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}