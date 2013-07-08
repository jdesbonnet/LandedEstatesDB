package ie.wombat.landedestates;

import java.beans.XMLEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DBExport {

	private static final String MYSQLDUMP = "mysqldump --host=cms.galway.net --user=leuser --password=xyz123 landedestates";
	
	public static void mysqlDump (OutputStream out) throws IOException {
	
		Process dumpProcess = Runtime.getRuntime().exec(MYSQLDUMP);
		InputStream pin = dumpProcess.getInputStream();
		byte[] buf = new byte[4096];
		int nbytes;
		while ( (nbytes = pin.read(buf)) >=0 ) {
			out.write(buf,0,nbytes);
		}
		pin.close();
		out.flush();
	}
	public static void export (Writer w) throws IOException {
		
		Document doc = DocumentHelper.createDocument();
		Session session = HibernateUtil.currentSession();
		Session dom4jSession = session.getSession(EntityMode.DOM4J);
		Transaction tx = session.beginTransaction();
		String query = "from Estate";
		List results = dom4jSession
		    .createQuery(query)
		    .list();
		
		Element exportElement = doc.addElement("export");
		
		for (int i=0; i<results.size(); i++ ) {
		    Element estate = (Element) results.get(i);
		    exportElement.add(estate);
		}

		tx.commit();
		session.close();
		
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(w, format );
        writer.write( doc );
        
	}
	
	public static void export2 (OutputStream out) throws IOException{
		
		long time = -System.currentTimeMillis();
		
		XMLEncoder e = new XMLEncoder(out);
	
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		writeQuery (session, e, "from Family");
		writeQuery (session, e, "from Estate");
		writeQuery (session, e, "from Property");
		writeQuery (session, e, "from Reference");
		writeQuery (session, e, "from ReferenceSource");
	
		e.close();
		
		tx.commit();
		HibernateUtil.closeSession();
		
		time += System.currentTimeMillis();
		
		System.err.println ("time to export: " + time + "ms");
	}
	
	private static void writeQuery (Session session, XMLEncoder encoder, String query) {
		Iterator iter = session.createQuery(query).list().iterator();
		while (iter.hasNext()) {
			encoder.writeObject(iter.next());
		}
	}
	public static String getEstateAsXML (long id) {
	
		Session session = HibernateUtil.currentSession();
		Session dom4jSession = session.getSession(EntityMode.DOM4J);
		Transaction tx = dom4jSession.beginTransaction();
		Element e = (Element)dom4jSession.load(Estate.class, new Long(id));
		tx.commit();
		HibernateUtil.closeSession();	
		return e.asXML();
	}
}
