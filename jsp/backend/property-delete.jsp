<%@page import="java.util.List"%>
<%@include file="_header.jsp"%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Property property;
	try {
		Long id = new Long (request.getParameter("id"));
		property = (Property)hsession.load(Property.class,id);
	} catch (Exception e) {
		throw new ServletException (e.toString());
	}
	
	/*
	 * Make sure this House record is not associated with any Estate. If so
	 * throw error message.
	 */
	String query = "from Estate where houses.id=" + property.getId();
	List estates = hsession.createQuery(query).list();
	if (estates.size() > 0) {
		StringBuffer errmsg = new StringBuffer();
		errmsg.append ("Cannot delete House record while it is still associated with an Estate. ");
		errmsg.append ("This House record is assoicated with: ");
		Iterator iter = estates.iterator();
		while (iter.hasNext()) {
			Estate estate = (Estate)iter.next();
			errmsg.append ("Estate #" + estate.getId() + " " + estate.getName() + "; ");
		}
		
		throw new ServletException (errmsg.toString());
	}
	
	hsession.delete (property);
	
	/* Return to referring page */
	String referer = request.getHeader("Referer");
	response.sendRedirect (referer);
%>
