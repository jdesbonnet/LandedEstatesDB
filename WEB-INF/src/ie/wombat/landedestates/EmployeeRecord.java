package ie.wombat.landedestates;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="employee_record")
public class EmployeeRecord implements Indexable {
	
	@Id
	private Long id;
	
	//@Column(columnDefinition="text")
	@Column(length=128000) // portable
	private String description;
	
	//@ManyToOne
	//private Property house;
	
	
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
