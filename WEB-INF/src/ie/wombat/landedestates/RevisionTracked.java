package ie.wombat.landedestates;

import java.util.Date;

public interface RevisionTracked {
	
	public Long getId();
	public Date getLastModified();
	public User getLastModifiedBy();
	public Integer getVersion();

}
