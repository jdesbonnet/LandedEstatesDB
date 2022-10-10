package ie.wombat.landedestates;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

/**
 * Reference. The Comparable interface sorts by the source name.
 * 
 * @author joe
 *
 */

@Entity
@Indexed
@Table(name="reference")
public class Reference implements Serializable, Comparable<Reference> {

	private static final long serialVersionUID = 1L;

	@Id
	@DocumentId
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	@Field(index = Index.YES, store = Store.NO)
	private String description;
	
	@ManyToOne
	@NotFound(action = NotFoundAction.IGNORE)
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
