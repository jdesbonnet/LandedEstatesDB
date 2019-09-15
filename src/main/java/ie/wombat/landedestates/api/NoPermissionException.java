package ie.wombat.landedestates.api;

@SuppressWarnings("serial")
public class NoPermissionException extends APIException {

	public NoPermissionException () {
		super();
	}
	
	public NoPermissionException (String s) {
		super(s);
	}
}
