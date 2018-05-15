package ie.wombat.landedestates;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="object_history")
public class ObjectHistory implements Auditable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private int version;
	
	@Column(name="last_modified")
	private Date modified = Calendar.getInstance().getTime();
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name="object_id")
	private Long objectId;
	
	@Column(name="object_class")
	private String objectClass;
	
	@Lob
	@Column(name="object_xml")
	private String objectXML;
	
	@Lob
	@Column(name="object_json")
	private String objectJson;
	
	
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
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
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
	public String getObjectJson() {
		return objectJson;
	}
	public void setObjectJson(String objectJson) {
		this.objectJson = objectJson;
	}
	
}
