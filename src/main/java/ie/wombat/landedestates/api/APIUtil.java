package ie.wombat.landedestates.api;

import javax.persistence.EntityManager;
import ie.wombat.landedestates.LEDBEntity;

public class APIUtil {

	public static LEDBEntity fetch (EntityManager em, ie.wombat.landedestates.User user, Class<?> clazz, Long id) throws APIException {
		
		LEDBEntity entity = (LEDBEntity) em.find(clazz, id);
		if (entity == null) {
			return null;
		}
		
		/*
		if ( ! entity.getInstanceId().equals(user.getInstanceId())) {
			throw new APIException ("Entity " + entity.getClass().getSimpleName() + "#" + id + " does not belong to Instance#" + user.getInstanceId());
		}
		*/
		
		return entity;
	}
}
