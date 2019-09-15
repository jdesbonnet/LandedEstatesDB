package ie.wombat.landedestates.api;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.json.JSONObject;

public class APICredentials {
	
	private static final Logger log = LoggerFactory.getLogger(APICredentials.class);

	private String email;
	private String password;
	private String appVersion;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getAppVersion() {
		return appVersion;
	}
	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}
	
	public static APICredentials decodeFromHttpRequest(HttpServletRequest request) {
		
		if (request.getHeader("X-LEDB-Auth")==null) {
			log.warn("header line X-LEDB-Auth not found, returning null");
			return null;
		}
		APICredentials credentials = new APICredentials();
		
		String b64auth = request.getHeader("X-LEDB-Auth");
		byte[] authbytes = Base64.getDecoder().decode(b64auth);
		
		String authStr;
		
		try {
			authStr = new String(authbytes,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Will always have UTF-8
			log.error(e.toString());
			return null;
		}
		
		// TODO: remove: should not password in clear text in log		
		JSONObject authJson = JSONObject.fromObject(authStr);
		credentials.setEmail(authJson.getString("email"));
		credentials.setPassword(authJson.getString("password"));
		
		if (authJson.has("appv")) {
			credentials.setAppVersion(authJson.getString("appv"));
		}
		
		return credentials;
	
	}
}
