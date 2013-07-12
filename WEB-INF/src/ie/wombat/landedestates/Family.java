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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
@Table(name="family")
public class Family implements Indexable {
	
	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String name;
	private String title;
	private String description;
	
	@ManyToMany
	@JoinTable(name="estates_families",
	joinColumns=@JoinColumn(name="estate_id"),
	inverseJoinColumns = @JoinColumn(name="family_id")
	)
	private Set<Estate> estates = new HashSet<Estate>();
	
	@Column(name="project_phase")
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
