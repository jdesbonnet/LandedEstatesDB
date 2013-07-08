<%@include file="_header.jsp"%><%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long id = null;
	if (request.getParameter("id") != null) {
		try {
			id = new Long(request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
	}
	
	Family family;
	if (id != null) {
		family = (Family)hsession.load(Family.class,id);
	} else {
		family = new Family ();
	}
	
	
	family.setName (request.getParameter("name"));
	family.setTitle (request.getParameter("title"));
	family.setDescription (request.getParameter("description"));
	
	hsession.save(family);
	
	//response.sendRedirect("family-show.jsp?id=" + family.getId());
	String next = request.getParameter("next");
	if (next != null) {
		response.sendRedirect(next);
	} else {
		response.sendRedirect("family-show.jsp?id=" + family.getId());
	}
%>
