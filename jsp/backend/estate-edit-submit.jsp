<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long id = new Long (request.getParameter("id"));
	Estate estate = (Estate)em.find(Estate.class,id);
	

	// TODO: XSS clean these inputs
	
	/*
	 * Check for some change before going to the trouble of saving to DB
	 */
	if (!request.getParameter("name").equals(estate.getName())
		|| !request.getParameter("description").equals(estate.getDescription()) ) {
		
		estate.setVersion (estate.getVersion() + 1);
		estate.setName (request.getParameter("name"));
		estate.setDescription (request.getParameter("description"));
	} 
	
	Integer projectPhase = new Integer(request.getParameter("project_phase"));
	estate.setProjectPhase(projectPhase);
	
	/*
	 * What happens next depends on which save button was clicked
	 */
	
	// Family association remove X buttons
	Long familyId = getIdFromSubmitParameter(request,"_submit_family_remove_");
	if (familyId != null) {
		Family family = em.find(Family.class,familyId);
		estate.getFamilies().remove(family);
		response.sendRedirect("estate-edit.jsp?id=" + estate.getId()+"#families");
		return;
	}
	
	Long referenceId = getIdFromSubmitParameter(request,"_submit_reference_edit_");
	if (referenceId != null) {
		response.sendRedirect("reference-edit.jsp?id=" + referenceId 
				+ "&estate_id=" + estate.getId());
		return;
	}
	
	referenceId = getIdFromSubmitParameter(request,"_submit_reference_delete_");
	if (referenceId != null) {
		Reference reference = em.find(Reference.class,referenceId);
		estate.getReferences().remove(reference);
		response.sendRedirect("estate-edit.jsp?id=" + estate.getId()+"#references");
		return;
	}

	
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
	

	estate.setLastModifiedBy(user);
	db.index(em,estate);
	
	response.sendRedirect("estate-show.jsp?id=" + estate.getId());
%><%!
	public Long getIdFromSubmitParameter(HttpServletRequest request, String prefix) {
		for (String key : request.getParameterMap().keySet()) {
			if (key.startsWith(prefix)) {
				Long id = new Long(key.substring(prefix.length()));
				return id;
			}
		}
		return null;
	}
%>
