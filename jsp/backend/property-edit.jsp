<%@include file="_header.jsp"%><%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long id = new Long(request.getParameter("id"));

	Property property = (Property)em.find(Property.class, id);
	
	context.put ("tabId","houses");
	context.put ("property", property);
	
	templates.merge ("/backend/property-edit.vm",context,out);
%>
