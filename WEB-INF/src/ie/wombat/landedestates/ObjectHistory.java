package ie.wombat.landedestates;

import java.util.Calendar;
import java.util.Date;

public class ObjectHistory implements Auditable {

	private Long id;
	private int version;
	private Date modified = Calendar.getInstance().getTime();
	private int uid;
	private Long objectId;
	private String objectClass;
	private String objectXML;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public void setUid (int uid) {
		this.uid = uid;
	}
	public int getUid () {
		return this.uid;
	}
	public void setObjectId (Long objId) {
		this.objectId = objId;
	}
	public Long getObjectId () {
		return this.objectId;
	}
	public String getObjectClass() {
		return objectClass;
	}
	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}
	public String getObjectXML() {
		return objectXML;
	}
	public void setObjectXML(String xml) {
		this.objectXML = xml;
	}
	
}
