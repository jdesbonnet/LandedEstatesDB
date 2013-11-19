package ie.wombat.landedestates;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {
	
	private static Logger log = Logger.getLogger(Configuration.class);
	
	private static File confFile;
	private static long confLoadedTime=0L;
	private static Properties configuration;
	
	public static void init (File appRoot) {
		File webInfDir = new File(appRoot,"WEB-INF");
		confFile = new File(webInfDir,"config.properties");
		loadConfigurationProperties();
	}
	
	public static void loadConfigurationProperties () {
		System.err.println ("Loading configuration...");

		Properties properties = new Properties() ;
		try {
			properties.load(new FileReader(confFile));
		} catch (Exception e1) {
			log.error ("Unable to load config.properties at " + confFile.getPath());
		}
		configuration = properties;
		confLoadedTime = System.currentTimeMillis();
	}
	
	public static String getConfiguration (String key) {
		
		// If configuration file has been edited, reload
		if (confFile.lastModified() > confLoadedTime) {
			loadConfigurationProperties();
		}
		// TODO: should values be trim() before returning?
		return (String)configuration.get(key);
	}
	
	
	public static String getConfiguration (String key, String defaultValue) {
		String value = getConfiguration(key);
		return value == null ? defaultValue : value;
	}
}
