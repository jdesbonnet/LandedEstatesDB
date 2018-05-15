package ie.wombat.landedestates;


import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implements the cleanup of the "Open Session in View" pattern.
 * 
 * @author joe
 *
 */
public class EndOfRequestCleanup implements ServletRequestListener {

	private static Logger log = LoggerFactory.getLogger(EndOfRequestCleanup.class);
	
	private static SimpleDateFormat df = new SimpleDateFormat ("yyyyMMdd HH:mm:ss");

	
	public void requestDestroyed(ServletRequestEvent arg0) {
		try {
			
			HttpServletRequest request = (HttpServletRequest) arg0.getServletRequest();

			Date now = new Date (System.currentTimeMillis());
			
			StringBuffer buf = new StringBuffer();
			buf.append ("EOR ");
			buf.append (arg0.hashCode());
			buf.append (" uri=");
			buf.append (request.getRequestURI());
			buf.append (" ts=" + df.format(now));
			buf.append (" queryString=" + request.getQueryString());
			buf.append (" remoteHost=" + request.getRemoteHost());
			buf.append (" referer=" + request.getHeader("Referer"));
			buf.append (" userAgent=" + request.getHeader("User-Agent"));
			
			HttpSession session = request.getSession(false);
			buf.append (" userId=" + (session == null ? "null" : session.getAttribute("userId")));
			
			//buf.append (" dFreeMem=" + (deltaMem/1024));
			buf.append ("KB  mem=");
			buf.append (Runtime.getRuntime().freeMemory()/1024); 
			buf.append ("KB / ");
			buf.append (Runtime.getRuntime().totalMemory()/1024);
			buf.append ("KB");
			
			log.debug (buf.toString());
	
			EntityManager em = HibernateUtil.getEntityManager();
			
			if (em.getTransaction().isActive()) {
				log.warn("Found active transation -- commiting the transaction");
				em.getTransaction().commit();
			}
			
			if (em.isOpen()) {
				log.debug("Closing EntityManager " + em);
				em.close();
			}
			
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void requestInitialized(ServletRequestEvent arg0) {
		// Nothing to do.
	}

}
