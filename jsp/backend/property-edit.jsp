<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Property property;
	try {
		Long id = new Long(request.getParameter("id"));
		property = (Property)hsession.load(Property.class, id);
	} catch (Exception e) {
		out.println ("error: " + e);
		return;
	}
	context.put ("tabId","houses");
	context.put ("property", property);
	
	templates.merge ("/backend/property-edit.vm",context,out);
%>
