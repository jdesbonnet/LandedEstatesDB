package ie.wombat.landedestates;

import java.util.HashSet;
import java.util.Set;

public class Estate implements Indexable {
	private Long id;
	private String name;
	private String description;
	private int version; // experimental
	private Integer projectPhase;
	
	
	private Set<Family> families = new HashSet<Family>();
	private Set<Property> houses = new HashSet<Property>();
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
	
	public Set<Property> getProperties() {
		return houses;
	}
	/*
	public void setProperties(Set properties) {
		this.properties = properties;
	}
	*/
	public Set<Property> getHouses() {
		return houses;
	}
	public void setHouses (Set<Property> houses) {
		this.houses = houses;
	}
	
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
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
