package ie.wombat.landedestates;

import java.util.Date;

public interface Auditable {

	public void setVersion (int version);
	public int getVersion ();
	
	public void setModified (Date d);
	public Date getModified ();
	
	public void setUid (int uid);
	public int getUid();
	
	public void setObjectId (Long objId);
	public Long getObjectId ();
	
	public void setObjectXML (String xml);
	public String getObjectXML ();
	
}
