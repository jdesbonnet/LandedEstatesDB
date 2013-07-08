<%@page 
contentType="text/html; charset=utf-8"
pageEncoding="utf-8"
import="java.util.Set"
import="java.util.List"
import="java.util.ArrayList"
import="java.util.Iterator"
import="java.util.HashMap"
import="java.util.ArrayList"
import="java.util.Properties"
import="java.text.DecimalFormat"
import="java.sql.Connection"
import="java.sql.ResultSet"
import="java.sql.SQLException"
import="ie.wombat.template.*"
import="ie.wombat.ui.Tab"
import="ie.wombat.landedestates.*"
import="ie.wombat.imagetable.ImageDB"
import="ie.wombat.imagetable.Image"
errorPage="error.jsp"
%><%request.setCharacterEncoding("utf-8");%>
<%!

// Number formatter for Latitude/Longitude (5 places after decimal)
public static final DecimalFormat latlonf = new DecimalFormat ("###.00000");

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
				//new Tab ("newfamily","New family", "family-edit.jsp")
	};
	
			
	private static Tab[] estatesSubTabs = {
				new Tab ("az", "A-Z", "estate-list.jsp"),
				new Tab ("listall", "List all", "estate-list.jsp?letter=_all"),
				//new Tab ("newestate","New estate","estate-new.jsp"),
	};
	
	private static Tab[] housesSubTabs = {
				new Tab ("az", "A-Z", "property-list.jsp"),
				new Tab ("listall", "List all", "property-list.jsp?letter=_all")
	};
	
	private static Tab[] refSourcesSubTabs = {
				//new Tab ("newrefsource", "New reference source", "refsource-new.jsp")
	};
	
	//private static Tab[] imageSubTabs {
	//};
	
	private static Tab[] tabs = { 
		new Tab("home","Map","index.jsp"),
		new Tab("families","Families","family-list.jsp", familiesSubTabs), 
		new Tab ("estates","Estates","estate-list.jsp",estatesSubTabs),
		new Tab ("houses","Houses","property-list.jsp",housesSubTabs	),
		new Tab ("refsources","Reference Sources","refsource-list.jsp", refSourcesSubTabs),
		new Tab ("images", "Images", "images.jsp"),
		//new Tab ("tools","Tools","tools.jsp")
	};
	
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
	public String formatLatLon (Property prop) {
		return latlonf.format(prop.getLatitude())
				+ "<br />"
				+ latlonf.format(prop.getLongitude());
	}
	public String formatLatLonForUrl (Property prop) {
		return latlonf.format(prop.getLatitude())
				+ ","
				+ latlonf.format(prop.getLongitude());
	}
%><%
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
    context.put ("YUI","http://yui.yahooapis.com/2.9.0/build");
    
    // Key applied for http://test.galway.net:8080/LandedEstates/jsp/ on 18 Nov 2007.
	//context.put ("yahooMapKey", "dlLBEf7V34EfycUAzlDjDCBvEQGMcz.EgWqDhh3yKNtuXjDTbpALgidyvpFtnecmCw--");
	
    // new key obtained for http://landedestates.nuigalway.ie 8 Sep 2009.
    context.put ("yahooMapKey","KozUJ.TV34Hl5WEiVy2GxjMUXdYHUbjXGWjn63ZXBD5LyAmoAKvcalQwmzbWdeOTGw--");

    
    context.put ("tabs",tabs);
    
    context.put ("degPerMLon",degPerMLon);
    context.put ("degPerMLat",degPerMLat);
    

    // Key registered with Yahoo, 28 Jan 2008 and 
    // associated with http://www.landedestates.ie
    context.put ("yahooMapKey","hbSnbuLV34EvP5X5NQJl3amxv0K36Qf4hHdBT0R238gGPvNM3CW568rwtl1L_FqEbg--");
    
    /*
     * Set jspfile to name of JSP script file
     */
    String jspFile;
    {
    	String[] p = request.getRequestURI().split("/");
    	jspFile = p[p.length-1];
    	context.put ("jspfile", p[p.length-1]);
    }
      
	DB db = DB.getInstance();
	
	
	context.put ("q", request.getParameter("q"));
	
	context.put ("jsp",this);
	//context.put ("db",db);
	context.put ("counties",db.getCounties());
	context.put ("serverName", request.getServerName());
	
	org.hibernate.Session hsession = HibernateUtil.currentSession();
	org.hibernate.Transaction tx = hsession.beginTransaction();
	context.put ("hsession",hsession);

	context.put ("rootLevel","..");
%>