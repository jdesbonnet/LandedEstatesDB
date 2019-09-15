package ie.wombat.landedestates.api;

import java.util.ArrayList;
import java.util.List;

/**
 * A object which encapsulates the response to an API request.
 * 
 * @author Joe Desbonnet
 *
 */
public class APIMethodResponse {

	private int status;
	private String errorText;
	
	/**
	 * These field names will be excluded from JSON serialization.
	 */
	private String[] excludedFieldNames;
	
	// Experimental alternative binary response to API eg image or audio
	private boolean binaryResponse = false;
	private String mimeType = null;
	
	// If result object is String and this is true, then...
	private boolean jsonResponse = false;
	
	/** Optional Content-Disposition HTTP response header. Used when downloading files. */
	private String contentDisposition = null;
	
	private List<Alert> alerts = new ArrayList<>();
	
	private Object result;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
	
	/**
	 * Normally API returns JSON with HTTP mime type application/json. However
	 * an alternative binary response can be specified.
	 * @param mimeType
	 */
	public void setBinaryResponse(String mimeType) {
		binaryResponse = true;
		this.mimeType = mimeType;
	}
	public void setJsonStringResponse(boolean b) {
		this.jsonResponse = b;
	}
	
	/** Optional Content-Disposition HTTP response header. Used when downloading files. */
	public void setContentDisposition(String cd) {
		this.contentDisposition = cd;
	}
	
	public String getContentDisposition () {
		return this.contentDisposition;
	}
	
	public boolean isBinary() {
		return binaryResponse;
	}
	public String getMimeType () {
		return mimeType;
	}
	
	/**
	 * If the result object is JSON string (of class java.String)
	 * @return
	 */
	public boolean isJsonString () {
		return this.jsonResponse;
	}


	public String[] getExcludedFieldNames () {
		return this.excludedFieldNames;
	}
	public void excludeFieldNames(String[] excludedFields) {
		this.excludedFieldNames = excludedFields;
	}
	
	public void addAlert (Alert alert) {
		this.alerts.add(alert);
	}
	public List<Alert> getAlerts() {
		return this.alerts;
	}
	
}
