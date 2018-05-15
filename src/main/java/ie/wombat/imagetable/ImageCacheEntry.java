/*
 * (c) 2008 Joe Desbonnet
 */
package ie.wombat.imagetable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A proxy for the image cache file.  Only want to expose enough functionality
 * to read the file and not to delete etc
 * @author joe
 *
 */
public class ImageCacheEntry {

	private File cacheFile;
	private Long imageId;
	private String imageSize;
	private String mimetype;
	
	ImageCacheEntry (File cacheFile, Long imageId, 
			String imageSize, String mimetype) {
		this.cacheFile = cacheFile;
		this.imageId = imageId;
		this.imageSize = imageSize;
		this.mimetype = mimetype;
	}
	
	
	File getCacheFile() {
		return cacheFile;
	}
	public Long getImageId() {
		return imageId;
	}
	public String getImageSize() {
		return imageSize;
	}
	public InputStream getInputStream() throws FileNotFoundException {
		return new FileInputStream(cacheFile);
	}
	public String getMimetype() {
		return mimetype;
	}
	public long getLength() {
		return cacheFile.length();
	}
}
