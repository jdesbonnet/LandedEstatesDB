/*
 * (c) 2008 Joe Desbonnet
 */
package ie.wombat.imagetable;


import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.iptc.IptcDirectory;

import net.sf.json.util.JSONUtils;

public class ImageUtil {

	private static Logger log = Logger.getLogger (ImageUtil.class);
	
	public static void main (String[] arg) throws Exception {
		System.out.println (calcMeanIntensity(new File(arg[0])));
	}
	/**
	 * Experimental code to get mean intensity of top of image (to 
	 * decide black or white overlay text.
	 * @param imageFile
	 * @throws IOException
	 */
	public static int calcMeanIntensity(File imageFile) 
	throws IOException {

		if (!imageFile.exists()) {
			throw new FileNotFoundException("file " + imageFile.getPath() + " not found.");
		}
		
		BufferedImage imageIn = ImageIO.read(imageFile);
		
		if (imageIn == null) {
			throw new IOException ("error reading image data from file " + imageFile.getPath());
		}

		int w = imageIn.getWidth();
		int h = imageIn.getHeight();

		int maxh = h / 10;
		int maxw = w / 2;
		int c, x, y, r, g, b;
		int sigma_i = 0;
		for (y = 0; y < maxh; y++) {
			for (x = 0; x < maxw; x++) {
				c = imageIn.getRGB(x, y);
				r = (c >> 16) & 0xff;
				g = (c >> 8) & 0xff;
				b = c & 0xff;
				sigma_i += (r + g + b);
			}
		}
		int mean_i = sigma_i / (maxw * maxh * 3);
		return mean_i;
	}
	
	/**
	 * Convert Image object to JSON
	 * 
	 * @param image
	 * @param out
	 * @throws IOException
	 */
	public static void writeImageJson (Image image, Writer out) 
	throws IOException {
		writeImageJson(image,out,false);
	}
	
	public static void writeImageJson (Image image, Writer out, boolean includeTags) 
	throws IOException {
		out.write ("{id:" + image.getId());
		if (image.getCaption() != null) {
			out.write (",caption:" + JSONUtils.quote(image.getCaption()));
		}
		/*
		if (image.getDescription() != null && image.getDescription().length() > 0) {
			out.write (",description:\"" + image.getDescription() + "\"");
		}
		*/
		if (image.getAttribution() != null) {
			out.write (",attribution:"  + JSONUtils.quote(image.getAttribution()));
		}
			
		out.write ("}");
	}
	
	
	// This defines what Image Metadata Tag IDs we're interested in.
	private static final int TAG_CAPTION = 0x0278;
	private static final int TAG_COPYRIGHT_NOTICE = 0x0274;
	private static final int TAG_SOURCE = 0x0273;
	private static final int TAG_LOCATION = 0x025c;
	
	/**
	 * Use embedded metadata in the original image data to update caption, and attribution fields.
	 * 
	 * @param image
	 * @param cacheEntry
	 * @throws IOException
	 */
	public static void updateImagePropertiesFromEmbeddedMetadata (Image image, ImageCacheEntry cacheEntry) 
		throws IOException {
		
		try {
		BufferedInputStream bufin = new BufferedInputStream(cacheEntry.getInputStream());
		Metadata metadata = ImageMetadataReader.readMetadata (bufin);
		Directory iptcDirectory = metadata.getDirectory(IptcDirectory.class);
		log.info ("iptcDirectory=" + iptcDirectory);
		Iterator<com.drew.metadata.Tag> iter = iptcDirectory.getTagIterator();
		while (iter.hasNext()) {
			com.drew.metadata.Tag tag = iter.next();
			log.info ("    examining tag " + tag + " of type " + tag.getTagType() );
			switch (tag.getTagType()) {
			case TAG_CAPTION:
				image.setCaption(tag.getDescription());
				break;
			case TAG_COPYRIGHT_NOTICE:
				image.setAttribution(tag.getDescription());
				break;
			}
		}
		log.info ("all tags examined");
		} catch (MetadataException e) {
			// Convert to IOException
			throw new IOException (e);
		} catch (ImageProcessingException e) {
			// Convert to IOException
			throw new IOException (e);
		}
	}
}
