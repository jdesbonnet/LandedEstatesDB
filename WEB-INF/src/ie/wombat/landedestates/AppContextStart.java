package ie.wombat.landedestates;


import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

/**
 * Responsible for initializing the app. contextInitialized() is called first 
 * sets up various globally accessible variables etc.
 * 
 * @author joe
 *
 */
public class AppContextStart implements ServletContextListener{

	private static final Logger log = Logger.getLogger(AppContextStart.class);
	
	public void contextDestroyed(ServletContextEvent arg0) {		
	}

	public void contextInitialized(ServletContextEvent event) {
		System.err.println ("********** LEDB STARTING **********");
		BasicConfigurator.configure();
	}

}
