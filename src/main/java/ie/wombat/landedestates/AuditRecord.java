package ie.wombat.landedestates;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="audit_record")
public class AuditRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(name="record_timestamp")
	private Date timestamp = new Date();
	
	@Column(name="entity_id")
	private Long entityId;
	
	@Column(name="class_name")
	private String className;
	
	@Column(name="entity_xml")
	private String entityXml;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	
	public Long getEntityId() {
		return entityId;
	}
	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}
	public String getEntityXml() {
		return entityXml;
	}
	public void setEntityXml(String entityXml) {
		this.entityXml = entityXml;
	}
	
	
}
