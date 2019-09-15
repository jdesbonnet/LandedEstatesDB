package ie.wombat.landedestates.api;

/**
 * API handlers throw this exception to indicate error condition. The {@link APIServlet}
 * will catch this exception and report it in the standard JSON response.
 * 
 * @author Joe Desbonnet
 *
 */
@SuppressWarnings("serial")
public class APIException extends Exception {

	public APIException() {
		super();
	}
	
	public APIException (String error) {
		super(error);
	}
	
}
