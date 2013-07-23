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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Indexed
@Table(name="family")
@XmlRootElement
public class Family implements Indexable {
	
	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String name;
	
	private String title;
	
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String description;
	
	@ManyToMany
	@JoinTable(name="estate_families",
	joinColumns=@JoinColumn(name="family_id"),
	inverseJoinColumns = @JoinColumn(name="estate_id")
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
	
}
