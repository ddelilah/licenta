package exceptions;

public class ApplicationException extends ServiceCenterAccessException{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8654211850353958749L;

	public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}
