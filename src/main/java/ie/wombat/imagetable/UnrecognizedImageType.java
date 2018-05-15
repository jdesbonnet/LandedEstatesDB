package ie.wombat.imagetable;

/**
 * Thrown if the file type/encoding of the image cannot be handled.
 * 
 * @author joe
 *
 */
public class UnrecognizedImageType extends Exception {
	public UnrecognizedImageType () {
		super();
	}
	public UnrecognizedImageType (String s) {
		super(s);
	}
}
