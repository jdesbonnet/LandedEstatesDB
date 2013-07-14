<%@page import="ie.wombat.gis.OSIGridReference"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ie.wombat.imagetable.ImageDB"%>
<%@page import="ie.wombat.imagetable.Image"%>
<%@include file="_header.jsp"%><%
	/**
 * This script uses the OSI Grid Reference to set latitude and longidue if they
 * have not already been set.
 */
 
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}

	List<House> allHouses = hsession.createQuery ("from Property").list();
	
	for (House house : allHouses) {
		if (!house.hasGridReference()) {
	continue;
		}
		OSIGridReference gr = new OSIGridReference(house.getGridReference());
		//if (gr.getLatitude() == null || gr.getLongitude() == null) {
	//out.println ("WARNING: No lat/lon for House#" + house.getId() +"<br>\n");
	//continue;
		//}
		
	
		house.setLatitude(gr.getLatitudeDeg());
		house.setLongitude(gr.getLongitudeDeg());
		out.println ("Setting lat/lon for House#" + house.getId() + "<br>\n");
		
		
	}
%>
all done!