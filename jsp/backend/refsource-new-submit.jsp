<%@include file="_header.jsp"%><%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}


	ReferenceSource source = new ReferenceSource();
	source.setName (request.getParameter("name"));
	em.persist(source);
	
	response.sendRedirect("refsource-edit.jsp?id=" + source.getId());
%>
