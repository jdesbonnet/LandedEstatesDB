package ie.wombat.landedestates.api.handlers;


import net.sf.json.JSONObject;



import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.wombat.landedestates.EntityUtil;
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
public class GetHouse implements APIMethodHandler {

	private static Logger log = LoggerFactory.getLogger(GetHouse.class);
		
	@Override
	public void execute(APIMethodResponse apiResponse, EntityManager em, User user, HttpServletRequest request, JSONObject requestJson) throws APIException {
		Long houseId = new Long(request.getParameter("id"));
		House house = (House)EntityUtil.fetch(em, user, House.class, houseId);
		apiResponse.setResult(house);
	}
	
}
