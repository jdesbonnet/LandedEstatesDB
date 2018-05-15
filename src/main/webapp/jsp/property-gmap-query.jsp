<%@page import="ie.wombat.landedestates.House"%>
<%
	/*
	 * Query service for Google Maps
	 */
	 
	 /*
	String hquery = "from Property";
	
	out.clear();
	response.setContentType("application/xml");
	out.println ("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
	out.println ("<markers>");
	
	
	Iterator iter = em.createQuery(hquery).getResultList.iterator();
	while (iter.hasNext()) {
		House p = (House)iter.next();
		if (p.getName() != null && p.hasGridReference()) {
	out.println ("<marker ");
	out.println (" id=\"" + p.getId() + "\"");
	out.println (" lat=\"" + p.getLatitudeDeg() + "\"");
	out.println (" lon=\"" + p.getLongitudeDeg() + "\"");
	out.println (" name=\"" + XmlUtil.makeSafe(p.getName()) + "\"");
	out.println (" townland=\"" + XmlUtil.makeSafe(p.getTownland()) + "\"");
	out.println (" gridref=\"" + p.getGridReference() + "\"");
	out.println ("/>");
		}
	}
	out.println ("</markers>");
	*/
%>