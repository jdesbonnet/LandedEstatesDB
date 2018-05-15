<%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long estateId = new Long(request.getParameter("estate_id"));
	Estate estate = em.find(Estate.class,estateId);

	Long referenceId = new Long(request.getParameter("reference_id"));
	Reference reference = em.find(Reference.class,referenceId);
	
	estate.getReferences().remove(reference);
	em.remove(reference);
	
	response.sendRedirect ("estate-edit.jsp?id="+estateId + "#references");
%>
