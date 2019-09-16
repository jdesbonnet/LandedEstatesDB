package ie.wombat.landedestates.api.handlers;


import net.sf.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.wombat.landedestates.House;
import ie.wombat.landedestates.OSMGeocode;
import ie.wombat.landedestates.User;
import ie.wombat.landedestates.api.APIException;
import ie.wombat.landedestates.api.APIMethodHandler;
import ie.wombat.landedestates.api.APIMethodResponse;
import ie.wombat.landedestates.api.GetAllowed;

/**
 * Return {@link House} record.
 * 
 * @author Joe Desbonnet
 *
 */
@GetAllowed
public class OSMQuery implements APIMethodHandler {

	private static Logger log = LoggerFactory.getLogger(OSMQuery.class);
		
	@Override
	public void execute(APIMethodResponse apiResponse, EntityManager em, User user, HttpServletRequest request, JSONObject requestJson) throws APIException {
		
		Double latitude = new Double(request.getParameter("latitude"));
		Double longitude = new Double(request.getParameter("longitude"));

		String json;
		try {
			json = OSMGeocode.getReverseGeocodeJson(latitude, longitude);
			apiResponse.setJsonStringResponse(true);
			apiResponse.setResult(json);
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
}
