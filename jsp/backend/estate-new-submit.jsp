<%@include file="_header.jsp"%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Estate estate = new Estate ();
	estate.setName (request.getParameter("name"));
	
	try {
		estate.setProjectPhase (new Integer(request.getParameter("project_phase")));
	} catch (Exception e) {
		// ignore
	}
	
	db.saveEstate (estate);
	response.sendRedirect("estate-edit.jsp?id=" + estate.getId());
%>
