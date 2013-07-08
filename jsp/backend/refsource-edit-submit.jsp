<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Long id = null;
	if (request.getParameter("id") != null) {
		try {
			id = new Long (request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
	}
	
	ReferenceSource source;
	if (id != null) {
		source = (ReferenceSource)hsession.load(ReferenceSource.class, id);
	} else {
		source = new ReferenceSource ();
	}

	source.setName (request.getParameter("name"));
	source.setContactPerson(request.getParameter("contact_person"));
	source.setContactTelephone(request.getParameter("contact_telephone"));
	source.setContactEmail(request.getParameter("contact_email"));
	
	try {
		Long categoryId = new Long (request.getParameter("category_id"));
		ReferenceCategory category = (ReferenceCategory)hsession.load(ReferenceCategory.class, categoryId);
		source.setCategory(category);
	} catch (Exception e) {
		throw new ServletException (e);
	}
	
	hsession.save (source);
	
	response.sendRedirect("refsource-show.jsp?id=" + source.getId());
%>
