<%@include file="_header.jsp"%><%
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
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
	try {
		Long familyId = new Long(request.getParameter("family_id"));
		family = (Family)hsession.load(Family.class,familyId);
	} catch (Exception e) {
	}
	if (family == null) {
		throw new ServletException ("no family_id or family not found");
	}


	estate.getFamilies().remove(family);
	hsession.save(estate);
	
	response.sendRedirect ("estate-edit.jsp?id="+estate.getId());
%>
