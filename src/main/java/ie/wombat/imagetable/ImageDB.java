/*
 * Created on Feb 21, 2004
 * (c) Joe Desbonnet
 */
package ie.wombat.imagetable;


import ie.wombat.landedestates.HibernateUtil;
import ie.wombat.template.XSS;

//import java.io.ByteArrayInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;


import java.io.IOException;
import java.io.InputStream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.devlib.schmidt.imageinfo.ImageInfo;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * General purpose ImageDB. 
 * @author joe
 *  
 */
public class ImageDB {

	public static final String VERSION = "0.7.10";
	private static final String DEFAULT_IMAGE_ENTITY_NAME="Image";
	
	/** Quality factor used in thumbnail JPEG compression */
	static final double TN_JPEG_Q = 0.75;

	/** Quality factor used in intermediate size image JPEG compression */
	static final double IM_JPEG_Q = 0.85;

	private static final int MAX_AGE = 3600000;

	private static Logger log = LoggerFactory.getLogger(ImageDB.class);

	private static final int BUF_SIZE = 4096;
	// Thumbnail bounding box
	private static final int TN_MAX_WIDTH = 120;
	private static final int TN_MAX_HEIGHT = 120;

	// Intermediate size bounding box
	private static final int IM_MAX_WIDTH = 800;
	private static final int IM_MAX_HEIGHT = 800;
	private static final int IM_MIN_WIDTH = 320;
	private static final int IM_MIN_HEIGHT = 320;

	private static final String DEFAULT_TMP_DIR = "/var/tmp";
	private static File tmpDir = new File(DEFAULT_TMP_DIR);
	
	
	private static final String DEFAULT_CACHE_DIR = DEFAULT_TMP_DIR;
	//private static File cacheDir = new File(DEFAULT_CACHE_DIR);
	private File cacheDir = null;
	
	// If set, use JDBC to for blob operations
	private String sqlTableName = null;
	
	// Counter, seeded with current system time
	static long tmpFileGen = System.currentTimeMillis();
	
	// Array of Image IDs for random image selection
	private static HashMap<String, Long[]> imageIdsHash = new HashMap<String, Long[]>();
	private static HashMap<String, Long> imageIdsTimeStampHash = new HashMap<String, Long>();

	
	//private static ImageDB instance = new ImageDB();
	private static HashMap<String, ImageDB> instanceMap = new HashMap<String, ImageDB>();
	
	private String imageEntityName;
	private String imageDataEntityName;
	private String imageTagEntityName;
	
	private int imMaxWidth = IM_MAX_WIDTH;
	private int imMaxHeight = IM_MAX_HEIGHT;
	
	private ImageDB() {
	}

	public static synchronized ImageDB getInstance(String imageEntityName) {
		ImageDB db = (ImageDB)instanceMap.get(imageEntityName);
		if (db == null) {
			db = new ImageDB();
			db.setImageEntityName(imageEntityName);
			instanceMap.put(imageEntityName, db);
		}
		return db;
	}

	/**
	 * Set cache directory. Package scope only.
	 * @param d
	 */
	void setCacheDir (File d) {
		this.cacheDir = d;
	}
	
	/**
	 * Get cache directory.
	 * @return
	 */
	File getCacheDir () {
		return this.cacheDir;
	}
	
	
	
	protected String getSqlTableName() {
		return sqlTableName;
	}

	/**
	 * If set, use JDBC for blob operations. Image data table name is 
	 * assumed to have a "_data" suffix eg general_image_data
	 * 
	 * @param sqlTableName
	 */
	protected void setSqlTableName(String sqlTableName) {
		this.sqlTableName = sqlTableName;
	}

	/**
	 * Set tmp directory for intermediate tmp files
	 * @param d
	 */
	void setTmpDir (File d) {
		tmpDir = d;
	}
	
	File getTmpDir () {
		return tmpDir;
	}
	
	/**
	 * Sets the image entity name (as defined in Hibernate configuration). Must
	 * be set prior to use.
	 * @return
	 */
	public String getImageEntityName() {
		return imageEntityName;
	}
	
	/**
	 * Set the intermetidate image bounding box
	 */
	public void setIntermediateImageBounds (int w, int h) {
		this.imMaxHeight = w;
		this.imMaxHeight = h;
	}
	
	/**
	 * Set the Image entity name (as defined in Hibernate configuration).
	 * @param imageEntityName
	 */
	public void setImageEntityName(String imageEntityName) {
		this.imageEntityName = imageEntityName;
		this.imageDataEntityName = imageEntityName+"Data";
		this.imageTagEntityName = imageEntityName+"Tag";
	}

	public Image createImage (InputStream in) throws IOException, UnrecognizedImageType
	{
		EntityManager em = HibernateUtil.getEntityManager();
		Image image = createImage (em,in); // TODO: ??????
		return image;
	}
	
	/**
	 * Create an image using and existing Hibernate session 
	 * and an InputStream. It is assumed that a transaction
	 * is already setup on the session.
	 */
	
	
	public Image createImage(EntityManager em, InputStream in) 
		throws IOException, UnrecognizedImageType {
		Image image = new Image();
		replaceImage(em, image, in);
		return image;
	}
	
	/**
	 * @deprecated Causes memory problems.
	 * @param hsession
	 * @param origImgData
	 * @return
	 * @throws IOException
	 * @throws UnrecognizedImageType
	 */
	/*
	public Image createImage(Session hsession, byte[] origImgData) 
	throws IOException, UnrecognizedImageType {
		Image image = new Image();
		replaceImage (hsession, image, origImgData);
		return image;
	}
	*/
	
	/**
	 * Save image by invoking hibernate session save() on object.
	 * 
	 * @param hsession
	 * @param image
	 */
	public void saveImage (Session hsession, Image image) {
		hsession.save(imageEntityName, image);
	}
	
	/**
	 * Replace (or set) image binary and metadata. Use discouraged. If an image
	 * binary needs to be changed best to delete old and create new. One exception
	 * is backup restore.
	 * 
	 * @param hsession
	 * @param image
	 * @param origImgData
	 * @throws IOException
	 * @throws UnrecognizedImageType
	 */
	public void replaceImage(EntityManager em, Image image, InputStream in) 
		throws IOException, UnrecognizedImageType {
		
		// Unfortunate necessitity to write this to a file
		
		File tmpFile = newTmpFile ();
		OutputStream out = new FileOutputStream(tmpFile);
		log.info("replaceImage(): writing input stream to " + tmpFile.getPath());
		IOUtils.copy(in, out);
		out.close();
		
		
		// Determine dimensions and mimetype
		log.info("replaceImage(): checking dimensions of " + tmpFile.getPath());
		InputStream in2 = new FileInputStream (tmpFile);
		ImageInfo ii = new ImageInfo();	
		ii.setInput(in2);
		ii.check();
		log.info ("replaceImage(): dimensions are " + ii.getWidth() + " x " + ii.getHeight());
		ImageDimension origd = new ImageDimension (ii.getWidth(), ii.getHeight());
		ImageDimension imd = getIntermediateDimension(ii.getWidth(), ii.getHeight());
		
		
		
		String origImageMimetype = ii.getMimeType();
		in2.close();
		
		/*
		 * Generate intermediate size only if original exceeds im bounding box
		 */
		if (imd == null) {
			log.info("no im (intermediate) size image required");
		} else {
		
			int imw = imd.getWidth();
			int imh = imd.getHeight();
		
			log.info ("replaceImage(): im dimensions are " + imw + " x " + imh);
			
			log.info("replaceImage(): scaling orig to im size");
			File imFile = ImageMagick.scale(tmpFile, imw, imh, "jpeg", 85);
			log.info("replaceImage(): im output in " + imFile.getPath());
			ImageData imData = new ImageData();
			
			imData.setSize((int)imFile.length());
			imData.setMimetype("image/jpeg");
			imData.setData(IOUtils.toByteArray(new FileInputStream(imFile)));
			em.persist(imData);
			
			// In theory we can work this out mathematically, but to be
			// safe we re-read the image
			log.info("replaceImage(): re-reading im dimensions");
			in2 = new FileInputStream(imFile);
			ii = new ImageInfo();
			ii.setInput(in2);
			ii.check();
			log.info ("replaceImage(): im dimensions are " + ii.getWidth() + " x " + ii.getHeight());
			image.setIntermediateWidth(ii.getWidth());
			image.setIntermediateHeight(ii.getHeight());
			in2.close();
			
			image.setIntermediateImageData(imData);
			em.persist(image);
		}
		
		/*
		 * Generate thumbnail
		 */
		int tnw = TN_MAX_WIDTH;
		int tnh = TN_MAX_HEIGHT;
		File tnFile = ImageMagick.scale(tmpFile, tnw, tnh, "jpeg", 85);
		ImageData tnData = new ImageData();
		tnData.setSize((int)tnFile.length());
		tnData.setMimetype("image/jpeg");
		log.debug("getting id for thumbnail data, entityName=" + imageDataEntityName);
		tnData.setData(IOUtils.toByteArray(new FileInputStream(tnFile)));
		em.persist(tnData);
		
		
		/*
		 *  Original image
		 */
		ImageData data = new ImageData();
		data.setSize((int)tmpFile.length());
		data.setMimetype(origImageMimetype);
		data.setData(IOUtils.toByteArray(new FileInputStream(tmpFile)));
		image.setMimetype(origImageMimetype);
		image.setWidth(origd.getWidth());
		image.setHeight(origd.getHeight());
		
		// Thumbnail dimensions
		// In theory we can work this out mathematically, but to be
		// safe we re-read the image
		in2 = new FileInputStream(tnFile);
		ii = new ImageInfo();
		ii.setInput(in2);
		ii.check();
		image.setThumbnailWidth(ii.getWidth());
		image.setThumbnailHeight(ii.getHeight());
		in2.close();
		
		
		image.setImageData(data);
		
		image.setThumbnailImageData(tnData);
		
		em.persist(data);
		em.persist(image);		
	}


	
	/**
	 * Get dimensions of IM image, or null if IM generation not required.
	 * @param w
	 * @param h
	 * @return
	 */
	private ImageDimension getIntermediateDimension (int w, int h) {
		
		// Check if orig fits in intermetidate bounds. If so return null
		// (signalling that an im image is not necessary)
		if (w <= imMaxWidth && h <= imMaxHeight) {
			return null;
		}
		
		// One of the image dimensions exceeds intermediate image bounds
		
		float fw = (float)w;
		float fh = (float)h;
		float ws = fw/(float)imMaxWidth;
		float wh = fh/(float)imMaxHeight;
		
		float div = Math.max(ws, wh);
		
		int imw = (int)(fw/div);
		int imh = (int)(fh/div);
		
		return new ImageDimension(imw,imh);
	}
	
	
	private byte[] digestData (byte[] data) {
		MessageDigest md;

		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			log.error(e.toString());
			e.printStackTrace();
			throw new AppEnvError(e);
		}
		
		return md.digest(data);
	}
	

	/**
	 * Handle image upload from form (from image-new.jsp).
	 * Request requirements: form must use POST method 
	 * and enctype="multipart/form-data".
	 * The uploaded file can be in parameter 'file1'
	 * or as a URL in 'url'. 
	 * 
	 * (delete)
	 * For convenience, on return, the param Map will be attached as an
	 * attribute to the request object. This enables easy access to request
	 * parameters. Use request.getAttribute("param") to access.
	 * (end delete)
	 * 
	 * @param hsession Hibernate Session (open and transaction in progress)
	 * @param request HttpServletRequest object containing the image
	 * @param params A Properties object which will contain all the form params after returning 
	 * @return Upload images (normally array of one element, but in case of ZIP archive that
	 * can be more than one).
	 * 
	 * @throws IOException
	 * @throws FileUploadException
	 */
	
	public static List<Image> handleImageUpload(EntityManager em, 
			ImageUpload imageUpload) 
	throws IOException, FileUploadException, UnrecognizedImageType {

		log.info("handleImageUpload() called");
		
		List<FileItem> fileItems = imageUpload.getFileItems();
		Properties param = imageUpload.getProperties();
		
		// Now have parameters in 'params' and list of files in 'fileItems'
		
		log.info("Found " + fileItems.size() + " file items.");
		
		String imageEntityName = param.getProperty("entity_name");
		if (imageEntityName == null) {
			imageEntityName = DEFAULT_IMAGE_ENTITY_NAME;
			log.warn ("missing entity_name parameter, using default");
			//throw new FileUploadException ("missing entity_name parameter");
		}
		
		ImageDB db = ImageDB.getInstance(imageEntityName);
		
		if (db == null) {
			throw new FileUploadException ("unknown image entity name " + imageEntityName);
		}
		

		String caption = XSS.clean(param.getProperty("caption"));
		String description = XSS.clean(param.getProperty("description"));
		String attribution = XSS.clean(param.getProperty("attribution"));
		
		// TODO: get tags
		// Tags can be specified by the presence of tag_nnn params, or
		// a single 'tags' param with comma separated list
		

		
		// If 'url' param present, get image using HTTP
		String url = (String)param.get("url");
		if (url != null && url.startsWith("http")) {
			HttpClient client = new HttpClient();
			client.setTimeout(15000);
			GetMethod get = new GetMethod(url);
			int status = client.executeMethod(get);
			if (status != 200) {
				throw new FileUploadException("Remote web server return status != 200 in response to request for " + url);
			}
			
			InputStream in = get.getResponseBodyAsStream();
			
			Image image = db.createImage (em, in);
			image.setFilename(url);
			image.setCaption(caption);
			image.setDescription(description);
			image.setAttribution(attribution);
			em.persist(image);
			List<Image> ret = new ArrayList<Image>(1);
			ret.add(image);
			return ret;
			
		} 

		List<Image> ret = new ArrayList<Image>();
		
		for (FileItem item : fileItems) {
			if (item == null || item.getName().length() == 0) {
				throw new FileUploadException("no upload file found in request");
			}
			// Get file name from full path
			String filename = db.extractFileName(item);
			InputStream in = item.getInputStream();
		
			if (filename.endsWith(".zip") || filename.endsWith(".ZIP")) {
				List<Image> images = db.addZipArchive(em, in);
				for (Image image : images) {
					image.setCaption(caption);
					image.setDescription(description);
					image.setAttribution(attribution);
					em.persist(image);
				}
				ret.addAll(images);
			} else {
				Image image = db.createImage (em, in);
				image.setFilename(filename);
				image.setCaption(caption);
				image.setDescription(description);
				image.setAttribution(attribution);
				em.persist(image);
				ret.add(image);
			}
		}
		
		return ret;
	}
	

	public List<Image> addZipArchive(EntityManager em, InputStream in)
			throws IOException {

		ZipInputStream zipIn = new ZipInputStream(in);
		ZipEntry zipEntry;

		List<Image> list = new ArrayList<Image>();
		//byte[] buf = new byte[BUF_SIZE];
		byte data[] = new byte[BUF_SIZE];
		
		while ((zipEntry = zipIn.getNextEntry()) != null) {

			log.info("processing " + zipEntry.getName());
			log.info("size=" + zipEntry.getSize());
			int imgSize = (int) zipEntry.getSize();

			if (imgSize <= 0) {
				log.warn("Ignoring entry " + zipEntry.getName()
						+ " as size is zero or negative size="
						+ zipEntry.getSize());
				continue;
			}

			// Prefer to write to file than load into memory.
			// TODO: see if we can construct a ZipEntryInputStream
			// byte[] imgData = new byte[imgSize];
			File tmpFile = newTmpFile();
			FileOutputStream tmpOut = new FileOutputStream(tmpFile);

			BufferedOutputStream dest = new BufferedOutputStream(tmpOut, BUF_SIZE);
			int count;
			while ((count = zipIn.read(data, 0, BUF_SIZE)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();

			try {
				FileInputStream tmpIn = new FileInputStream(tmpFile);
				Image image = createImage(em, tmpIn);
				tmpIn.close();
				tmpFile.delete();
				image.setFilename(zipEntry.getName());
				list.add(image);
			} catch (IOException e) {
				log.error(e.toString());
				continue;
			} catch (UnrecognizedImageType e) {
				log.error(e.toString());
				continue;
			} finally {
				zipIn.closeEntry();
			}
			
			log.info("Completed processing " + tmpFile.getPath());
		}
		return list;
	}

	public void deleteImage (Long imageId) {
		EntityManager em = HibernateUtil.getEntityManager();
		deleteImage (em, imageId);
	}
	
	public void deleteImage (EntityManager em, Long imageId) {
		Image image = (Image)em.find(Image.class,imageId);
		deleteImage (em,image);
	}
	
	public void deleteImage (EntityManager em, Image image) {
		if (! image.isDeleted()) {
			image.setState(Image.DELETED);
		}
	}
	public void undeleteImage (EntityManager em, Image image) {
		if (image.isDeleted()) {
			image.setState(Image.NORMAL);
		}
	}
	
	/**
	 * Really remove image data from tables. After purge undelete is not possible.
	 * @param hsession
	 * @param image
	 */
	public void purgeImage (Session hsession, Image image) {
		
		if (! image.isDeleted()) {
			// TODO: throw exception
			log.error ("Attempting to purge " + image + ". Delete before purging.");
			return;
		}
		
		hsession.delete (image.getThumbnailImageData());
		
		// Small images may not have an im size. Check for null.
		if (image.getIntermediateImageData() != null) {
			hsession.delete (image.getIntermediateImageData());
		}
		
		hsession.delete (image.getImageData());
		
		hsession.delete (image);
		
	}
	
	public Image getImage (Session hsession, Long imageId) {
		return (Image)hsession.load(imageEntityName, imageId);
	}
	

	
	/**
	 * Get List of Tags matching prefix
	 * 
	 * @param prefix
	 * @return
	 */
	/*
	public List<Tag> getTagsWithPrefix (String prefix) {

		org.hibernate.Session hsession = HibernateUtil.currentSession();
		org.hibernate.Transaction tx = hsession.beginTransaction();

		List<Tag> ret = hsession.createQuery("from " + imageTagEntityName + " where name like '" + prefix + "%'").list();
	
		tx.commit();
		HibernateUtil.closeSession();
		
		return ret;
	}
	*/
	
	/**
	 * Get a random image from images matching tagId
	 * @param hsession
	 * @param tagId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public synchronized Image getRandomImage (Session hsession, String tagId) {
		
		long now = System.currentTimeMillis();
		
		Long[] ids = (Long[])imageIdsHash.get(tagId);
		Long ts = (Long)imageIdsTimeStampHash.get(tagId);
		long t = ts == null ? 0 : ts.longValue();
		if (ids == null || (now - t) > MAX_AGE) {
			List<Image> list = hsession.createQuery("from " + imageEntityName 
					+ " where tags.id=" + tagId).list();
			
			ids = new Long[list.size()];
			for (int i = 0; i < ids.length; i++) {
				Image image = (Image)list.get(i);
				ids[i] = image.getId();
			}
			imageIdsHash.put(tagId, ids);
			imageIdsTimeStampHash.put (tagId, new Long(now));
		}
		
		int randomIndex = (int)(Math.random() * (double)ids.length);
		return (Image)hsession.load(imageEntityName, ids[randomIndex]);
	}
	
	/**
	 * Retrieve image data (eg for display) as ImageCacheEntry. First
	 * look up cache, if not in cache retreive from db, apply any necessary
	 * transform, put copy in cache and return. 
	 * 
	 * Return null if record not found.
	 * 
	 * @param imageId
	 * @param imageSize
	 * @return
	 * @throws IOException
	 */
	public ImageCacheEntry getImageData (Long imageId, String imageSize) 
		throws IOException {
		
		EntityManager em = HibernateUtil.getEntityManager();
		
		if (log.isDebugEnabled()) {
			log.debug ("getImageData(" + imageId + "," + imageSize + ")");
		}
		
		// Look up disk cache
		
		ImageCacheEntry entry = getImageFromCache (imageId, imageSize);
		
		if (entry != null) {
			
			if (log.isDebugEnabled()) {
				log.debug ("getImageData(): " + imageId + " " + imageSize 
					+ " found in cache at " + entry.getCacheFile().getPath() 
					+ ", "
					+ entry.getLength() + " bytes in size, returning.");
			}
			return entry;
		}
		
		// Cache miss, fetch from db and populate cache
		
		if (log.isDebugEnabled()) {
			log.debug (imageId + " " + imageSize 
				+ " cache miss, opening hibernate session");
		}
		
		// Retrieve from db, renderImage() does the heavy lifting here
			
		Image image = em.find(Image.class,imageId);
		
		// Image not found
		if (image == null) {
			return null;
		}
			
		if (log.isDebugEnabled()) {
				log.debug ("getImageData(): " + imageId + " " + imageSize  
					+ " got image record " + image + ", now calling renderImage()");
		}
			
		entry = renderImage(image, imageSize);
			
		return entry;
				
	}
	
	/**
	 * Return entry from cache, or null if not found.
	 * @param imageId
	 * @param imageSize
	 * @return
	 * @throws IOException
	 */
	private ImageCacheEntry getImageFromCache (Long imageId, String imageSize)
		throws IOException {
		
		File cacheFile = getCacheFile(imageId, imageSize);
		
		if (log.isDebugEnabled()) {
			log.debug ("getImageFromCache(" + imageId + "," + imageSize 
					+ "): " + imageId + " " + imageSize 
				+ " cacheFile=" + cacheFile.getPath());
		}
		
		if (!cacheFile.exists()) {
			if (log.isDebugEnabled()) {
				log.debug ("getImageFromCache(): " + imageId + " " + imageSize  
					+ " not found in cache, returning null");
			}
			return null;
		}
		
		if (log.isDebugEnabled()) {
			log.debug ("getImageFromCache(): " + imageId + " " + imageSize  + " found at " 
				+ cacheFile.getPath() + ", size=" + cacheFile.length());
		}
		
		// TODO: fix hard coding of mimetype
		ImageCacheEntry entry = new ImageCacheEntry(cacheFile, 
				imageId, imageSize, "image/jpeg");
		
		return entry;
	}
	
	private File getCacheFile (Long imageId, String imageSize) 
		throws FileNotFoundException {
		
		if (log.isDebugEnabled()) {
			log.debug ("getCacheFile(" + imageEntityName + "#" + imageId 
					+ "," + imageSize + ")");
			log.debug ("getCacheFile(): cacheDir=" + cacheDir);
		}
		if (cacheDir == null) {
			throw new FileNotFoundException ("cacheDir has not been set");
		}
		return new File (cacheDir, imageEntityName + "." + imageId + "." + imageSize + ".jpg");
	}
	
	private void writeImageDataToCacheFile (ImageData data, File cacheFile) 
		throws IOException {
		
		if (log.isDebugEnabled()) {
			log.debug ("writeImageDataToCacheFile(" 
					+ data + "," + cacheFile.getPath());
		}
		
		OutputStream out = new FileOutputStream(cacheFile);
		out.write(data.getData());
		out.close();
		
	}
	
	private void getBlobUsingJDBC (Connection connection, String tableName, 
			Long recordId, File cacheFile) 
		throws SQLException, IOException {
		String sql = "select data from " + tableName + "_data where id="
						+ recordId;
		log.debug("getBlobUsingJDBC(): sql="+sql);
		ResultSet rs = connection.createStatement().executeQuery(
						sql);
		
		if ( ! rs.next()) {
			throw new FileNotFoundException (imageDataEntityName+"#" + recordId
					+ " not found in database.");
		}
		
		Blob blob = rs.getBlob(1);
		InputStream in = blob.getBinaryStream();
		OutputStream out = new FileOutputStream(cacheFile);
		IOUtils.copy(in, out);
		out.close();
		in.close();
		rs.close();
	}
	
	private void setBlobUsingJDBC(Connection connection, String tableName,
			Long recordId, File cacheFile) throws SQLException, IOException {
		String sql = "update " + tableName + "_data set data=? where id="
				+ recordId;
		log.debug("setBlobUsingJDBC(): sql=" + sql);
		PreparedStatement ps = connection.prepareStatement(sql);
		InputStream in = new FileInputStream(cacheFile);
		int length = (int)cacheFile.length();
		ps.setBinaryStream(1, in, length);
		ps.executeUpdate();
		ps.close();
	}
	
	private void reportJdbcDataNotFound (Image image, String imageSize) 
		throws FileNotFoundException {
		throw new FileNotFoundException (imageEntityName + "#" + image.getId()
				+ " size " + imageSize + " not found in database.");
	}
	
	/**
	 * Render image and enter into cache. Return cache entry. If already in 
	 * cache, the cache entry will be over-written. 
	 * 
	 * Resize and resize fill
	 * operations use the "orig" size as a base. It will call getImageData() 
	 * to get the "orig" size (which might inturn call renderImage again).
	 * 
	 * @param image
	 * @param imageSize
	 * @return
	 * @throws IOException
	 */
	private ImageCacheEntry renderImage (Image image, String imageSize) 
	throws IOException {
		
		// May throw IOException if cacheDir not set
		File cacheFile = getCacheFile(image.getId(), imageSize);
		
		if (log.isDebugEnabled()) {
			log.debug ("renderImage(Image#" + image.getId() + "," + imageSize + ")");
			log.debug ("renderImage(): cacheFile=" + cacheFile);
		}
		
		// Check that cache dir exists and create if necessary
		File parentDir = cacheFile.getParentFile();
		
		if ( ! parentDir.exists()) {
			log.warn ("renderImage(): creating cache directory " + parentDir.getPath());
			boolean createOk = parentDir.mkdirs();
			if (! createOk) {
				log.error ("renderImage(): mkdirs() failed");
				throw new IOException ("Cache directory does not exist at "
						+ parentDir.getPath() + ". An attempt to create it failed.");
			}
		}
		
		
		
		if ("orig".equals(imageSize)) {
			writeImageDataToCacheFile(image.getImageData(), cacheFile);
			return getImageFromCache(image.getId(), imageSize);
		} else if ("im".equals(imageSize)) {
			if (image.getIntermediateImageData() == null) {
				writeImageDataToCacheFile(image.getImageData(), cacheFile);
			} else {
				writeImageDataToCacheFile(image.getIntermediateImageData(), cacheFile);
			}
			return getImageFromCache(image.getId(), imageSize);
		} else if ("tn".equals(imageSize)) {
			writeImageDataToCacheFile(image.getThumbnailImageData(), cacheFile);
			return getImageFromCache(image.getId(), imageSize);
		} else if (imageSize.startsWith("fill")) {
			imageSize = imageSize.substring(4);
			String[] wh = imageSize.split("x");
			if (wh.length != 2) {
				throw new IOException ("invalid image size " + imageSize
						+ ", expecting fill(w)x(h), eg fill320x200");
			}
			int w = Integer.parseInt(wh[0]);
			int h = Integer.parseInt(wh[1]);
			ImageCacheEntry entry = getImageData(image.getId(), "orig");
			
			if (log.isDebugEnabled()) {
				log.debug ("renderImage(): calling ImageMagick.scaleToFill()");
			}
			
			File tmpOutFile = ImageMagick.scaleToFill(entry.getCacheFile(), w, h, "jpeg", 85);
			
			if (log.isDebugEnabled()) {
				log.debug ("renderImage(): renaming ImageMagick out file "
						+ tmpOutFile.getPath() + " to cache file " 
						+ cacheFile.getPath());
			}
			
			tmpOutFile.renameTo(cacheFile);
			
			return new ImageCacheEntry(cacheFile, image.getId(), 
					imageSize, "image/jpeg");
			
		} else {
			// {width} x {height} bounding box	
			String[] wh = imageSize.split("x");
			if (wh.length != 2) {
				throw new IOException ("invalid image size " + imageSize
						+ ", expecting (w)x(h), eg 320x200");
			}
			if (wh.length == 2) {	
				int w = Integer.parseInt(wh[0]);
				int h = Integer.parseInt(wh[1]);
				ImageCacheEntry entry = getImageData(image.getId(), "orig");
				
				if (log.isDebugEnabled()) {
					log.debug ("renderImage(): calling ImageMagick.scale()");
				}
				
				File tmpOutFile = ImageMagick.scale(entry.getCacheFile(), w, h, "jpeg", 85);
				
				if (log.isDebugEnabled()) {
					log.debug ("renderImage(): renaming ImageMagick out file "
							+ tmpOutFile.getPath() + " to cache file " 
							+ cacheFile.getPath());
				}
				
				tmpOutFile.renameTo(cacheFile);
				return new ImageCacheEntry(cacheFile, image.getId(), 
						imageSize, "image/jpeg");
			}
		}
		throw new IOException ("unrecognized image size " + imageSize);
	}
	
	
	
	/**
	 * Get the filename from a full file path. Handle both
	 * unix and window file path styles. TODO: duplicated in FileDB
	 * 
	 * @param fileItem
	 * @return
	 */
	private String extractFileName (FileItem fileItem) {

		/*
		 * File name may contain full path. We don't know if its a unix or win
		 * style path. So split on both.
		 */
		String filename = fileItem.getName();
		String[] p = filename.split("/");
		String[] q = p[p.length - 1].split("\\\\"); // Actully need four
													// backslashes here (think
													// about it!) !!
		filename = q[q.length - 1];
		
		return filename;

	}
	
	/**
	 * Create a File object for a new temporary file with unique name.
	 * 
	 * @return
	 */
	static File newTmpFile() {
		return new File(tmpDir, "_" + (++tmpFileGen) + ".tmp");
	}
	

	/**
	 * Returns approximate disk utilization of image (size of orig image + sizes
	 * of intermediate size and thumbnail stored in db). Does not include index
	 * overheads etc.
	 * 
	 * @param hsession
	 * @param image
	 * @return
	 */
	public long getImageDiskUtilization (Session hsession, Image image) {
		
		// Hoping that getId() will not cause the object to be retrieved.
		Long origId = image.getImageData().getId();
		Long imId =  image.getIntermediateImageData() == null ? null : image.getIntermediateImageData().getId();
		Long tnId = image.getThumbnailImageData().getId();
		
		long diskUtilization = 0;
		
		// Always have orig size
		diskUtilization += getImageDataSize(hsession,imageDataEntityName, origId);
		diskUtilization += getImageDataSize(hsession,imageDataEntityName, imId);
		diskUtilization += getImageDataSize(hsession,imageDataEntityName, tnId);
	
		return diskUtilization;
	}

	private static long getImageDataSize (Session hsession, String imageDataEntityName, Long imageDataId) {
		String queryStr = "select size from " + imageDataEntityName + " where id=" + imageDataId;
		try {
			Object o = hsession.createQuery(queryStr).iterate().next();
			return ((Integer)o).longValue();
		} catch (Exception e) {
			return 0;
		}
	}
	
	
}