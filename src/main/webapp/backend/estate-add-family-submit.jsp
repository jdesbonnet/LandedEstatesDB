<%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	String estateIdStr = request.getParameter("estate_id");
	if (estateIdStr == null) {
			throw new ServletException ("no estate_id");
	}
	
	// Was Cancel button pressed
	if (request.getParameter("_submit_cancel") != null) {
		response.sendRedirect ("estate-edit.jsp?id="+estateIdStr);
		return;
	}
	
	Long estateId = new Long(request.getParameter("estate_id"));
	Estate estate = (Estate)em.find(Estate.class,estateId);

	if (estate == null) {
		throw new ServletException ("no estate_id or estate not found");
	}
	
	Long familyId = new Long(request.getParameter("family_id"));
	Family family = (Family)em.find(Family.class,familyId);

	if (family == null) {
		throw new ServletException ("no family_id or family not found");
	}

	estate.getFamilies().add(family);
	
	response.sendRedirect ("estate-edit.jsp?id="+estate.getId()+"#families");
%>
