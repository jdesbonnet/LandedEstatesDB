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
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Store;

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
	@Field(index = Index.TOKENIZED, store = Store.NO)
	private String description;
	
	
	// House is not needed. Left here for the moment.
	@ManyToOne
	@JoinColumn(name="house_id")
	private House house;
	
	@ManyToOne
	@JoinColumn(name="estate_id")
	private Estate estate;
	
	@OneToMany
	@JoinTable(name="join_employee_record_to_reference",
		joinColumns=@JoinColumn(name="employee_record_id"),
		inverseJoinColumns = @JoinColumn(name="reference_id")
	)
	@OrderBy("id")
	private Set<Reference> references = new HashSet<Reference>();
	
	@ManyToMany
	@JoinTable(name="join_employee_record_to_tag",
		joinColumns=@JoinColumn(name="employee_record_id"),
		inverseJoinColumns = @JoinColumn(name="tag_id")
	)
	@OrderBy("name")
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
	
	
	
	public Set<Tag> getTags() {
		return tags;
	}
	public void setTags(Set<Tag> tags) {
		this.tags = tags;
	}
	public void addTag (Tag tag) {
		this.tags.add(tag);
	}
	public void removeTag (Tag tag) {
		this.tags.remove(tag);
	}
	
	public House getHouse() {
		return house;
	}
	public void setHouse(House house) {
		this.house = house;
	}
	
	
	public Estate getEstate() {
		return estate;
	}
	public void setEstate(Estate estate) {
		this.estate = estate;
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
