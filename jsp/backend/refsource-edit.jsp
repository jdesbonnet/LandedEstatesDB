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
	
	ReferenceSource source;
	if (id != null) {
		source = (ReferenceSource)hsession.load(ReferenceSource.class, id);
	} else {
		source = new ReferenceSource ();
	}

	context.put ("source", source);
	context.put ("categories", hsession.createQuery("from ReferenceCategory").list());
	
	templates.merge ("/backend/refsource-edit.vm",context,out);
%>
