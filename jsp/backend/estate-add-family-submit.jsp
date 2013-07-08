<%@include file="_header.jsp"%><%

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

	// Make sure a family was specified
	if (request.getParameter("family").trim().length() == 0) {
		response.sendRedirect ("estate-edit.jsp?id="+estateIdStr);
		return;
	}
	
	
	Estate estate = null;
	try {
		Long estateId = new Long(request.getParameter("estate_id"));
		estate = (Estate)hsession.load(Estate.class,estateId);
	} catch (Exception e) {
	}
	if (estate == null) {
		throw new ServletException ("no estate_id or estate not found");
	}
	

	
	
	
	Family family = null;
	
	/*
	try {
		Long familyId = new Long(request.getParameter("family_id"));
		family = (Family)hsession.load(Family.class,familyId);
	} catch (Exception e) {
	}
	if (family == null) {
		throw new ServletException ("no family_id or family not found");
	}
	*/
	
	Long familyId = DB.getIdFromAutoCompleteField(request.getParameter("family"));
	if (familyId == null) {
		throw new ServletException ("Invalid family. Expected a family record number in square brackets.");
	}
	try {
		family = (Family)hsession.load(Family.class,familyId);
		estate.getFamilies().add(family);
	} catch (Exception e) {
		throw new ServletException (e.toString());
	}
	
	
	hsession.save(estate);
	
	response.sendRedirect ("estate-edit.jsp?id="+estate.getId());
%>
