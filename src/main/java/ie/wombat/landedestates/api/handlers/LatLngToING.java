package ie.wombat.landedestates.api.handlers;


import net.sf.json.JSONObject;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.wombat.gis.OSIGridReference;
import ie.wombat.gis.convert.OSILLAConvert;
import ie.wombat.landedestates.House;
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
public class LatLngToING implements APIMethodHandler {

	private static Logger log = LoggerFactory.getLogger(LatLngToING.class);
		
	@Override
	public void execute(APIMethodResponse apiResponse, EntityManager em, User user, HttpServletRequest request, JSONObject requestJson) throws APIException {
		
		Double latitude = new Double(request.getParameter("latitude"));
		Double longitude = new Double(request.getParameter("longitude"));
	
		OSILLAConvert cvt = new OSILLAConvert ();
		double[] ngr = cvt.lla2ng(latitude * Math.PI/180,longitude * Math.PI/180,0);
		OSIGridReference gr = new OSIGridReference ((int)ngr[0],(int)ngr[1]);
		apiResponse.setResult(gr.getGridReference(OSIGridReference.FORMAT_XEEENNN));
			
	}
	
}
