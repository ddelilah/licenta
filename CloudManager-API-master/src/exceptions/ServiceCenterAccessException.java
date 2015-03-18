package exceptions;

public class ServiceCenterAccessException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5293444520499718612L;

	public ServiceCenterAccessException(String message) {
        super(message);
    }

    public ServiceCenterAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
