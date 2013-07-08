<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}
	
	Estate estate = null;
	try {
		Long estateId = new Long(request.getParameter("estate_id"));
		estate = (Estate)hsession.load(Estate.class,estateId);
	} catch (Exception e) {
	}
	if (estate == null) {
		throw new ServletException ("no estate_id or estate not found");
	}

	context.put ("tabId","estates");
	context.put ("estate",estate);
		
	templates.merge ("/backend/estate-add-family.vm",context,out);
%>
