<%@include file="_header.jsp"%><%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long estateId = new Long(request.getParameter("estate_id"));

	if (request.getParameter("_submit_cancel") != null) {
			response.sendRedirect ("estate-show.jsp?id="+estateId);
			return;
	}
	
	

	Estate estate = (Estate)em.find(Estate.class, estateId);

	
	String referenceDescription = XSSClean.clean(request.getParameter("description"));
	
	if (referenceDescription != null && referenceDescription.trim().length()>0) {
	
		Reference reference = new Reference ();
		reference.setDescription (referenceDescription);
		
		Long refSourceId = DB.getIdFromAutoCompleteField(request.getParameter("refsource"));
		ReferenceSource refSource = (ReferenceSource)em.find(ReferenceSource.class, refSourceId);
		reference.setSource(refSource);
		
		em.persist (reference);
		estate.getReferences().add(reference);
		//em.persist (estate);
	}
	
	/*
	 * Two save buttons: one will return to reference-new.jsp,
	 * the other to estate-show.jsp
	 */
	if (request.getParameter("_submit_exit") != null) {
		response.sendRedirect("estate-show.jsp?id=" + estate.getId());
	} else {
		response.sendRedirect("reference-new.jsp?estate_id=" + estate.getId());
	}
%>
