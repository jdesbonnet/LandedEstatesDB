package ie.wombat.landedestates;


import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ie.wombat.template.TemplateException;
import ie.wombat.template.TemplateRegistry;

/**
 * Responsible for initializing the app. contextInitialized() is called first 
 * sets up various globally accessible variables etc.
 * 
 * @author joe
 *
 */
public class AppContextStart implements ServletContextListener{

	private static final Logger log = LoggerFactory.getLogger(AppContextStart.class);
	
	public void contextDestroyed(ServletContextEvent arg0) {		
	}

	public void contextInitialized(ServletContextEvent event) {
		System.err.println ("********** LEDB STARTING **********");
		
		File appRoot = new File(event.getServletContext().getRealPath("/"));
		Configuration.init(appRoot);
		
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
