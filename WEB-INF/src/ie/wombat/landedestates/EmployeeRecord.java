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

import org.hibernate.search.annotations.DocumentId;

@Entity
@Table(name="employee_record")
public class EmployeeRecord implements Indexable {
	
	@Id	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@DocumentId
	private Long id;
	
	
	@Column(name="document_date")
	private Date date;
	
	
	//@Column(columnDefinition="text")
	@Column(name="description", length=128000) // portable
	private String description;
	
	//@ManyToOne
	//private Property house;
	
	
	@OneToMany
	@JoinTable(name="employee_record_to_reference",
		joinColumns=@JoinColumn(name="employee_record_id"),
		inverseJoinColumns = @JoinColumn(name="reference_id")
	)
	private Set<Reference> references = new HashSet<Reference>();
	
	@ManyToMany
	@JoinTable(name="employee_record_to_tag",
		joinColumns=@JoinColumn(name="employee_record_id"),
		inverseJoinColumns = @JoinColumn(name="tag_id")
	)
	private Set<Tag> tags = new HashSet<Tag>();
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Set<Reference> getReferences() {
		return references;
	}
	public void setReferences(Set<Reference> references) {
		this.references = references;
	}
	@Override
	public String getLuceneId() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
