package ie.wombat.landedestates.api;

import net.sf.json.JSONObject;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import ie.wombat.landedestates.User;

/**
 * Interface which API handlers implement.
 * 
 * @author Joe Desbonnet
 *
 */
public interface APIMethodHandler {

	public void execute (
			APIMethodResponse opResponse,
			EntityManager em,
			User user,
			HttpServletRequest request,
			JSONObject requestJson) throws IOException, APIException;
}
