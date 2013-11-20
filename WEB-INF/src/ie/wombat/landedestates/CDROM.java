package ie.wombat.landedestates;

import ie.wombat.template.Context;
import ie.wombat.template.TemplateRegistry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;


//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.methods.GetMethod;

import org.apache.log4j.Logger;

public class CDROM {
	
	private static final Logger log = Logger.getLogger (CDROM.class);
	
	public static final String HTML_FILE_SUFFIX = ".html";
	public static final String MAP_BASE_URL="http://localhost:8080/OpenJumpServer/image?project=irelandmap&scale=500.0&w=400&h=400";
	

	private static final boolean EXPORT_DB = false;

	//private static final DecimalFormat oidf = new DecimalFormat("0000000");

	private static final SimpleDateFormat datef = new SimpleDateFormat(
			"dd MMM yyyy, HH:mm");

	private String cdromTimestamp;

	public CDROM() {
		cdromTimestamp = datef.format(Calendar.getInstance().getTime());
	}

	public void makeCdRom() throws IOException {


		File cdromDir = new File("/var/tmp/lecdrom");

		if (!cdromDir.exists()) {
			cdromDir.mkdir();
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
			File familyFile = new File(cdromDir, "f" + family.getId()
					+ HTML_FILE_SUFFIX);
			Context context = makeContext();
			context.put("title", "Family: " + family.getName());
			context.put("family", family);
			mergeToFile("/cdrom/family.vm", context, familyFile);
		}

		/*
		 * Create Estate pages
		 */
		log.info ("Creating estate pages...");
		for (Estate estate : estates) {
			File estateFile = new File(cdromDir, "e" + estate.getId()
					+ HTML_FILE_SUFFIX);
			Context context = makeContext();
			context.put("title", "Estate: " + estate.getName());
			context.put("estate", estate);
			mergeToFile("/cdrom/estate.vm", context, estateFile);
		}

		/*
		 * Create house pages
		 */
		log.info ("Creating house pages...");
		for (House house : houses) {
			Context context = makeContext();
			context.put("title", "House: " + house.getName());
			context.put("house",house);
			File houseFile = new File(cdromDir, "h" + house.getId()
					+ HTML_FILE_SUFFIX);
			mergeToFile("/cdrom/property.vm", context, houseFile);
			
			/*
			if (house.hasGridReference()) {
				File mapFile = new File (cdromDir, "map" + house.getId() + ".png");
				String imageURL = MAP_BASE_URL
				+ "&x0=" + house.getEasting()
				+ "&y0=" + house.getNorthing();
				HttpClient client = new HttpClient();
				GetMethod get = new GetMethod(imageURL);
				client.executeMethod(get);
				//FileOutputStream mapOut = new FileOutputStream (mapFile);
				//mapOut.write(get.getResponseBody());
				//mapOut.close();
			}
			*/
		}

		{
		Context context = makeContext();
		context.put("estates", estates);
		context.put("families", families);
		context.put("houses", houses);

		/*
		 * Make Family index
		 */
		File familyIndexFile = new File(cdromDir, "f_index.htm");
		mergeToFile("/cdrom/f_index.vm", context, familyIndexFile);

		/*
		 * Make Estate index
		 */
		File estateIndexFile = new File(cdromDir, "e_index.htm");
		mergeToFile("/cdrom/e_index.vm", context, estateIndexFile);

		/*
		 * Make House index
		 */
		File houseIndexFile = new File(cdromDir, "h_index.htm");
		mergeToFile("/cdrom/h_index.vm", context, houseIndexFile);

		/*
		 * Make full index
		 */
		File fullIndexFile = new File(cdromDir, "a_index.htm");
		mergeToFile("/cdrom/full_index.vm", context, fullIndexFile);

		/*
		 * Make home page
		 */
		File indexFile = new File(cdromDir, "index.htm");
		mergeToFile("/cdrom/index.vm", context, indexFile);
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

	public void makeMobile() throws IOException {


		File targetDir = new File("/var/tmp/lemobile");

		if (!targetDir.exists()) {
			targetDir.mkdir();
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
			File familyFile = new File(targetDir, "f" + family.getId()
					+ HTML_FILE_SUFFIX);
			Context context = makeContext();
			context.put("title", "Family: " + family.getName());
			context.put("family", family);
			mergeToFile("/mobile/family.vm", context, familyFile);
		}

		/*
		 * Create Estate pages
		 */
		log.info ("Creating estate pages...");
		for (Estate estate : estates) {
			File estateFile = new File(targetDir, "e" + estate.getId()
					+ HTML_FILE_SUFFIX);
			Context context = makeContext();
			context.put("title", "Estate: " + estate.getName());
			context.put("estate", estate);
			mergeToFile("/mobile/estate.vm", context, estateFile);
		}

		/*
		 * Create house pages
		 */
		log.info ("Creating house pages...");
		for (House house : houses) {
			Context context = makeContext();
			context.put("title", "House: " + house.getName());
			context.put("house",house);
			File houseFile = new File(targetDir, "h" + house.getId()
					+ HTML_FILE_SUFFIX);
			mergeToFile("/mobile/house.vm", context, houseFile);
		}

		{
		Context context = makeContext();
		context.put("estates", estates);
		context.put("families", families);
		context.put("houses", houses);

		/*
		 * Make Family index
		 */
		File familyIndexFile = new File(targetDir, "f_index.html");
		mergeToFile("/mobile/f_index.vm", context, familyIndexFile);

		/*
		 * Make Estate index
		 */
		File estateIndexFile = new File(targetDir, "e_index.html");
		mergeToFile("/mobile/e_index.vm", context, estateIndexFile);

		/*
		 * Make House index
		 */
		File houseIndexFile = new File(targetDir, "h_index.html");
		mergeToFile("/mobile/h_index.vm", context, houseIndexFile);

		/*
		 * Make full index
		 */
		File fullIndexFile = new File(targetDir, "a_index.html");
		mergeToFile("/mobile/full_index.vm", context, fullIndexFile);

		/*
		 * Make home page
		 */
		File indexFile = new File(targetDir, "index.html");
		mergeToFile("/cdrom/index.vm", context, indexFile);
		}
		
		
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
		context.put("VERSION", DB.VERSION);
		context.put("cdromTimestamp", cdromTimestamp);
		return context;
	}
}
