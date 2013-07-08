<%@include file="_header.jsp"%><%

if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Estate estate;
	try {
		Long estateId = new Long (request.getParameter("estate_id"));
		estate = (Estate)hsession.load(Estate.class, estateId);
	} catch (Exception e) {
		throw new ServletException ("error parsing estate_id: " +e);
	}

	context.put ("tabId","estates");
	context.put ("estate",estate);
	templates.merge ("/backend/property-add-existing.vm",context,out);
%>
