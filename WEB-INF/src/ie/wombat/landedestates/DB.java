package ie.wombat.landedestates;

import ie.wombat.gis.convert.OSILLAConvert;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.hibernate.EntityMode;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DB {

	//public static final String VERSION = "1.2.4 28 Sep 2009";
	//public static final String VERSION = "1.2.5b3 13 Dec 2010";
	public static final String VERSION = "1.2.7b2, 18 May 2011, Phase II beta";

	
	public static final int DEFAULT_NEARBY_RADIUS = 10000;
	private static final String DEFAULT_INDEX_DIR = "/var/tmp/leidx";
	
	private static DB instance = new DB();

	private static Logger log = Logger.getLogger(DB.class);

	// TODO: remove hard coding of index location
	private static File indexDir = new File(DEFAULT_INDEX_DIR);

	static final String[] counties = { 
		
			/* Phase 1 counties */
			"Galway", "Mayo", "Roscommon", "Leitrim", "Sligo",
			
			/* Phase 2 counties */
			 "Clare", "Cork","Kerry","Limerick","Tipperary","Waterford",
			 
			/*
			"Carlow", "Cavan", "Dublin",  "Kildare",
			"Kilkenny", "Laois", "Leitrim",  "Longford", "Louth",
			"Meath", "Monaghan", "Offaly",  
			"Westmeath", "Wexford", "Wicklow", "Antrim", "Armagh",
			"Londonderry", "Down", "Fermanagh", "Tyrone"
			*/

	};
	
	private List categoryList;

	private DB() {
	}

	public static DB getInstance() {
		return instance;
	}

	public Connection getConnection() {
		return HibernateUtil.currentSession().connection();
	}
	
	public File getIndexDir () {
		return indexDir;
	}
	public void setIndexDir (File d) {
		indexDir = d;
	}

	/**
	 * @deprecated
	 * @param session
	 * @return
	 */
	public Family[] getFamilies(Session session) {

		long startTime = System.currentTimeMillis();

		String query = "from Family order by name";
		List result = session.createQuery(query)
			.setCacheable(true)
			.list();
		
		System.err.println("getFamilies(): duration = "
				+ (System.currentTimeMillis() - startTime));

		Family[] ret = new Family[result.size()];
		result.toArray(ret);
		return ret;
	}
	
	/**
	 * Return String array of county names.
	 * @return
	 */
	public String[] getCounties() {
		return counties;
	}

	/**
	 * @deprecated
	 * @param session
	 * @param id
	 * @return
	 */
	public Family getFamily (Session session, Long id) {
		return (Family) session.load(Family.class, id);
	}

	public List<Family> getFamiliesByLetter(Session session, String letter) {
		long time = -System.currentTimeMillis();
		String query = "from Family where name like ? order by name";
		List<Family> result = session
			.createQuery(query)
			.setString(0, letter+"%")
			.setCacheable(true)
			.list();
		time += System.currentTimeMillis();
		System.err.println ("getFamiliesByLetter(): returning " 
				+ result.size() + " records (" + time + "ms)");
		return result;
	}
	
	
	public void saveFamily(Session session, Family family) {
		
		session.save(family);
		saveObjectRevision(session, family.getId(), 0, family);
	}
	

	/**
	 * @deprecated
	 * @param session
	 * @return
	 */
	public Estate[] getEstates(Session session) {

		long startTime = System.currentTimeMillis();

		
		String query = "from Estate order by name";
		log.info(query);
		List<Estate> result = session.createQuery(query)
			.setCacheable(true)
			.list();
		
		System.err.println("getEstates(): duration = "
				+ (System.currentTimeMillis() - startTime));

		Estate[] ret = new Estate[result.size()];
		result.toArray(ret);
		return ret;
	}


	public List<Estate> getEstatesByFamily(Session session, Family family) {
		String query = "from Estate as e "
			+ " inner join fetch e.families as family"
			+ " where family.id=" + family.getId();
		List<Estate> result = session.createQuery(query).list();
		System.err.println ("getEstatesByFamily(): returning " + result.size() + " records");
		return result;
	}
	
	public List getEstatesByProperty(Property property) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String query = "from Estate as e where e.houses.id=" + property.getId();
		List result = session.createQuery(query).list();
		tx.commit();
		HibernateUtil.closeSession();

		System.err.println ("getEstatesByProperty(): returning " + result.size() + " records");
		return result;
	}
	
	public List getEstatesByLetter(Session session, String letter) {
	
		String query = "from Estate as e where e.name like '"+letter + "%' order by e.name";
		List result = session.createQuery(query).list();
		System.err.println ("getEstatesByLetter(): returning " + result.size() + " records");
		return result;
	}
	
	
	
	public List getEstatesByReferenceSource (Session session, ReferenceSource refSource) {
		return getEstatesByReferenceSource(session, refSource.getId());
	}
	public List getEstatesByReferenceSource (Session session, Long refSourceId) {
		
		String query = "from Estate as e where e.references.source.id=" 
			+ refSourceId;
		List result = session.createQuery(query).list();
		
		System.err.println ("getEstatesByReferenceSource(): returning " 
				+ result.size() + " records");
		
		return result;
	}
	
	/**
	 * Returns properties within radius distance (in meters) of easting,northing.
	 * TODO: uses rectangular bounding box -- fix.
	 * @param easting
	 * @param northing
	 * @param radius Radius in meters
	 * @return
	 */
	public Property[] getPropertiesByGridReference (Session hsession, 
			int easting, int northing, int radius) {
		
		long time = -System.currentTimeMillis();
		
		int x0 = easting - radius;
		int x1 = easting + radius;
		int y0 = northing - radius;
		int y1 = northing + radius;
		String query = "from Property as p "
			+ " where "
			+ " p.gridReference != null"
			+ " and p.easting > :x0 and p.easting < :x1 "
			+ " and p.northing > :y0 and p.northing < :y1"
			+ " order by (p.easting - :xc)*(p.easting - :xc) + (p.northing - :yc) * (p.northing - :yc)"
		;
		
		List<Property> result = hsession.createQuery(query)
		.setInteger("x0",x0)
		.setInteger("x1",x1)
		.setInteger("y0",y0)
		.setInteger("y1",y1)
		.setInteger("xc",easting)
		.setInteger("yc",northing)
		.list();
	
		time += System.currentTimeMillis();
		
		log.debug ("getPropertiesByGridReference(): returning " 
				+ result.size() + " records, time " + time + "ms");
		log.debug ("query=" + query);
		Property[] ret = new Property[result.size()];
		result.toArray(ret);
		return ret;	
	}
	
	public List<Property> getHousesInBoundingBox (Session hsession, 
			double latMin, double latMax, double lonMin, double lonMax) {
		
		long time = -System.currentTimeMillis();
		
		OSILLAConvert cvt = new OSILLAConvert();
		double bottomLeft[] = cvt.lla2ng(latMin*Math.PI/180, lonMin*Math.PI/180, 0);
		double topRight[] = cvt.lla2ng(latMax*Math.PI/180, lonMax*Math.PI/180, 0);
		
		int x0 = (int)bottomLeft[0];
		int y0 = (int)bottomLeft[1];
		int x1 = (int)topRight[0];
		int y1 = (int)topRight[1];
		
		// Limit to 500km bounding box 
		if (x0 < 0) x0 = 0;
		if (x0 > 500000) x0 = 500000;
		if (y0 < 0) y0 = 0;
		if (y0 > 500000) y0 = 500000;
		if (x1 < 0) x1 = 0;
		if (x1 > 500000) x1 = 500000;
		if (y1 < 0) y1 = 0;
		if (y1 > 500000) y1 = 500000;
		
		String query = "from Property as p "
			+ " where "
			+ " p.gridReference != null"
			+ " and p.easting > :x0 and p.easting < :x1 "
			+ " and p.northing > :y0 and p.northing < :y1"
		;
		
		System.err.println ("query=" + query);
		System.err.println ("x0=" + x0 + " y0=" + y0 + " x1=" + x1 + " y1=" + y1);
		
		List<Property> result = hsession
		.createQuery(query)
		.setInteger("x0",x0)
		.setInteger("x1",x1)
		.setInteger("y0",y0)
		.setInteger("y1",y1)
		.setCacheable(true)
		.list();
	
		time += System.currentTimeMillis();
		
		return result;
	}
	/*
	public Property[] getPropertiesWithGridReference () {
		long startTime = System.currentTimeMillis();

		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String query = "from Property as p where p.gridReference!=null";
		log.info(query);
		List result = session.createQuery(query).list();
		tx.commit();
		HibernateUtil.closeSession();

		System.err.println("getEstatesWithGridReference(): duration = "
				+ (System.currentTimeMillis() - startTime));

		Property[] ret = new Property[result.size()];
		result.toArray(ret);
		return ret;
	}
	*/
	
	public Property[] getProperties (Session session) {
		
		String query = "from Property as p order by p.name ";
		List<Property> result = session.createQuery(query)
			.setCacheable(true)
			.list();
		
		Property[] ret = new Property[result.size()];
		result.toArray(ret);
		return ret;
	}
	
	public Property[] getPropertiesByLetter (Session session, String letter) {
		
		String query = "from Property as p where p.name like ? order by p.name";
		List<Property> result = session.createQuery(query)
			.setString(0,letter + "%")
			.setCacheable(true)
			.list();
		Property[] ret = new Property[result.size()];
		result.toArray(ret);
		return ret;
	}
	
	
	public List getByQuery (String query) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		//String query = "from Estate where families.id=" + family.getId();
		List result = session.createQuery(query).list();
		tx.commit();
		HibernateUtil.closeSession();

		System.err.println ("getEstatesByFamily(): returning " + result.size() + " records");
		return result;
	}

	/*
	public void saveEstate (HttpSession session, Estate estate) {
		session.getId();
		HashMap revisionHash = (HashMap)session.getAttribute("_revhash");
		Long revId = (Long)revisionHash.get(estate.getId());	
	}
	*/
	
	
	public void saveEstate(Estate estate) {
		long startTime = System.currentTimeMillis();
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();

		session.saveOrUpdate(estate);
	
		session.flush();
		saveObjectRevision(session, estate.getId(), estate.getVersion(), estate);
		tx.commit();
		HibernateUtil.closeSession();

		try {
			int id = estate.getId().intValue();
			IndexReader indexReader = IndexReader.open(indexDir);
			try {
				indexReader.delete(estate.getId().intValue());
			} catch (Exception e) {
				System.err.println ("error deleting index entry for document " + id + ": " + e);
			}
			indexReader.close();
			IndexWriter indexWriter = getIndexWriter();
			indexWriter.addDocument(makeEstateDocument(estate));
			indexWriter.close();
		} catch (IOException e) {
			System.err.println(e);
		}

		System.err.println("saveEstate(): time="
				+ (System.currentTimeMillis() - startTime) + "ms");
	}

	public void deleteEstate(Estate estate) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		session.delete(estate);
		tx.commit();
		HibernateUtil.closeSession();
	}

	public Estate getEstateRevision (Session session, Long estateId, int version) {
		
		String query = "from ObjectHistory where objectId=" 
			+ estateId
			+ " and version=" 
			+ version;
		List result = session.createQuery(query).list();
		ObjectHistory oh=null;
		if (result.size() > 0) {
			oh = (ObjectHistory)result.get(0);
		} else {
			return null;
		}
		String objectXml = oh.getObjectXML();
		
		Element el = null;
		try {
			org.dom4j.Document doc = DocumentHelper.parseText(objectXml);
			el = doc.getRootElement();
		} catch (DocumentException e) {
			System.err.println (e);
		}
		
		System.err.println ("el=" + el.asXML());
		
		// TODO: convert objectXml -> Estate object!
		Session dom4jSession = session.getSession(EntityMode.DOM4J);
		Estate estate = new Estate();
		dom4jSession.replicate(Estate.class.getName(), el, ReplicationMode.OVERWRITE);
		
		System.err.println ("estate.name=" + estate.getName());
		
		return estate;

	}

	public void saveReference(Session session, Reference reference) {
		session.save(reference);	
		saveObjectRevision(session, reference.getId(), 0, reference);
	}

	
	/**
	 * @deprecated
	 * @param reference
	 */
	public void saveReference(Reference reference) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();

		if (reference.getId() == null) {
			session.save(reference);
		} else {
			session.update(reference);
		}

		session.flush();

		saveObjectRevision(session, reference.getId(), 0, reference);

		tx.commit();
		HibernateUtil.closeSession();
	}

	public ReferenceSource[] getReferenceSources(Session session) {
		String query = "from ReferenceSource order by name";
		List result = session.createQuery(query).list();
		ReferenceSource[] ret = new ReferenceSource[result.size()];
		result.toArray(ret);
		return ret;
	}
	
	public ReferenceCategory[] getReferenceCategories(Session session) {

		if (this.categoryList == null) {
		
			String query = "from ReferenceCategory ";
			List result = session.createQuery(query).list();
			this.categoryList = result;
		}

		ReferenceCategory[] ret = new ReferenceCategory[this.categoryList
				.size()];
		this.categoryList.toArray(ret);
		return ret;

	}
	
	public ReferenceCategory getReferenceCategory(int id) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		ReferenceCategory category = (ReferenceCategory) session.load(
				ReferenceCategory.class, new Long(id));
		tx.commit();
		HibernateUtil.closeSession();
		return category;
	}
	

	public void deleteReferenceSource(Session session, ReferenceSource source) {
		
		session.delete(source);
	
	}

	public void deleteReferenceSource(Session session, Long id) {
		ReferenceSource source = (ReferenceSource) session.load(
			ReferenceSource.class, id);
		session.delete(source);
	}
	
	public void saveReferenceSource(ReferenceSource source) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();

		if (source.getId() == null) {
			session.save(source);
		} else {
			session.update(source);
		}
		tx.commit();
		HibernateUtil.closeSession();
	}

	/*
	public void removePropertyFromEstate (Long estateId, Long propertyId) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		Property property = (Property) session.load(Property.class, propertyId);
		Estate estate = (Estate) session.load(Estate.class, estateId);
		estate.getProperties().remove(property);
		tx.commit();
		HibernateUtil.closeSession();

	}
	*/

	public void removeReferenceFromEstate(int estateId, int referenceId) {
		removeReferenceFromEstate(new Long(estateId), new Long(referenceId));
	}

	public void removeReferenceFromEstate(Long estateId, Long referenceId) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		Reference reference = (Reference) session.load(Reference.class,
				referenceId);
		Estate estate = (Estate) session.load(Estate.class, estateId);
		estate.getReferences().remove(reference);
		tx.commit();
		HibernateUtil.closeSession();
	}
	
	/**
	 * Return User object corresponding to username and password, or 
	 * null if no matching username/password exists.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User verifyUsernamePassword (String username, String password) {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String query = "from User where username='" + username + "'";
		List result = session.createQuery(query).list();
		tx.commit();
		HibernateUtil.closeSession();
		
		if (result.size() == 0) {
			return null;
		}
		
		User user = (User)result.get(0);
		if (password.equals(user.getPassword())) {
			return user;
		}
		return null;
	}

	public void makeIndex(Session hsession) throws IOException {
		boolean createFlag = true;

		// Create index dir if it does not exist.
		if (!indexDir.exists()) {
			log.info("Creating index directory at " + indexDir.getPath());
			indexDir.mkdirs();
		}

		log.info("Creating index in directory " + indexDir.getPath());
		
		long t = -System.currentTimeMillis();
		
		IndexWriter writer = new IndexWriter(indexDir.getPath(),
				new StandardAnalyzer(), createFlag);
		writer.maxFieldLength = 1000000;

		
	
		
		log.info("Indexing houses...");
		
		List<Property> houses = hsession.createQuery("from Property").list();
		for (Property house : houses) {
			Document doc = makeHouseDocument(house);
			if (doc != null) {
				writer.addDocument(doc);
			}
		}
		
		/*
		List<Object[]>houses = hsession.createQuery("select id,name,description from Property").list();
		for (Object[] row : houses) {
			Long id = (Long)row[0];
			String name = (String)row[1];
			String description = (String)row[2];
			Document doc = makeHouseDocument(id, name, description);
			if (doc != null) {
				writer.addDocument(doc);
			}
		}
		*/
		
		log.info("Indexing reference sources...");
		
		List<ReferenceSource> refSources = hsession.createQuery("from ReferenceSource").list();
		for (ReferenceSource refSource : refSources) {
			Document doc = makeReferenceSourceDocument(refSource);
			if (doc != null) {
				writer.addDocument(doc);
			}
		}
		
		log.info("Indexing estates...");
		
		List<Estate> estates = hsession.createQuery("from Estate").list();
		for (Estate estate : estates) {
			Document doc = makeEstateDocument(estate);
			if (doc != null) {
				writer.addDocument(doc);
			}
		}
		
		log.info("Optimizing index...");
		writer.optimize();
		writer.close();
		
		t += System.currentTimeMillis();
		
		log.info("Time to create index: " + t + "ms");
	}

	/**
	 * Return Estate records matching query 'q'. Return empty array if none found.
	 * 
	 * @param q
	 * @return List of Estate and Property (house) objects
	 * @throws IOException
	 */
	public List<Object> search(Session hsession, String q) throws SearchException {
		IndexSearcher searcher;

		if (!indexDir.exists() || !indexDir.isDirectory()) {
			throw new SearchException ("Index directory not found at " + indexDir.getPath() 
					+ ". Probable cause: search index not created or configured.");
		}
		
		
		if (isRecordId(q)) {
			Long id = new Long(q.substring(1));
			if (q.startsWith("E")) {
				try {
					Estate estate = (Estate)hsession.get(Estate.class,id);
					estate.getName(); // make sure it's loaded
				} catch (ObjectNotFoundException e) {
					return new ArrayList<Object>(0);
				}
			}
		}
		
		try {
			searcher = new IndexSearcher(IndexReader.open(indexDir));
		} catch (FileNotFoundException e) {
			throw new SearchException(
					"Index file not found. Probable cause: search index not created or "
							+ "configured. Reindex database from tools menu. Source exception: "
							+ e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SearchException(e.toString());
		}

		
		Analyzer analyzer = new StopAnalyzer();
		QueryParser qp = new QueryParser("digest", analyzer);
		qp.setOperator(QueryParser.DEFAULT_OPERATOR_AND);

		Query query;
		try {
			query = qp.parse(q);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new SearchException("Error searching: " + e.toString());
		}

		log.info("query=" + query);
		
		try {
			Hits hits = searcher.search(query);
			
			List<Object> ret = new ArrayList<Object>();
			for (int i = 0; i < hits.length(); i++) {
				Document doc = hits.doc(i);
				String idStr = doc.get("id");
				if (idStr.startsWith("E")) {
					Long id = new Long(idStr.substring(1));
					Estate estate = (Estate)hsession.load(Estate.class,id);
					// Filter for Phase 1 only
					//if (estate.getProjectPhase() == 1) {
						ret.add (estate);
					//}
				} else if (idStr.startsWith("H")) {
					Long id = new Long(idStr.substring(1));
					Property property = (Property)hsession.load(Property.class,id);
					// Filter for Phase 1 only
					//if (property.getProjectPhase() == 1) {
						ret.add (property);
					//}
				}
			}
			return ret;
		} catch (IOException e) {
			throw new SearchException("Error searching: " + e.toString());
		}
	}

	public void exportDb(Writer w) throws IOException {
		DBExport.export(w);
	}

	private IndexWriter getIndexWriter () throws IOException {
		
		//		 Create index dir if it does not exist.
		boolean createFlag = false;
		if (!indexDir.exists()) {
			log.info("Creating index directory at " + indexDir.getPath());
			indexDir.mkdirs();
			createFlag = true;
		}

		
		//log.info("Creating index in directory " + indexDir.getPath());
		IndexWriter indexWriter = new IndexWriter(indexDir.getPath(),
				new StandardAnalyzer(), createFlag);
		indexWriter.maxFieldLength = 100000;	
		return indexWriter;
	}
	
	/**
	 * Create a Lucene Document object from an Estate object.
	 * 
	 * @param estate
	 * @return
	 */
	private static Document makeEstateDocument(Estate estate) {
		
		if (estate.getName() == null) {
			log.error ("Estate#" + estate.getId() + " name is null");
			return null;
		}
		
		Document doc = new Document();
		doc.add(Field.UnIndexed("id", estate.getLuceneId()));
		
		/*
		for (Family family : estate.getFamilies()) {
			buf.append (family.getName() + " ");
		}
		
		for (Property prop : estate.getProperties()) {
			buf.append (prop.getName());
			buf.append (" ");
			buf.append (prop.getDescription());
			buf.append (" ");
			buf.append (prop.getTownland());
			buf.append (" ");
			buf.append (prop.getCounty());
			buf.append (" ");	
		}
		*/
		
		/*
		for (Reference reference : estate.getReferences()) {
			if (reference.getSource() != null) {
				buf.append (reference.getSource().getName() + " ");
			}
			buf.append (reference.getDescription());
			buf.append (" ");
		}
		*/
		
		doc.add(Field.Text("name", estate.getName()));
		if (estate.getDescription() != null) {
			doc.add(Field.Text("description", estate.getDescription()));
		}
		String digest = estate.getName() + " " + estate.getDescription();
		doc.add(Field.Text("digest", digest));
		return doc;
	}

	private static Document makeHouseDocument(Property house) {
		
		if (house.getName() == null) {
			log.error ("House#" + house.getId() + " name is null");
			return null;
		}
		
		Document doc = new Document();
		doc.add(Field.UnIndexed("id", house.getLuceneId()));
		doc.add(Field.Text("name", house.getName()));
		if (house.getDescription() != null) {
			doc.add(Field.Text("description", house.getDescription()));
		}
		
		String digest = house.getName() + " " + house.getDescription(); 
		doc.add(Field.Text("digest", digest));
		
		return doc;
	}
	
	private static Document makeReferenceSourceDocument(ReferenceSource refSource) {
		
		if (refSource.getName() == null) {
			log.error ("ReferenceSource#" + refSource.getId() + " name is null");
			return null;
		}
		Document doc = new Document();
		doc.add(Field.UnIndexed("id", refSource.getLuceneId()));
		doc.add(Field.Text("name", refSource.getName()));
		if (refSource.getDescription() != null) {
			doc.add(Field.Text("description", refSource.getDescription()));
		}
		String digest = refSource.getName() + " " + refSource.getDescription(); 
		doc.add(Field.Text("digest", digest));
		
		return doc;
	}

	/*
	 * 
	 */
	public ObjectHistory[] getRecentChanges() {
		long startTime = System.currentTimeMillis();

		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String query = "from ObjectHistory order by modified desc";
		List result = session.createQuery(query).list();
		tx.commit();
		HibernateUtil.closeSession();

		System.err.println("getRecentChanges(): duration = "
				+ (System.currentTimeMillis() - startTime));

		ObjectHistory[] ret = new ObjectHistory[result.size()];
		result.toArray(ret);
		return ret;
	}

	public ObjectHistory[] getRevisionHistory(String className, Long id) {
		long startTime = System.currentTimeMillis();

		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		String query = "from ObjectHistory "
			+ "where objectClass='" + className + "' "
			+ " and objectId=" + id + " order by modified desc";
		List result = session.createQuery(query).list();
		tx.commit();
		HibernateUtil.closeSession();

		System.err.println("getRevisionHistory(): duration = "
				+ (System.currentTimeMillis() - startTime));

		ObjectHistory[] ret = new ObjectHistory[result.size()];
		result.toArray(ret);
		return ret;
	}
	/*
	 * Save object in audit trail
	 */
	private static void saveObjectRevision(Session session, Long id, int version, Object o) {

		String query = "from ObjectHistory where objectId=" + id 
		+ " and version=" + version;
		List result = session.createQuery(query).list();
		System.err.println ("saveObjectRevision(): query=" + query);
		System.err.println ("saveObjectRevision(): query returned " + result.size() + " objects");
		ObjectHistory oh;
		if (result.size() > 0) {
			oh = (ObjectHistory)result.get(0);
		} else {
			System.err.println ("creating new ObjectHistory object");
			oh = new ObjectHistory();
			oh.setObjectId(id);
			oh.setObjectClass(o.getClass().getName());
			oh.setVersion(version);
		}
		oh.setModified(new Date(System.currentTimeMillis()));
		
		Session dom4jSession = session.getSession(EntityMode.DOM4J);
		Element e = (Element) dom4jSession.load(o.getClass(), id);	
		oh.setObjectXML(e.asXML());
		session.saveOrUpdate(oh);
		
		
		// TODO: experimantal
		System.err.print ("object XML=");
		XMLEncoder xe = new XMLEncoder(System.err);
		xe.writeObject(o);
		xe.flush();
		System.err.println ("");

	}
	
	private boolean isRecordId (String s) {
		if (s==null) {
			return false;
		}
		if (s.length()<2) {
			return false;
		}
		if (! Character.isLetter(s.charAt(0))) {
			return false;
		}
		for (int i = 1; i < s.length(); i++) {
			if (! Character.isDigit(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	public static Long getIdFromAutoCompleteField(String acf) {

		if (acf == null) {
			return null;
		}
		String[] p = acf.split(" ");
		String lastp = p[p.length - 1];
		if (lastp.startsWith("[") && lastp.endsWith("]")) {
			try {
				Long id = new Long(lastp.substring(1, lastp.length() - 1));
				return id;
			} catch (Exception e) {
				// ignore
			}
		}
		return null;

	}
	
}
