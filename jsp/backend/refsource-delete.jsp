<%@include file="_header.jsp"%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Long id;
	try {
		id = new Long(request.getParameter("id"));
	} catch (Exception e) {
		throw new ServletException ("no or bad 'id'");
	}
	
	
	// TODO: this sould be enforced in at lower layer
	/*
	 * Make sure there are no Estate records referencing this
	 * source before deleting this ReferenceSource record.
	 */
	List<Estate> estates = hsession
		.createQuery("from Estate as e "
			+ " inner join fetch e.references as ref"
				+ " where ref.source.id=" + id)
		.list();
	
	if (estates.size() > 0) {
		StringBuffer errorText = new StringBuffer();
		errorText.append ("Cannot delete this ReferenceSource because the following records reference it: \n<ul>\n");
		for (Estate estate : estates) {
			errorText.append ("<li> Estate #" + estate.getId() +"</li>\n");
		}
		errorText.append("</ul>\n");
		throw new ServletException (errorText.toString());
	}
	
	ReferenceSource refSource = (ReferenceSource)hsession.load(ReferenceSource.class,id);
	hsession.delete(refSource);
	
	response.sendRedirect("refsource-list.jsp");
%>
