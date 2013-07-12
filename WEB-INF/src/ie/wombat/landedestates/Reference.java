package ie.wombat.landedestates;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;

/**
 * Reference. The Comparable interface sorts by the source name.
 * 
 * @author joe
 *
 */

@Entity
@Indexed
@Table(name="reference")
public class Reference implements Comparable<Reference> {

	@Id
	@DocumentId
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String description;
	
	@ManyToOne
	private ReferenceSource source;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ReferenceSource getSource() {
		//System.err.println ("warning: have reference without source!");
		return source;
	}
	public void setSource(ReferenceSource source) {
		this.source = source;
	}
	public int compareTo(Reference o) {
		Reference ref = (Reference)o;
		if (ref.getSource() == null || ref.getSource().getName() == null) {
			return 0;
		}
		if (getSource() == null || getSource().getName() == null) {
			return 0;
		}
		return getSource().getName().compareTo(ref.getSource().getName());
	}
	
}
