package ie.wombat.landedestates.staticsite;

import ie.wombat.imagetable.Image;
import ie.wombat.landedestates.DB;
import ie.wombat.landedestates.Estate;
import ie.wombat.landedestates.Family;
import ie.wombat.landedestates.HibernateUtil;
import ie.wombat.landedestates.House;
import ie.wombat.template.Context;
import ie.wombat.template.TemplateRegistry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;


//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.GetMethod;

import org.apache.lucene.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StaticSite {
	
	private static final Logger log = LoggerFactory.getLogger (StaticSite.class);
	
	public static final String FILE_SUFFIX = ".md";
	

	private static final boolean EXPORT_DB = false;

	//private static final DecimalFormat oidf = new DecimalFormat("0000000");

	private static final SimpleDateFormat datef = new SimpleDateFormat(
			"dd MMM yyyy, HH:mm");

	private String buildTimestamp;

	public StaticSite() {
		buildTimestamp = datef.format(new Date());
	}

	public void makeStaticSite() throws IOException {


		File staticRootDir = new File("/var/tmp/lestatic");

		if (!staticRootDir.exists()) {
			staticRootDir.mkdir();
		}

				
		// Create a transaction
		EntityManager em = HibernateUtil.getEntityManager();
				
		log.info ("Query families");
		List<Family> families = em.createQuery("from Family").getResultList();
		
		log.info ("Query estates");
		List<Estate> estates = em.createQuery("from Estate").getResultList();
		
		log.info ("Query houses");
		List<House> houses = em.createQuery("from House").getResultList();
		
		
		//Context context = makeContext();
		
		/*
		 * Create Family pages
		 */
		log.info ("Creating family pages...");
		for (Family family : families) {
			File familyFile = new File(staticRootDir, "f" + family.getId()
					+ FILE_SUFFIX);
			Context context = makeContext();
			context.put("title", "Family: " + family.getName());
			context.put("family", family);
			mergeToFile("/static/md/family.vm", context, familyFile);
		}

		/*
		 * Create Estate pages
		 */
		log.info ("Creating estate pages...");
		for (Estate estate : estates) {
			File estateFile = new File(staticRootDir, "e" + estate.getId()
					+ FILE_SUFFIX);
			Context context = makeContext();
			context.put("title", "Estate: " + estate.getName());
			context.put("estate", estate);
			mergeToFile("/static/md/estate.vm", context, estateFile);
		}

		/*
		 * Create house pages
		 */
		log.info ("Creating house pages...");
		for (House house : houses) {
			Context context = makeContext();
			context.put("title", "House: " + house.getName());
			context.put("house",house);
			File houseFile = new File(staticRootDir, "h" + house.getId()
					+ FILE_SUFFIX);
			mergeToFile("/static/md/house.vm", context, houseFile);
			
		}

		log.info ("Creating images");
		List<Image> images = em.createQuery("from Image").getResultList();
		for (Image image : images) {
			String suffix = image.getFilenameSuffix();
			File imageFile = new File(staticRootDir, "i" + image.getId()
			 + suffix);
			FileOutputStream fout = new FileOutputStream(imageFile);
			fout.write(image.getImageData().getData());
			fout.close();
		}
		
		{
		Context context = makeContext();
		context.put("estates", estates);
		context.put("families", families);
		context.put("houses", houses);

		/*
		 * Make Family index
		 */
		File familyIndexFile = new File(staticRootDir, "f_index.md");
		mergeToFile("/static/md/f_index.vm", context, familyIndexFile);

		/*
		 * Make Estate index
		 */
		File estateIndexFile = new File(staticRootDir, "e_index.md");
		mergeToFile("/static/md/e_index.vm", context, estateIndexFile);

		/*
		 * Make House index
		 */
		File houseIndexFile = new File(staticRootDir, "h_index.md");
		mergeToFile("/static/md/h_index.vm", context, houseIndexFile);

		/*
		 * Make full index
		 */
		File fullIndexFile = new File(staticRootDir, "a_index.md");
		mergeToFile("/static/md/full_index.vm", context, fullIndexFile);

		/*
		 * Make home page
		 */
		File indexFile = new File(staticRootDir, "index.md");
		mergeToFile("/static/md/index.vm", context, indexFile);
		}
		
		/*
		 * Dump database as XML
		 */
		/*
		if (EXPORT_DB) {
			File dbExportFile = new File(cdromDir, "export.xml");
			FileWriter fr = new FileWriter(dbExportFile);
			db.exportDb(fr);
			fr.close();

		}
		*/

		/*
		 * Write Windows autorun.inf
		 */
		//File autoRunFile = new File(cdromDir, "autorun.inf");
		//FileWriter fr = new FileWriter(autoRunFile);
		//fr.write("[AUTORUN]\nShellExecute=index.htm\n");
		//fr.close();
				
		
	}


	
	private void mergeToFile(String template, Context context, File file)
			throws IOException {
		TemplateRegistry templates = TemplateRegistry.getInstance();
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(templates.mergeToString(template, context));
			fw.close();
		} catch (Exception e) {
			throw new IOException(e.toString());
		}
	}

	private Context makeContext() {
		Context context = new Context();
		DB.getInstance().initTemplateContext(context);
		context.put("buildTimestamp", buildTimestamp);
		return context;
	}
}
