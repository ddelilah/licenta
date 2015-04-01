package app.constants;

public enum RackState {
	ON("On"), OFF("Off");

	private final String value;

	private RackState (String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
