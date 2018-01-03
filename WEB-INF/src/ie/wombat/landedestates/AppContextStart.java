package ie.wombat.landedestates;


import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.apache.velocity.app.Velocity;

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
		
		File appRoot = new File(event.getServletContext().getRealPath("/"));
		Configuration.init(appRoot);
		
		/*
		 try
		 {
		     Velocity.setProperty(Velocity.ENCODING_DEFAULT,"UTF-8");
		     Velocity.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.NullLogSystem");
		     // Allow relative paths in VM include/parse directives
		     Velocity.setProperty("eventhandler.include.class", "org.apache.velocity.app.event.implement.IncludeRelativePath");
		     Velocity.init();
		 }
		 catch (Exception e)
		 {
		     e.printStackTrace();
		 }
		 */
	}

}
