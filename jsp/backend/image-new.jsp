<%@include file="_header.jsp"%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


Property property;
try {
	Long id = new Long(request.getParameter("property_id"));
	property = (Property)hsession.load(Property.class, id);
} catch (Exception e) {
	throw new ServletException (e.toString());
}
context.put ("tabId","houses");
context.put ("property", property);

	context.put ("propertyId",request.getParameter("property_id"));
	templates.merge ("/backend/image-new.vm",context,out);
%>