package ie.wombat.landedestates.api.handlers;


import net.sf.json.JSONObject;



import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.wombat.landedestates.MapTileLayer;
import ie.wombat.landedestates.User;
import ie.wombat.landedestates.api.APIException;
import ie.wombat.landedestates.api.APIMethodHandler;
import ie.wombat.landedestates.api.APIMethodResponse;
import ie.wombat.landedestates.api.GetAllowed;

/**
 * Return {@link MapTileLayer}s.
 * 
 * @author Joe Desbonnet
 *
 */
@GetAllowed
public class GetMapTileLayers implements APIMethodHandler {

	private static Logger log = LoggerFactory.getLogger(GetMapTileLayers.class);
		
	@Override
	public void execute(APIMethodResponse apiResponse, EntityManager em, User user, HttpServletRequest request, JSONObject requestJson) throws APIException {
		apiResponse.setResult(em.createQuery("from MapTileLayer order by name").getResultList());
	}
	
}
