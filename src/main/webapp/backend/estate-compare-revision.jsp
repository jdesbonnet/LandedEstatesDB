<%
	Estate estate;
	Long id = null;
	try {
		id = new Long (request.getParameter("estate_id"));
		estate = (Estate)em.find(Estate.class,id);
	} catch (NumberFormatException e) {
		throw new ServletException ("no estate_id or malformed estate_id");
	}
	
	int version = Integer.parseInt(request.getParameter("version"));
	
	Estate estatePrevious  = db.getEstateRevision(em, id, version);
	
	context.put ("estateCurrent", estate);
	context.put ("estatePrevious", estatePrevious);

	templates.merge ("/backend/estate-compare-revision.vm",context,out);
%>
