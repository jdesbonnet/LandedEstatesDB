<%@page import="ie.wombat.gis.OSIGridReference"%>
<%@page import="ie.wombat.gis.InvalidGridReferenceException"%>
<%@include file="_header.jsp"%><%

/*
 * Generate HTML for popup window (approx 400 x 400) containing map.
 * The map is centered on an existing property record (with a valid grid reference) by
 * passing the id of the house record in 'id'.
 * Or at a specified grid reference by passing a 'gridref' parameter.
 * If neither 'id' or 'gridref' passed then show default map of west of Ireland.
 */
 
if (request.getParameter("id") != null) {

	Property property;
	try {
		Long id = new Long(request.getParameter("id"));
		property = (Property)hsession.load(Property.class, id);
	} catch (Exception e) {
		out.println ("error: " + e);
		return;
	}
	context.put ("lat", new Double(property.getLatitudeDeg()));
	context.put ("lon", new Double(property.getLongitudeDeg()));
	context.put ("zoom", "3");
} else if (request.getParameter("gridref") != null) {
	try {
		OSIGridReference osigr = new OSIGridReference(request.getParameter("gridref"));
		context.put ("lat", new Double(osigr.getLatitudeDeg()));
		context.put ("lon", new Double(osigr.getLongitudeDeg()));
		context.put ("zoom", "3");
	} catch (InvalidGridReferenceException e) {
		context.put ("error", "Invalid grid reference: " + request.getParameter("gridref"));
	}
} else {
	context.put ("lat","53.3");
	context.put ("lon", "-9");
	context.put ("zoom", "10");
}
templates.merge ("/backend/popup-map.vm",context,out);
%>
