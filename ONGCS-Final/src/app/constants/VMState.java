package app.constants;

public enum VMState {

	SHUT_DOWN("Shut down"), PENDING("Pending"), RUNNING("Running");

	private final String value;

	private VMState(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

}
