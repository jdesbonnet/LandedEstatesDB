<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Reference reference;	
	try {
		Long id = new Long(request.getParameter("id"));
		reference = (Reference)hsession.load(Reference.class,id);
	} catch (Exception e) {
		throw new ServletException ("no or bad 'id'");
	}
	

	Estate estate;
	try {
		Long estateId = new Long (request.getParameter("estate_id"));
		estate = (Estate)hsession.load(Estate.class, estateId);
	} catch (Exception e) {
		throw new ServletException ("no or bad 'estate_id'");
	}
	
	try {
		Long sourceId = new Long (request.getParameter("source_id"));
		ReferenceSource source = (ReferenceSource)hsession.load(ReferenceSource.class,sourceId);
		reference.setSource(source);
	} catch (Exception e) {
		throw new ServletException ("no or bad 'source_id'");
	}
	
	reference.setDescription (request.getParameter("description"));
	
	System.err.println ("reference description=" + reference.getDescription());
	System.err.println ("reference description len=" + reference.getDescription().length());
	
	hsession.save (reference);
	
	response.sendRedirect("estate-edit.jsp?id=" + estate.getId());
%>
