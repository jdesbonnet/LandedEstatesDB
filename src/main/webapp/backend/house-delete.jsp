<%@page import="java.util.List"%>
<%
	if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}

	House house;
	try {
		Long id = new Long (request.getParameter("id"));
		house = (House)em.find(House.class,id);
	} catch (Exception e) {
		throw new ServletException (e.toString());
	}
	
	/*
	 * Make sure this House record is not associated with any Estate. If so
	 * throw error message.
	 */
	String query = "from Estate where houses.id=" + house.getId();
	List<Estate> estates = em.createQuery(query).getResultList();
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
	
	em.remove (house);
	
	/* Return to referring page */
	String referer = request.getHeader("Referer");
	response.sendRedirect (referer);
%>
