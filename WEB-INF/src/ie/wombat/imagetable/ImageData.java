/*
 * (c) 2008 Joe Desbonnet
 */
package ie.wombat.imagetable;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

@Entity
@Table(name="image_data")
public class ImageData {

	private static Logger log = Logger.getLogger (ImageData.class);
	
	// Standard size constants
	public static final String SIZE_ORIGINAL = "orig";
	public static final String SIZE_INTERMEDIATE = "im";
	public static final String SIZE_THUMBNAIL = "tn";
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String mimetype;
	
	@Transient
	private Integer size;
	
	@Lob
	private byte[] data;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
