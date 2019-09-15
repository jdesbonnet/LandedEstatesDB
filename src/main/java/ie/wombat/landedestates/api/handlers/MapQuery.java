package ie.wombat.landedestates.api.handlers;


import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
public class MapQuery implements APIMethodHandler {

	private static Logger log = LoggerFactory.getLogger(MapQuery.class);
		
	@Override
	public void execute(APIMethodResponse apiResponse, EntityManager em, User user, HttpServletRequest request, JSONObject requestJson) throws APIException {
		List<House> houses = em.createQuery("from House order by id").getResultList();
		
		List<HouseLite> ret = new ArrayList<MapQuery.HouseLite>(houses.size());
		for (House house : houses) {
			HouseLite hl = new HouseLite();
			hl.id = house.getId();
			hl.name = house.getName();
			hl.lat = house.getLatitude();
			hl.lng = house.getLongitude();
			ret.add(hl);
		}
		
		apiResponse.setResult(ret);
	}
	
	private static class HouseLite {
		public Long id;
		public String name;
		public Double lat;
		public Double lng;
	}
}
