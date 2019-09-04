package ie.wombat.landedestates;

import java.io.Serializable;
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
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
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
@Table(name="estate")
@XmlRootElement
public class Estate implements Serializable, Indexable, RevisionTracked {
	
	@Id
        @DocumentId
        @GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Field(index = Index.YES, store = Store.NO)
	private String name;
	
	//@Field(index = Index.NO, store = Store.NO)
        @Lob
	private String description;
	
	private Integer version;
	
	@Column(name="project_phase")
	private Integer projectPhase;
	
	@Column(name="last_modified")
	private Date lastModified;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name="last_modified_by")
	private User lastModifiedBy;
	
	
	
	@ManyToMany
	@JoinTable(name="estate_families",
		joinColumns=@JoinColumn(name="estate_id"),
		inverseJoinColumns = @JoinColumn(name="family_id")
	)
	@OrderBy("name")
	private Set<Family> families = new HashSet<Family>();
	
	
        /**
         * Houses (sometimes refered to as 'properties' in earlier code/doc)
         * associated with this estate record.
         */
	@OneToMany
	@JoinTable(name="estate_properties",
		joinColumns=@JoinColumn(name="estate_id"),
		inverseJoinColumns = @JoinColumn(name="property_id")
	)
	private Set<House> houses = new HashSet<House>();
	

	/**
         * From Employee Records project.
         */
	@OneToMany
	@JoinColumn(name="estate_id")
	private Set<EmployeeRecord> employeeRecords = new HashSet<EmployeeRecord>();
	
	/**
         * References in literature and other documentation.
         */
	@OneToMany
	@JoinTable(name="estate_references",
		joinColumns=@JoinColumn(name="estate_id"),
		inverseJoinColumns = @JoinColumn(name="reference_id")
	)
	@OrderBy("id")
	private Set<Reference> references = new HashSet<Reference>();
	
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
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Set<Family> getFamilies() {
		return families;
	}
	public void setFamilies(Set<Family> families) {
		this.families = families;
	}
	
	
	public Set<Reference> getReferences() {
		return references;
	}
	public void setReferences(Set<Reference> references) {
		this.references = references;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	
	
	
	@XmlTransient
	public Set<House> getHouses() {
		return houses;
	}
	public void setHouses(Set<House> houses) {
		this.houses = houses;
	}
	public Integer getProjectPhase() {
		return projectPhase;
	}
	public void setProjectPhase(Integer projectPhase) {
		this.projectPhase = projectPhase;
	}
	
	
	//@XmlTransient
	/*
	public House getPrimaryHouse() {
		return primaryHouse;
	}
	public void setPrimaryHouse(House primaryHouse) {
		this.primaryHouse = primaryHouse;
	}
	*/
	
	
	@XmlTransient
	public Set<EmployeeRecord> getEmployeeRecords() {
		return employeeRecords;
	}
	public void setEmployeeRecords(Set<EmployeeRecord> employeeRecords) {
		this.employeeRecords = employeeRecords;
	}
	
	
	// TODO: direct comparison of family objects should be possible
	public boolean hasFamily (Family family) {
		for (Family f : families) {
			if (family.getId().equals(f.getId())) {
				return true;
			}
		}
		return false;
	}
	
	public String getLuceneId () {
		return "E" + this.id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
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

	
}
