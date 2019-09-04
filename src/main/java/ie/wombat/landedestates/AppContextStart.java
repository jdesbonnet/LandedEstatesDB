package ie.wombat.landedestates;


import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.wombat.template.TemplateException;
import ie.wombat.template.TemplateRegistry;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Responsible for initializing the app. contextInitialized() is called first 
 * sets up various globally accessible variables etc.
 * 
 * @author joe
 *
 */
public class AppContextStart implements ServletContextListener{

	private static final Logger log = LoggerFactory.getLogger(AppContextStart.class);
	
        @Override
	public void contextDestroyed(ServletContextEvent arg0) {		
	}

        @Override
	public void contextInitialized(ServletContextEvent event) {
		System.err.println ("*** LEDB STARTING context=" 
				+ event.getServletContext().getContextPath());
		
		File appRoot = new File(event.getServletContext().getRealPath("/"));
		Configuration.init(appRoot);

                // Retrieve software version from pom.xml (also written in pom.properties)
		File pomPropertiesFile = new File (appRoot, "META-INF/maven/ie.wombat.landedestates/ledb/pom.properties");
		Properties pomProperties = new Properties ();
		try {
			pomProperties.load(new FileInputStream(pomPropertiesFile));
		} catch (IOException e) {
                    log.error("could not read properties file " + pomPropertiesFile.getPath() + ": " + e.toString());
		}	
		String version = pomProperties.getProperty("version");
                log.info("version=" + version);
                
		File  templateRoot = new File(appRoot,"WEB-INF/classes/templates");
		try {
			TemplateRegistry.getInstance().init(templateRoot.getPath());
		} catch (TemplateException e) {
			System.err.println("error initializing templates at " 
					+ templateRoot.getPath());
			e.printStackTrace();
		}
		
	}

}
