package ie.wombat.landedestates;

import ie.wombat.gis.convert.OSILLAConvert;
import ie.wombat.template.Context;
import ie.wombat.ui.Tab;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;



import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;

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
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DB {

	//public static final String VERSION = "1.2.4 28 Sep 2009";
	//public static final String VERSION = "1.2.5b3 13 Dec 2010";
	//public static final String VERSION = "1.3.2, July 2013";
	public static final String VERSION = "2.0.0 alpha 1, Jan 2018";

	
	public static final int DEFAULT_NEARBY_RADIUS = 10000;
	private static final String DEFAULT_INDEX_DIR = "/var/tmp/leidx";
	
	private static DB instance = new DB();

	private static Logger log = LoggerFactory.getLogger(DB.class);

	// TODO: remove hard coding of index location
	private static File indexDir = new File(DEFAULT_INDEX_DIR);
	
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

	// Earth radius  in m
	private static final double earthRadius = 6378100;

	private static Double degPerMLon =  new Double (180 /  (Math.PI * earthRadius * Math.cos(53.5 * Math.PI/180)));
	// degrees latitude per meter (near Ireland)
	private static Double degPerMLat =  new Double (180 /  (Math.PI * earthRadius));
	
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

	private static Tab[] familiesSubTabs = {
			new Tab ("az", "A-Z", "family-list.jsp"),
			new Tab ("listall", "List all", "family-list.jsp?letter=_all"),
			/*
			new Tab ("a","A", "families.jsp?letter=A"),
			new Tab ("b","B", "families.jsp?letter=B"),
			new Tab ("c","C", "families.jsp?letter=C"),
			new Tab ("d","D", "families.jsp?letter=D"),
			new Tab ("e","E", "families.jsp?letter=E"),
			new Tab ("f","F", "families.jsp?letter=F"),
			new Tab ("g","G", "families.jsp?letter=G"),
			new Tab ("h","H", "families.jsp?letter=H"),
			new Tab ("i","I", "families.jsp?letter=I"),
			new Tab ("j","J", "families.jsp?letter=J"),
			new Tab ("k","K", "families.jsp?letter=K"),
			new Tab ("l","L", "families.jsp?letter=L"),
			new Tab ("m","M", "families.jsp?letter=M"),
			new Tab ("n","N", "families.jsp?letter=N"),
			new Tab ("o","O", "families.jsp?letter=O"),
			new Tab ("p","P", "families.jsp?letter=P"),
			new Tab ("q","Q", "families.jsp?letter=Q"),
			new Tab ("r","R", "families.jsp?letter=R"),
			new Tab ("s","S", "families.jsp?letter=S"),
			new Tab ("t","T", "families.jsp?letter=T"),
			new Tab ("u","U", "families.jsp?letter=U"),
			new Tab ("v","V", "families.jsp?letter=V"),
			new Tab ("w","W", "families.jsp?letter=W"),
			new Tab ("x","XYZ", "families.jsp?letter=XYZ"),
			*/
			new Tab ("newfamily","New family", "family-edit.jsp")
	};
	
	private static Tab[] estatesSubTabs = { new Tab("az", "A-Z", "estate-list.jsp"),
			new Tab("listall", "List all", "estate-list.jsp?letter=_all"),
			new Tab("newestate", "New estate", "estate-new.jsp"), };

	private static Tab[] housesSubTabs = { new Tab("az", "A-Z", "house-list.jsp"),
			new Tab("listall", "List all", "house-list.jsp?letter=_all") };

	private static Tab[] employeeRecordsSubTabs = {
			// new Tab ("new", "New record",
			// "employee-record-add.jsp?letter=_all"),
			new Tab("listall", "List all employee records", "employee-record-list.jsp?letter=_all"),
			new Tab("listtags", "List tags", "employee-record-tag-list.jsp"),
			new Tab("export", "Export employee records", "employee-record-export.jsp"),
			new Tab("help", "Help", "help.jsp?topic=EmployeeRecord&tab=employeeRecords") };

	private static Tab[] refSourcesSubTabs = { new Tab("newrefsource", "New reference source", "refsource-new.jsp") };

	private static Tab[] tabs = { 
			new Tab("home","Map","index.jsp"),
			new Tab("families","Families","family-list.jsp", familiesSubTabs), 
			new Tab ("estates","Estates","estate-list.jsp",estatesSubTabs),
			new Tab ("houses","Houses","house-list.jsp",housesSubTabs	),
			new Tab ("employeeRecords","Employee Records","employee-record-list.jsp",employeeRecordsSubTabs	),
			new Tab ("refsources","Reference Sources","refsource-list.jsp", refSourcesSubTabs),
			new Tab ("images", "Images", "images.jsp"),
			new Tab ("tools","Tools","tools.jsp")
		};
	
	private List categoryList;

	private DB() {
	}

	public static DB getInstance() {
		return instance;
	}

	public EntityManager getConnection() {
		return HibernateUtil.getEntityManager();
	}
	
	public File getIndexDir () {
		return indexDir;
	}
	public void setIndexDir (File d) {
		indexDir = d;
	}

	
	/**
	 * Return String array of county names.
	 * @return
	 */
	public String[] getCounties() {
		return counties;
	}

	
	public List<Family> getFamiliesByLetter(EntityManager em, String letter) {
		long time = -System.currentTimeMillis();
		String query = "from Family where name like ? order by name";
		List<Family> result = em
			.createQuery(query)
			.setParameter(0, letter+"%")
			//.setCacheable(true)
			.getResultList();
		time += System.currentTimeMillis();
		System.err.println ("getFamiliesByLetter(): returning " 
				+ result.size() + " records (" + time + "ms)");
		return result;
	}
	



	public List<Estate> getEstatesByFamily(EntityManager em, Family family) {
		String query = "from Estate as e "
			+ " inner join fetch e.families as family"
			+ " where family.id=" + family.getId();
		List<Estate> result = em.createQuery(query).getResultList();
		System.err.println ("getEstatesByFamily(): returning " + result.size() + " records");
		return result;
	}
	
	public List<Estate> getEstatesByHouse(EntityManager em, House house) {
		String query = "from Estate as e where e.houses.id=" + house.getId();
		List<Estate> result =  em.createQuery(query).getResultList();
		return result;
	}
	
	public List<Estate> getEstatesByLetter(EntityManager em, String letter) {
	
		String query = "from Estate as e where e.name like '"+letter + "%' order by e.name";
		List<Estate> result = em.createQuery(query).getResultList();
		System.err.println ("getEstatesByLetter(): returning " + result.size() + " records");
		return result;
	}
	
	
	
	public List<Estate> getEstatesByReferenceSource (EntityManager em, ReferenceSource refSource) {
		return getEstatesByReferenceSource(em, refSource.getId());
	}
	public List<Estate> getEstatesByReferenceSource (EntityManager em, Long refSourceId) {
		
		String query = "from Estate as e where e.references.source.id=" 
			+ refSourceId;
		List<Estate> result = em.createQuery(query).getResultList();
		
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
	public List<House> getHousesByGridReference (EntityManager em, 
			int easting, int northing, int radius) {
		
		long time = -System.currentTimeMillis();
		
		int x0 = easting - radius;
		int x1 = easting + radius;
		int y0 = northing - radius;
		int y1 = northing + radius;
		
		String query = "from House as h "
			+ " where "
			+ " h.gridReference != null"
			+ " and h.easting > :x0 and h.easting < :x1 "
			+ " and h.northing > :y0 and h.northing < :y1"
			+ " order by (h.easting - :xc)*(h.easting - :xc) + (h.northing - :yc) * (h.northing - :yc)"
		;
		
		List<House> result = em.createQuery(query)
		.setParameter("x0",x0)
		.setParameter("x1",x1)
		.setParameter("y0",y0)
		.setParameter("y1",y1)
		.setParameter("xc",easting)
		.setParameter("yc",northing)
		.getResultList();
	
		time += System.currentTimeMillis();
		
		log.debug ("getPropertiesByGridReference(): returning " 
				+ result.size() + " records, time " + time + "ms");
		log.debug ("query=" + query);
		return result;
	}
	
	public List<House> getHousesInBoundingBox (EntityManager em, 
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
		
		String query = "from House as h "
			+ " where "
			+ " h.gridReference != null"
			+ " and h.easting > :x0 and h.easting < :x1 "
			+ " and h.northing > :y0 and h.northing < :y1"
		;
		
		System.err.println ("query=" + query);
		System.err.println ("x0=" + x0 + " y0=" + y0 + " x1=" + x1 + " y1=" + y1);
		
		List<House> result = em
		.createQuery(query)
		.setParameter("x0",x0)
		.setParameter("x1",x1)
		.setParameter("y0",y0)
		.setParameter("y1",y1)
		.getResultList();
	
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
	
	public List<House> getProperties (EntityManager em) {
		
		String query = "from House as h order by h.name ";
		List<House> result = em.createQuery(query).getResultList();
		return result;
	}
	
	public List<House> getPropertiesByLetter (EntityManager em, String letter) {
		
		String query = "from House as h where h.name like ? order by h.name";
		List<House> result = em.createQuery(query)
			.setParameter(0,letter + "%")
			.getResultList();
		return result;
		
	}
	
	
	public Estate getEstateRevision (EntityManager em, Long estateId, int version) {
		/*
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
		*/
		return null;

	}

	/**
	 * Return User object corresponding to username and password, or 
	 * null if no matching username/password exists.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public User verifyUsernamePassword (EntityManager em, String username, String password) {
		
		String query = "from User where username='" + username + "'";
		List<User> result = em.createQuery(query).getResultList();
	
		if (result.size() == 0) {
			return null;
		}
		
		User user = (User)result.get(0);
		if (password.equals(user.getPassword())) {
			return user;
		}
		return null;
	}

	/**
	 * Reindexes entire database.
	 * 
	 * @param em
	 * @throws IOException
	 */
	public void makeIndex(EntityManager em) throws IOException {

		
		FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);

		// These are the entities we ant indexed
		// TODO: can we get this list automatically?
		//String[] entites = { "Estate", "House", "Family", "EmployeeRecord"};
		String[] entites = { "Estate", "House", "Family","Reference"};

		for (String entityName : entites) {
			List<Object> objects = em
					.createQuery("from " + entityName)
					.getResultList();
			log.info("Found " + objects.size() + " objects of type " +entityName + " to index.");
			for (Object o : objects) {
				fullTextEm.index(o);
			}
		}
		
				
	}
	
	/**
	 * (Re-)index entity and save object history record.
	 * @param em
	 * @param user
	 * @param o
	 */
	public void postEntityUpdate (EntityManager em, User user, RevisionTracked o) {
		index(em,o);
		o.setLastModified(new Date());
		o.setLastModifiedBy(user);
		ObjectHistory oh = new ObjectHistory();
		oh.setUser(user);
		oh.setObjectClass(o.getClass().getName());
		oh.setObjectId(o.getId());
		
		// Save modified object JSON
		//Gson gson = new Gson();
		
		Gson gson = new GsonBuilder().setExclusionStrategies(new ExclusionStrategy() {

			public boolean shouldSkipClass(Class<?> clazz) {
				return false;
			}

			/**
			 * Custom field exclusion goes here
			 */
			public boolean shouldSkipField(FieldAttributes f) {
				return f.getName().equals("password");
			}

		}).serializeNulls().create();
		String objectJson = gson.toJson(o);
		oh.setObjectJson(objectJson);
		
		em.persist(oh);
	}

	/**
	 * Reindex an single entity object.
	 * 
	 * @param em
	 * @param o
	 */
	public void index(EntityManager em, Object o) {
		FullTextEntityManager fullTextEm = Search.getFullTextEntityManager(em);
		fullTextEm.index(o);
	}
	
	/**
	 * Return Estate records matching query 'q'. Return empty array if none found.
	 * 
	 * @param q
	 * @return List of Estate and Property (house) objects
	 * @throws IOException
	 */
	public List<Object> search(EntityManager em, String q) throws SearchException {
		IndexSearcher searcher;

		if (!indexDir.exists() || !indexDir.isDirectory()) {
			throw new SearchException ("Index directory not found at " + indexDir.getPath() 
					+ ". Probable cause: search index not created or configured.");
		}
		
		
		if (isRecordId(q)) {
			Long id = new Long(q.substring(1));
			if (q.startsWith("E")) {
				try {
					Estate estate = em.find(Estate.class,id);
					estate.getName(); // make sure it's loaded
				} catch (ObjectNotFoundException e) {
					return new ArrayList<Object>(0);
				}
			}
		}
		/*
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
		*/
		return categoryList;
		
		/*
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
		*/
		
		/*
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
		*/
	}

	public void exportDb(Writer w) throws IOException {
		DBExport.export(w);
	}



	
	
	/*
	 * 
	 */
	public List<ObjectHistory> getRecentChanges(EntityManager em) {
		long startTime = System.currentTimeMillis();

		
		String query = "from ObjectHistory order by modified desc";
		List<ObjectHistory> result = em.createQuery(query).getResultList();
	
		System.err.println("getRecentChanges(): duration = "
				+ (System.currentTimeMillis() - startTime));

		return result;
		
	}

	public List<ObjectHistory> getRevisionHistory(EntityManager em, String className, Long id) {
		@SuppressWarnings("unchecked")
		List<ObjectHistory> result = em
				.createQuery("from ObjectHistory "
				+ " where objectClass=:objectClass and objectId=:objectId "
				+ " order by modified desc")
				.setParameter("objectClass", className)
				.setParameter("objectId", id)
				.getResultList();
		return result;
	}
	/*
	 * Save object in audit trail
	 */
	private static void saveObjectRevision(EntityManager em, Long id, int version, Object o) {

		String query = "from ObjectHistory where objectId=" + id 
		+ " and version=" + version;
		
		@SuppressWarnings("unchecked")
		List<ObjectHistory> result = em
				.createQuery("from ObjectHistory where objectId=:objectId")
				.setParameter("objectId", id)
				.getResultList();
		
		log.info ("saveObjectRevision(): query=" + query);
		log.info ("saveObjectRevision(): query returned " + result.size() + " objects");
		ObjectHistory oh;
		if (result.size() > 0) {
			oh = (ObjectHistory)result.get(0);
		} else {
			log.info ("creating new ObjectHistory object");
			oh = new ObjectHistory();
			oh.setObjectId(id);
			oh.setObjectClass(o.getClass().getName());
			oh.setVersion(version);
			em.persist(oh);;
		}
		oh.setModified(new Date());
		
		Gson gson = new Gson();
		oh.setObjectJson(gson.toJson(o));
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
	
	public void initTemplateContext(Context context) {
		context.put("VERSION", DB.VERSION);
		
		// Obsolete YUI
		context.put("YUI", "http://yui.yahooapis.com/2.8.2");
		context.put("YUIJS", "http://yui.yahooapis.com/2.8.2/");
		context.put("YUICSS", "http://yui.yahooapis.com/2.8.2/");
		
		// Google Maps API key
		context.put("GOOGLE_MAP_KEY",Configuration.getConfiguration("GOOGLE_MAP_KEY"));
		
		context.put("formatUtils", new FormatUtils());
		context.put ("tabs",tabs);
		
		context.put ("dateFormat",dateFormat);
		
		// Longitude degrees per meter 
		context.put ("degPerMLon",degPerMLon);
		
		// Latitude degrees per meter
		context.put ("degPerMLat",degPerMLat);
		
		//String contextPath = request.getContextPath();
		//context.put("LEAFLET_SCRIPT",contextPath+"/webjars/leaflet/" + leafletVersion + "/leaflet.js");
		//context.put("LEAFLET_CSS",contextPath+"/webjars/leaflet/" + leafletVersion + "/leaflet.css");
		
	}
	
}
