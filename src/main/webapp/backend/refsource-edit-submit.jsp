<%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}


	Long refSourceId = new Long (request.getParameter("id"));
	ReferenceSource source = (ReferenceSource)em.find(ReferenceSource.class, refSourceId);
	
	if (source == null) {
		source = new ReferenceSource ();
		em.persist(source);
	}

	source.setName (request.getParameter("name"));
	source.setDescription (request.getParameter("description"));
	source.setContactPerson(request.getParameter("contact_person"));
	source.setContactTelephone(request.getParameter("contact_telephone"));
	source.setContactEmail(request.getParameter("contact_email"));
	

	Long categoryId = new Long (request.getParameter("category_id"));
	ReferenceCategory category = (ReferenceCategory)em.find(ReferenceCategory.class, categoryId);
	source.setCategory(category);
	
	response.sendRedirect("refsource-show.jsp?id=" + source.getId());
%>