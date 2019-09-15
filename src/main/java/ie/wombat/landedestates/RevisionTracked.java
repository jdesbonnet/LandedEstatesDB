package ie.wombat.landedestates;

import java.util.Date;

public interface RevisionTracked extends LEDBEntity {
	
	//public Long getId();
	public Date getLastModified();
	public void setLastModified(Date d);
	public User getLastModifiedBy();
	public void setLastModifiedBy(User user);
	public Integer getVersion();

}
