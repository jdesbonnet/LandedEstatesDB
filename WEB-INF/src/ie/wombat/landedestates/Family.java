package ie.wombat.landedestates;

import java.util.HashSet;
import java.util.Set;

public class Family implements Indexable {
	private Long id;
	private String name;
	private String title;
	private String description;
	private Set<Estate> estates = new HashSet<Estate> ();
	
	private Integer projectPhase;
	
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
	
	
}
