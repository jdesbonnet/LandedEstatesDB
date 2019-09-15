package ie.wombat.landedestates;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.wombat.landedestates.api.NoPermissionException;

public class EntityUtil {
	
	private static Logger log = LoggerFactory.getLogger(EntityUtil.class);

	/**
	 * Convenience method that returns an entity implementing {@link InstanceEntity} interface, but first
	 * checks to make sure User is allowed access. If not a {@link NoPermissionException} is thrown.
	 * 
	 * @param em
	 * @param user
	 * @param entityClass
	 * @param entityId
	 * @return
	 * @throws NoPermissionException
	 */
	public static <T> LEDBEntity fetch (EntityManager em, User user, Class <T> entityClass, Long entityId) throws NoPermissionException {
		
		log.info("fetch(User#" + user.getId() + "," + entityClass.getSimpleName()+"#"+entityId);
		
		Object o = em.find(entityClass, entityId);
		if (o == null) {
			return null;
		}
		
		if ( ! (o instanceof LEDBEntity)) {
			throw new NoPermissionException("Requested entity " + entityClass.getSimpleName() + "#" + entityId
					+ " does not implement " + LEDBEntity.class.getSimpleName() + " interface");
		}
		
		LEDBEntity entity = (LEDBEntity)o;
		
		// ACL checks
		
		return entity;
		
	}
	
	
	
}
