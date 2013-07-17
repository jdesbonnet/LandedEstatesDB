package ie.wombat.landedestates;

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

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
@Table(name="estate")
public class Estate implements Indexable {
	
	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	
	private String name;
	
	private String description;
	
	private int version; // experimental
	
	@Column(name="project_phase")
	private Integer projectPhase;
	

	@ManyToMany
	@JoinTable(name="estate_families",
		joinColumns=@JoinColumn(name="estate_id"),
		inverseJoinColumns = @JoinColumn(name="family_id")
	)
	private Set<Family> families = new HashSet<Family>();
	
	@OneToMany
	@JoinTable(name="estate_properties",
		joinColumns=@JoinColumn(name="estate_id"),
		inverseJoinColumns = @JoinColumn(name="property_id")
	)
	private Set<House> houses = new HashSet<House>();
	
	
	@ManyToOne
	@JoinColumn(name="primary_house_id")
	private House primaryHouse;
	
	
	@OneToMany
	@JoinTable(name="estate_references",
		joinColumns=@JoinColumn(name="estate_id"),
		inverseJoinColumns = @JoinColumn(name="reference_id")
	)
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
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	
	
	
	
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
}
