package ie.wombat.landedestates;


import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Primary purpose is to close open Hibernate transactions/sessions.
 * 
 * @author joe
 *
 */
public class RequestListener implements ServletRequestListener  {

	private long requestInitTime;
	private static SimpleDateFormat df = new SimpleDateFormat ("yyyyMMdd HH:mm:ss");
	
	public void requestDestroyed(ServletRequestEvent arg0) {
		try {
			HttpServletRequest request = (HttpServletRequest) arg0.getServletRequest();
			String contextPath = request.getContextPath();
			
			if (HibernateUtilOld.isSessionOpen()) {
				Session hsession = HibernateUtilOld.currentSession();
				Transaction tx = hsession.getTransaction();
				tx.commit();
				HibernateUtilOld.closeSession();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		HttpServletRequest request = (HttpServletRequest) arg0.getServletRequest();
		System.err.println ("request=" + request.getRequestURI() 
				+ " @ " + df.format(Calendar.getInstance().getTime())
				+ " time=" + (System.currentTimeMillis() - requestInitTime));
		
	}

	public void requestInitialized(ServletRequestEvent arg0) {
		 //requestInitTime = System.currentTimeMillis();
	}

}
