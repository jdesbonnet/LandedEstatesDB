package ie.wombat.landedestates;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
@Table(name="family")
@XmlRootElement
public class Family implements Indexable,RevisionTracked {
	
	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Field(index = Index.YES, store = Store.NO)
	private String name;
	
	private String title;
	
	//@Field(index = Index.NO, store = Store.NO)
	private String description;
	
	@ManyToMany(mappedBy="families")
	@Transient
	//@JoinTable(name="estate_families",
	//joinColumns=@JoinColumn(name="family_id")
	//inverseJoinColumns = @JoinColumn(name="estate_id")
	//)
	private Set<Estate> estates = new HashSet<Estate>();
	
	@Column(name="project_phase")
	private Integer projectPhase;
	
	@Column(name="last_modified")
	private Date lastModified;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="last_modified_by")
	private User lastModifiedBy;
	
	private Integer version;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlTransient
	public Set<Estate> getEstates() {
		return estates;
	}
	public void setEstates(Set<Estate> estates) {
		this.estates = estates;
	}
	
	public Integer getProjectPhase() {
		return projectPhase;
	}
	public void setProjectPhase(Integer projectPhase) {
		this.projectPhase = projectPhase;
	}
	public String getLuceneId () {
		return "F"+this.id;
	}
	
	
	
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	public User getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(User lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	@Transient
	public String getNameAndTitle() {
		if (getTitle() == null || getTitle().trim().length()==0) {
			return getName();
		}
		return getName() + " (" + getTitle() + ")";
	}
}
