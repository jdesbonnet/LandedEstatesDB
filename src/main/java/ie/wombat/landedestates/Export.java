package ie.wombat.landedestates;

import ie.wombat.imagetable.Image;
import ie.wombat.imagetable.ImageData;
import ie.wombat.template.Context;
import ie.wombat.template.TemplateRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Export select records or entire database to JSON files.
 * 
 * @author joe
 *
 */

public class Export {
	
	private static final Logger log = LoggerFactory.getLogger (Export.class);
	
	private File exportRootDir;
	
	// Use pretty (nicely formatted) JSON output. Helps with line level diffs.
	private Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.setExclusionStrategies(new ExportExclusionStrategy())
			.create();
	
	/**
	 * Export on record (House, Estate, Family)
	 * @param obj
	 * @throws IOException
	 */
	public void exportEntityRecord (LEDBEntity obj) throws IOException {
		
		String className = obj.getClass().getSimpleName();
		
		// One directory for each entity
		File dir = new File(exportRootDir,className);
		if ( ! dir.exists()) {
			dir.mkdir();
		}
		
		File jsonFile = new File(dir, className + "_" + obj.getId() + ".json");
		
		log.info("writing file " + jsonFile.getPath());
		
		Writer w = new FileWriter (jsonFile);
		w.write(gson.toJson(obj));
		w.close();
		
	}
	
	/**
	 * Export image. Writes original image binary and also image metadata in JSON.
	 * 
	 * @param image
	 * @throws IOException
	 */
	public void exportImage(Image image) throws IOException {
		File imageDir = new File (exportRootDir,"images");
		
		if (! imageDir.exists()) {
			imageDir.mkdir();
		}
		
		String suffix = image.getFilenameSuffix();
		if (suffix == null) {
			log.error("Image#" + image.getId() + ": null suffix / unknown image type");
			return;
		}
	
		File imageJsonFile = new File(imageDir, "image_" + image.getId() + ".json");
		log.info("writing image metadata file " + imageJsonFile.getPath());
		Writer w = new FileWriter(imageJsonFile);
		w.write(gson.toJson(image));
		w.close();
		
		ImageData imageData = image.getImageData();
		if (imageData == null) {
			log.error("Image#" + image.getId() + ": null imageData");
			return;
		}
		File imageFile = new File(imageDir, "image_" + image.getId() + image.getFilenameSuffix());
		log.info("writing image file " + imageFile.getPath());
		FileOutputStream os = new FileOutputStream (imageFile);
		os.write(image.getImageData().getData());
		os.close();
	

		
	}

	@SuppressWarnings("unchecked")
	public void exportAll(EntityManager em) throws IOException {


		log.info ("Query families");
		List<Family> families = em.createQuery("from Family").getResultList();
		for (Family family : families) {
			exportEntityRecord(family);
		}
		
		log.info ("Query estates");
		List<Estate> estates = em.createQuery("from Estate").getResultList();
		for (Estate estate : estates) {
			exportEntityRecord(estate);
		}
		
		log.info ("Query houses");
		List<House> houses = em.createQuery("from House").getResultList();
		for (House house : houses) {
			exportEntityRecord(house);
		}
		
		
		log.info ("ReferenceSources");
		List<ReferenceSource> referenceSources = em.createQuery("from ReferenceSource").getResultList();
		for (ReferenceSource referenceSource : referenceSources) {
			exportEntityRecord(referenceSource);
		}
		
		log.info ("Query images");
		List<Image> images = em.createQuery("from Image").getResultList();
		for (Image image : images) {
			exportImage(image);
		}
	}
	
	/**
	 * Set export root directory (create it if it does not exist).
	 * @param exportRootDir
	 */
	public void setExportRoot(File exportRootDir) {
		this.exportRootDir = exportRootDir;
		
		if ( ! exportRootDir.exists()) {
			exportRootDir.mkdir();
		}
	}
	
	
}
