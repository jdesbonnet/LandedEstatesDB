package ie.wombat.imagetable;

/**
 * Image dimension (package scope only). Immutable object.
 * @author joe
 *
 */
public class ImageDimension {
	private int width;
	private int height;
	public ImageDimension (int width, int height) {
		this.width = width;
		this.height = height;
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
}
