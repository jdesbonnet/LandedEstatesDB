package ie.wombat.landedestates;

import java.io.StringWriter;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

public class AuditUtil {

	private static Logger log = Logger.getLogger(AuditUtil.class);
	
	public static void writeAuditRecord (EntityManager em, User user, Auditable a) throws JAXBException {
			
		// Get XML for object
		JAXBContext jaxbContext = JAXBContext.newInstance(EmployeeRecord.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		StringWriter w = new StringWriter();
		marshaller.marshal(a, w);
		String xml = w.toString();
		
		AuditRecord auditRecord = new AuditRecord();
		auditRecord.setClassName(a.getClass().getName());
		auditRecord.setEntityId(a.getId());
		auditRecord.setUser(user);
		auditRecord.setEntityXml(xml);
		
		em.persist(auditRecord);
		
	}
}
