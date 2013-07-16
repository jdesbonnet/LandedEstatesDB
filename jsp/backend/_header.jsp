<%@page 
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="java.util.Set"
import="java.util.List"
import="java.util.Date"
import="java.util.ArrayList"
import="java.util.Iterator"
import="java.util.HashMap"
import="java.util.ArrayList"
import="java.util.Properties"
import="java.text.DecimalFormat"
import="java.text.SimpleDateFormat"
import="java.sql.Connection"
import="java.sql.ResultSet"
import="java.sql.SQLException"
import="javax.persistence.EntityManager"
import="net.sf.json.util.JSONUtils"
import="ie.wombat.template.*"
import="ie.wombat.ui.Tab"
import="ie.wombat.landedestates.*"
import="ie.wombat.imagetable.ImageDB"
import="ie.wombat.imagetable.Image"
%><%request.setCharacterEncoding("utf-8");%><%!// errorPage="error.jsp"

public static final DecimalFormat latlonf = new DecimalFormat ("###.00000");

// Used where complete timestamp is needed (eg debugging info)
public static SimpleDateFormat timestampDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");

// TODO: is this necessary?
public static String imageEntityName = "Image";

private static String[] alphabet = { "A","B","C","D","E","F","G","H","I","J","K","L","M",
"N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
private static final double re = 6378100;
// degrees longitude per meter (near Ireland)
private static Double degPerMLon =  new Double (180 /  (Math.PI * re * Math.cos(53.5 * Math.PI/180)));
// degrees latitude per meter (near Ireland)
private static Double degPerMLat =  new Double (180 /  (Math.PI * re));

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
	
			
	private static Tab[] estatesSubTabs = {
				new Tab ("az", "A-Z", "estate-list.jsp"),
				new Tab ("listall", "List all", "estate-list.jsp?letter=_all"),
				new Tab ("newestate","New estate","estate-new.jsp"),
	};
	
	private static Tab[] housesSubTabs = {
				new Tab ("az", "A-Z", "house-list.jsp"),
				new Tab ("listall", "List all", "house-list.jsp?letter=_all")
	};
	
	private static Tab[] employeeRecordsSubTabs = {
		//new Tab ("new", "New record", "employee-record-add.jsp?letter=_all"),
		new Tab ("listall", "List all employee records", "employee-record-list.jsp?letter=_all"),
		new Tab ("listtags", "List tags", "employee-record-tag-list.jsp"),
		new Tab ("export", "Export employee records","employee-record-export.jsp")
	};
	
	
	private static Tab[] refSourcesSubTabs = {
				new Tab ("newrefsource", "New reference source",
						"refsource-new.jsp")
	};
	
	//private static Tab[] imageSubTabs {
	//};
	
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
	
	/*
	private String latin1ToUTF8 (String latinIn) {
		try {
			return new String(latinIn.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e1) {
            return e1.toString();
        }
	}
	*/
	
	/**
	 * For use in Velocity templates to ensure that a string is safe to include
	 * in the value attribute of a text input field. Need to pass reference
	 * to JSP in Velocity context (eg context.put("jsp", this);)
	 */
	public String escape (String in) {
		StringBuffer buf = new StringBuffer();
		char[] ca = in.toCharArray();
		for (int i = 0; i < ca.length; i++) {
				if (ca[i] == '"') {
					buf.append ("&quot;");
				} else {
					buf.append (ca[i]);
				}
		}
		return buf.toString();
	}
	public String formatLatLon (House prop) {
		return latlonf.format(prop.getLatitude())
				+ " "
				+ latlonf.format(prop.getLongitude());
	}%><%

	
	// Debug info block (enclosed to prevent namespace polution)
	{
	System.err.println ("\n\n____ Start of Request ____");
	System.err.println ("Timestamp = "  + timestampDateFormat.format(new Date()));
	System.err.println ("Remote Host = " + request.getRemoteHost() + "(" + request.getRemoteAddr() + ")");
	System.err.println ("HTTPMethod = " + request.getMethod() );
    System.err.println ("ServerName = " + request.getServerName());
    StringBuffer requestURL = request.getRequestURL();
    if (request.getQueryString() != null) {
        requestURL.append("?").append(request.getQueryString());
    }
    String completeURL = requestURL.toString();
    System.err.println ("requestURL = " + completeURL);
	System.err.println ("requestURLLen = " + completeURL.length());
    
    
    System.err.println ("Headers:");
    java.util.Enumeration en = request.getHeaderNames();
    while (en.hasMoreElements()) {
        String headerName = (String) en.nextElement();
        System.err.println ("    " + headerName + " = " + request.getHeader(headerName));
    }
    
    System.err.println ("Cookies:");
    Cookie[] cookies = request.getCookies();
    if (cookies == null || cookies.length==0) {
            System.err.println ("    (what??! no cookie!)");
    }
    if (cookies != null) {
    	for (Cookie cookie : cookies) {
			System.err.println ("    " + cookie.getName() + " = " + cookie.getValue());
		}
	}
    
    System.err.println ("Parameters:");
    en = request.getParameterNames();
    while (en.hasMoreElements()) {
        String paramName = (String) en.nextElement();
        System.err.println ("    " + paramName + " = " + request.getParameter(paramName));
    }


    } // end debug info block

    
    
	// Create a transaction
	EntityManager em = HibernateUtil.getEntityManager();
	em.getTransaction().begin();

	TemplateRegistry templates= TemplateRegistry.getInstance();
  
  	/*
	 * Initialize our own templating wrapper
	 */
	if (! templates.isInitialized()) {
		
		try {
	templates.init(
		getServletContext().getRealPath("/templates"));
		} catch (ie.wombat.framework.AppException e) {
	throw new ServletException(e.toString());
		}
	}
	
  
    Context context = new Context(request,response);
    context.put ("jsp", this); // So that we can use methods eg escape()
    context.put ("contextPath",request.getContextPath());
    context.put ("VERSION",DB.VERSION);
    //context.put ("YUI","/yui");
    context.put ("YUI","http://yui.yahooapis.com/2.8.2");
    context.put ("YUIJS","http://yui.yahooapis.com/2.8.2/");
    context.put ("YUICSS","http://yui.yahooapis.com/2.8.2/");
    context.put ("tabs",tabs);
    
    context.put ("degPerMLon",degPerMLon);
    context.put ("degPerMLat",degPerMLat);
    
    context.put ("dateFormat", dateFormat);
    

    //context.put ("yahooMapKey","YahooDemo");
    
    // new key obtained for http://landedestates.nuigalway.ie 8 Sep 2009.
    context.put ("yahooMapKey","KozUJ.TV34Hl5WEiVy2GxjMUXdYHUbjXGWjn63ZXBD5LyAmoAKvcalQwmzbWdeOTGw--");

    
    /*
     * Set jspfile to name of JSP script file
     */
    String jspFile;
    {
    	String[] p = request.getRequestURI().split("/");
    	jspFile = p[p.length-1];
    	context.put ("jspfile", p[p.length-1]);
    }
  
    User user = (User)session.getAttribute("_user");
 
	/*
	 * If user not logged in redirect to login.jsp. Skip this
	 * test for scripts 'login.jsp' and 'login-submit.jsp'.
	 */
    if (user == null) {
    	if (!jspFile.equals("login.jsp") 
    		&& !jspFile.equals("login-submit.jsp") 
    		&& !jspFile.equals("error.jsp")
    		) {
    		System.err.println ("not logged in, redirecting to login.jsp");
    		response.sendRedirect("login.jsp");
    		return;
    	}
    }
    context.put ("user", user);
    
	DB db = DB.getInstance();
	
	
	context.put ("q", request.getParameter("q"));
	
	context.put ("jsp",this);
	//context.put ("db",db);
	context.put ("counties",db.getCounties());
	context.put ("serverName", request.getServerName());
	
	Integer phase=null;
	
	// Enclose in block to prevent 'p' var interfering with other scripts
	{
		String p = request.getParameter("p");
		// If not specified, see if already set in session
		if (p == null) {
			p = (String)session.getAttribute("projectPhase");
		}
		try {
			phase = new Integer(p);
			context.put ("phase",phase);
			// Remember using session attribute
			session.setAttribute("projectPhase",phase.toString());
		} catch (Exception e) {
			// ignore
		}
	}
%>