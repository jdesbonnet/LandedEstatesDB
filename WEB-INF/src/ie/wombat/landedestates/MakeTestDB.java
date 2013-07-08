package ie.wombat.landedestates;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class MakeTestDB {

	private static final int NFAMILY = 100;
	private static final int NESTATE = 1000;
	
	public static void main (String[] arg) {
		MakeTestDB test = new MakeTestDB();
		test.makeTestDb();
	}
	
	public void makeTestDb () {
		
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		DB db = DB.getInstance();
		
		/*
		 * Make family records
		 */
		for (int i = 0; i < NFAMILY; i++) {
			String familyName = "Test Family " + i;
			Family family = new Family();
			family.setName(familyName);
			session.save(family);
		}
		
		Family[] families = db.getFamilies(session);
		
		/*
		 * Make estate records
		 */
		int j,k;
		for (int i = 0; i < NESTATE; i++) {
			String estateName = "Test Estate " + i;
			Estate estate = new Estate();
			estate.setName(estateName);
			for (j = 0; j < 5; j++) {
				k = (int)(Math.random() * (double)NFAMILY);
				estate.getFamilies().add(families[k]);
			}
			session.save(estate);
		}
		
		tx.commit();
		HibernateUtil.closeSession();
		
	}
}
