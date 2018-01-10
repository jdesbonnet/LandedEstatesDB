<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long estateId = new Long (request.getParameter("estate_id"));
	Estate estate = (Estate)em.find(Estate.class,estateId);
	
	context.put ("estate",estate);
	templates.merge ("/backend/house-new.vm",context,out);
%>
