/*
 * (c) 2008 Joe Desbonnet
 */
package ie.wombat.imagetable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * 
 * @author Joe Desbonnet
 *
 */
@Entity
@Table(name="image")
public  class Image {

	public static final int DELETED = 0;
	public static final int NORMAL = 1;
	
	public static final String MIMETYPE_JPEG = "image/jpeg";
	public static final String MIMETYPE_GIF = "image/gif";
	public static final String MIMETYPE_PNG = "image/png";
	public static final String MIMETYPE_TIFF = "image/tiff";
	public static final String SUFFIX_JPEG = ".jpg";
	public static final String SUFFIX_GIF = ".gif";
	public static final String SUFFIX_PNG = ".png";
	public static final String SUFFIX_TIFF = ".tif";
	public static final String DEFAULT_SUFFIX = SUFFIX_JPEG;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private Integer state = NORMAL;
	private String filename;
	private String caption;
	private String description;
	private String attribution;
	
	// Type of the original image
	private String mimetype;
	
	private String digest;
	
	//private Date created;
	//private Date lastModified;

	// Width and height of the original image
	private int width;
	private int height;
	
	@Column(name="im_width")
	private int intermediateWidth;
	
	@Column(name="im_height")
	private int intermediateHeight;
	
	@Column(name="tn_width")
	private int thumbnailWidth;
	
	@Column(name="tn_height")
	private int thumbnailHeight;
	
	@ManyToOne
	@JoinColumn(name="image_data_id")
	private ImageData imageData;
	
	@ManyToOne
	@JoinColumn(name="im_data_id")
	private ImageData intermediateImageData;
	
	@ManyToOne
	@JoinColumn(name="tn_data_id")
	private ImageData thumbnailImageData;
		
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * Return a suitable filename suffix for the 
	 * mimetype of the original image. The suffix includes the dot. Eg 
	 * return ".png" for PNG images.
	 * @return
	 */
	public String getFilenameSuffix () {
		if (MIMETYPE_JPEG.equals(this.mimetype)) {
			return SUFFIX_JPEG;
		}
		if (MIMETYPE_GIF.equals(this.mimetype)) {
			return SUFFIX_GIF;
		}
		if (MIMETYPE_PNG.equals(this.mimetype)) {
			return SUFFIX_PNG;
		}
		if (MIMETYPE_TIFF.equals(this.mimetype)) {
			return SUFFIX_TIFF;
		}
		return DEFAULT_SUFFIX;
	}
	public String getCaption() {
		return caption;
	}
	public void setCaption(String caption) {
		this.caption = caption;
	}
	/*
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	*/
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	public String getAttribution() {
		return attribution;
	}
	public void setAttribution(String attribution) {
		this.attribution = attribution;
	}
	
	/**
	 * Get mimetype of the original image
	 * @return
	 */
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	
	/**
	 * Get height of the original image
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public ImageData getImageData() {
		return imageData;
	}
	public void setImageData(ImageData imageData) {
		this.imageData = imageData;
	}
	public int getIntermediateHeight() {
		return intermediateHeight;
	}
	public void setIntermediateHeight(int intermediateHeight) {
		this.intermediateHeight = intermediateHeight;
	}
	public ImageData getIntermediateImageData() {
		return intermediateImageData;
	}
	public void setIntermediateImageData(ImageData intermediateImageData) {
		this.intermediateImageData = intermediateImageData;
	}
	public int getIntermediateWidth() {
		return intermediateWidth;
	}
	public void setIntermediateWidth(int intermediateWidth) {
		this.intermediateWidth = intermediateWidth;
	}
	
	/*
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	*/
	
	public int getThumbnailHeight() {
		return thumbnailHeight;
	}
	public void setThumbnailHeight(int thumbnailHeight) {
		this.thumbnailHeight = thumbnailHeight;
	}
	public ImageData getThumbnailImageData() {
		return thumbnailImageData;
	}
	public void setThumbnailImageData(ImageData thumbnailImageData) {
		this.thumbnailImageData = thumbnailImageData;
	}
	public int getThumbnailWidth() {
		return thumbnailWidth;
	}
	public void setThumbnailWidth(int thumbnailWidth) {
		this.thumbnailWidth = thumbnailWidth;
	}
	
	/**
	 * Get width of the original image
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	public boolean isDeleted () {
		return ( (this.state != null) && (this.state == DELETED) );
	}
	
}
