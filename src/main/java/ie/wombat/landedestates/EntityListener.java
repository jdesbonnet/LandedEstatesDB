package ie.wombat.landedestates;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

public class EntityListener {
	
	@PrePersist
	public void onPrePersist(Object o) {
		System.err.println ("** PRE-PERSIST " + o);
	}

	@PreUpdate
	public void onPreUpdate(Object o) {
		System.err.println ("** PRE-UPDATE " + o);
	}

	
}
