package util;

/**
 * Provides response types and message for methods
 */
public enum ResponseType {
	SUCCESS ("success"),
	INFO ("info"),
	ERROR ("error");
	
	String message;
	private ResponseType(String message) {
		this.message = message;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}


