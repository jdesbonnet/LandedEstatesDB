<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Reference reference;
	try {
		Long id = new Long(request.getParameter("id"));
		reference = (Reference)hsession.load(Reference.class, id);
	} catch (Exception e) {
		throw new ServletException ("no 'id'");
	}
	

	try {
		Long estateId = new Long (request.getParameter("estate_id"));
		context.put ("estate", hsession.load(Estate.class,estateId));
	} catch (Exception e) {
		throw new ServletException ("error: no estate_id");
	}
	
	context.put ("reference", reference);
	context.put ("referenceSources", hsession.createQuery("from ReferenceSource order by name").list());
	
	templates.merge ("/backend/reference-edit.vm",context,out);
	
%>
