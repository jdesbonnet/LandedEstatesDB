<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}

	Long id = new Long (request.getParameter("id"));
	Estate estate = (Estate)em.find(Estate.class,id);
	

	/*
	 * Check for some change before going to the trouble of saving to DB
	 */
	if (!request.getParameter("name").equals(estate.getName())
		|| !request.getParameter("description").equals(estate.getDescription()) ) {
		
		estate.setVersion (estate.getVersion() + 1);
		estate.setName (request.getParameter("name"));
		estate.setDescription (request.getParameter("description"));
	} 
	
	
	/*
	 * What happens next depends on which save button was clicked
	 */
	
	if (request.getParameter("_submit_add_family") != null) {
		response.sendRedirect("estate-add-family.jsp?estate_id=" + estate.getId());
		return;
	}
	
	if (request.getParameter("_submit_new_house") != null) {
		response.sendRedirect("house-new.jsp?estate_id=" + estate.getId());
		return;
	}
	
	if (request.getParameter("_submit_existing_house") != null) {
		response.sendRedirect("house-add-existing.jsp?estate_id=" + estate.getId());
		return;
	}
	
	if (request.getParameter("_submit_new_reference") != null) {
		response.sendRedirect("reference-new.jsp?estate_id=" + estate.getId());
		return;
	}
	
	response.sendRedirect("estate-show.jsp?id=" + estate.getId());
%>
