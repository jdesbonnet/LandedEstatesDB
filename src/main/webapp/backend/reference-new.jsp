<%
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long estateId = new Long (request.getParameter("estate_id"));
	context.put ("estate", em.find(Estate.class, estateId) );
	
	context.put ("pageId","./reference-new");

	templates.merge ("/backend/master.vm",context,out);
%>
