package ie.wombat.landedestates.api;

import java.io.InputStream;

/**
 * Experimental class. If returned from API handler then treat response as binary. Current method is to 
 * use {@link APIMethodResponse#setResult(Object)} with byte[] object and {@link APIMethodResponse#setBinaryResponse(String)}.
 * It think this may be cleaner.
 * 
 * @author Joe Desbonnet
 *
 */
public class StreamResponse {

	private String mimetype;
	private InputStream in;
	
	public StreamResponse (String mimetype, InputStream in) {
		this.mimetype = mimetype;
		this.in = in;
	}

	public String getMimetype() {
		return mimetype;
	}

	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}

	public InputStream getIn() {
		return in;
	}

	public void setIn(InputStream in) {
		this.in = in;
	}
	
	
}
