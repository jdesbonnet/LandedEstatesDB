<%@include file="_header.jsp"%><%if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Estate estate;
	try {
		Long estateId = new Long (request.getParameter("estate_id"));
		estate = (Estate)hsession.load(Estate.class, estateId);
	} catch (Exception e) {
		throw new ServletException (e.toString());
	}
	
	House property = new House();
	property.setName(request.getParameter("name"));
	
	try {
		property.setProjectPhase (new Integer(request.getParameter("project_phase")));
	} catch (Exception e) {
		// ignore
	}
	
	hsession.save(property);
	
	estate.getProperties().add(property);
	hsession.save(estate);
	
	response.sendRedirect("property-edit.jsp?id=" 
	+ property.getId() 
	+ "&estate_id=" + estate.getId());%>
