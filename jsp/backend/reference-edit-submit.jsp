<%@include file="_header.jsp"%><%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}
	
	Long refId = new Long(request.getParameter("id"));
	Reference reference = (Reference)em.find(Reference.class,refId);

	Long estateId = new Long (request.getParameter("estate_id"));
	Estate estate = (Estate)em.find(Estate.class, estateId);

	Long sourceId = new Long (request.getParameter("source_id"));
	ReferenceSource source = (ReferenceSource)em.find(ReferenceSource.class,sourceId);
	reference.setSource(source);

	reference.setDescription (request.getParameter("description"));

	response.sendRedirect("estate-edit.jsp?id=" + estate.getId());
%>
