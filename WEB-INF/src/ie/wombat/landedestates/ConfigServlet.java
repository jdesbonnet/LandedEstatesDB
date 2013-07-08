package ie.wombat.landedestates;

import java.io.File;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServlet;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class ConfigServlet extends HttpServlet {
	
	{
		BasicConfigurator.configure();
	}
	
	private static final Logger log = Logger.getLogger(ConfigServlet.class);
	public void init (ServletConfig config) {
		
		
		log.debug ("TEST DEBUG");
		log.info ("TEST INFO");
		log.warn ("TEST WARN");
		
		DB db = DB.getInstance();
		
		String indexDirName = config.getInitParameter("indexDir");
		if (indexDirName != null) {
			File indexDir = new File(indexDirName);
			if (! indexDir.exists()) {
				log.warn ("indexDir at " + indexDirName + " does not exist, attempting to create");
				boolean makeOk = indexDir.mkdir();
				if (! makeOk) {
					log.warn ("unable to create index dir at " + indexDirName + ", using default at "  + db.getIndexDir().getPath());
				} else {
					log.info("indexDir=" + indexDir);
					db.setIndexDir(indexDir);
				}
			} else {
				// indexDir exists, but make sure we can write to indexDir
				if (! indexDir.canWrite()) {
					log.warn ("indexDir at " + indexDirName + " is not writeable, using default at " + db.getIndexDir().getPath() );
				} else {
					log.info("indexDir=" + indexDir);
					db.setIndexDir(indexDir);
				}
			}
		}
	}
}
	