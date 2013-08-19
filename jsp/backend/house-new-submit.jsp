<%@include file="_header.jsp"%><%if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Estate estate;
	try {
		Long estateId = new Long (request.getParameter("estate_id"));
		estate = (Estate)em.find(Estate.class, estateId);
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
	
	em.persist(property);
	
	estate.getHouses().add(property);
	
	response.sendRedirect("house-edit.jsp?id=" 
	+ property.getId() 
	+ "&estate_id=" + estate.getId());%>
