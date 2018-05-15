/*
 * (c) 2008 Joe Desbonnet
 */
package ie.wombat.imagetable;

public class ImageMetadata {

	private ImageDimension dimension;
	private String mimetype;
	public ImageDimension getDimension() {
		return dimension;
	}
	public void setDimension(ImageDimension dimension) {
		this.dimension = dimension;
	}
	public String getMimetype() {
		return mimetype;
	}
	public void setMimetype(String mimetype) {
		this.mimetype = mimetype;
	}
	
}
