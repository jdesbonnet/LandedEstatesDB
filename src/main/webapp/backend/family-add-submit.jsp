<%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Family family = new Family();

	// TODO: XSS clean inputs
	
	family.setName (request.getParameter("name"));
	family.setTitle (request.getParameter("title"));
	family.setDescription (request.getParameter("description"));
	em.persist(family);
	
	db.postEntityUpdate(em, user, family);
	
	//response.sendRedirect("family-show.jsp?id=" + family.getId());
	String next = request.getParameter("next");
	if (next != null) {
		response.sendRedirect(next);
	} else {
		response.sendRedirect("family-show.jsp?id=" + family.getId());
	}
%>
