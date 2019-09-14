/*
 * Created on Feb 20, 2004
 * (c) 2008 Joe Desbonnet
 */
package ie.wombat.imagetable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




/**
 * 
 * ImageTable image servlet which serves images from database. Config params:
 * <ul>
 * <li> cacheDir: directory to use for caching images. Must already exist and 
 * be writeable.
 * <li> cacheFor: duration in seconds to keep images in disk cache.
 * </ul>
 * 
 * TODO: Bug (discovered 21 May 2006): Vulnerable period when a cached image is created.
 * Eg if a thumbnail is referenced twice in a very short period of time, the first
 * attempt succeeds, but the second attempt fails because it thinks there is a cache 
 * file entry but the creation of the file is not yet complete.
 * 
 * @author joe
 *
 */
public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final long DEFAULT_CACHE_PERIOD = 24*3600*1000;


	//private static final File DEFAULT_CACHE_DIR = new File ("/var/tmp");

	
	// Default to Image class name TODO: no default
	private String imageEntityName = Image.class.getName();
	
	//static File imageCache = DEFAULT_CACHE_DIR;
	
	private ImageDB imagedb;
	private long cacheFor = DEFAULT_CACHE_PERIOD;
	

	private static Logger log = LoggerFactory.getLogger (ImageServlet.class);

	/**
	 * Initialize servlet. Look for parameters imageEntityName (mandatory),
	 * cacheDir (directory to use for disk caching), 
	 * cacheFor (Expires http headers will be set for this number
	 * of seconds in the future).
	 */
	public void init (ServletConfig config) {
		String en = config.getInitParameter("imageEntityName");
		if (en != null) {
			imageEntityName = en;
		}
		
		log.info("Initializing ImageTable " + ImageDB.VERSION);
		log.info ("servletName=" 
				+ config.getServletName() 
				+ " imageEntityName=" + imageEntityName);
		
		imagedb = ImageDB.getInstance(imageEntityName);
		
		// Set SQL table name specified (for JDBC blob access)
		// (null value disables this feature)
		imagedb.setSqlTableName(config.getInitParameter("sqlTableName"));
		log.info ("Image table:" + imagedb.getSqlTableName());
		log.info ("Image data table:" + imagedb.getSqlTableName()+"_data");
		
		// Location of tmp dir
		String tmpDirName = config.getInitParameter("tmpDir");
		if (tmpDirName != null) {
			File d = new File (tmpDirName);
			d.mkdirs();
			if (d.exists() && d.isDirectory() && d.canWrite()) {
				imagedb.setTmpDir(d);
			} else {
				log.error("Unable to use directory " + d.getPath() 
						+ " as tmp dir. Possible causes: does not "
						+ " exist, not directory or now writeable. "
						+ " Fall back to default: " + imagedb.getTmpDir());
			}
		}
		
		// Location of cache
		String imageCacheDirName = config.getInitParameter("cacheDir");
		if (imageCacheDirName != null) {
			File d = new File (imageCacheDirName);
			d.mkdirs();
			if (d.exists() && d.isDirectory() && d.canWrite()) {
				imagedb.setCacheDir(d);
			} else {
				log.error("Unable to use directory " + d.getPath() 
						+ " as image cache dir. Possible causes: does not "
						+ " exist, not directory or now writeable. "
						+ " Fall back to default: " + imagedb.getCacheDir());
			}
		}
		
		String cacheForStr = config.getInitParameter("cacheFor");
		if (cacheForStr != null) {
			try {
				this.cacheFor = Long.parseLong(cacheForStr);
				this.cacheFor *= 1000; // convert from seconds to ms
			} catch (Exception e) {
				// Log error, but otherwise ignore and use default
				log.error("Error parsing 'cacheFor' servlet init parameter. Expected integer but got '" + cacheForStr + "'");
			}
		}
		//imagedb = ImageDB.getInstance(imageEntityName);
		//imagedb.setCacheDir (imageCache);
	}
	
	/**
	 * Return image binary. Request path /(size/filter)/(id).jpg
	 */
	public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		
		String pathInfo = request.getPathInfo();
		
		log.info("doGet() pathInfo=" + pathInfo);
		
		String[] p = pathInfo.split("/");
		
		// Remove any blank nodes in pathInfo
		String[] p2 = new String[p.length];
		int j = 0;
		for (int i = 0; i < p.length; i++) {
			if (p[i].length() > 0) {
				p2[j++] = p[i];
			}
		}
		
		// Image size or filter name
		String imageSize = p2[0];
		
		// Image id (usually suffixed with .jpg 
		String imageIdStr = p2[1];
		if (imageIdStr.endsWith(".jpg")) {
			imageIdStr = imageIdStr.substring(0,imageIdStr.length()-4);
		}
		
		Long imageId = new Long(imageIdStr);
		
		if (imageId.longValue() == 0) {
			error404 (response, imageId);
			return;
		}
		
		//response.setDateHeader("Expires", System.currentTimeMillis() + this.cacheFor);
		
		// Get image data for imageId. The getImageData tries to avoid
		// opening session to db if data available in cache
		
		log.info("calling imagedb.getImageData( " + imageId + "," + imageSize + "));");
		
		ImageCacheEntry entry = imagedb.getImageData(imageId, imageSize);
		
		// Null return value indicates image not found. Return 404.
		if (entry == null) {
			error404(response,imageId);
			return;
		}
		
		// Stream result back to client
		InputStream in = entry.getInputStream();
		response.setContentType(entry.getMimetype());
		response.setContentLength((int)entry.getLength());
		IOUtils.copy(in, response.getOutputStream());
		in.close();
	}
	
	private final void error404(HttpServletResponse response, Long imageId) 
	throws IOException {
		response.setStatus(404);
		response.setContentType("text/plain");
		response.getOutputStream().println("Error 404: " 
				+ imageEntityName + "#" + imageId + " not found. It may" +
						" have been deleted.");
	}
}
