<%@include file="_header.jsp"%><%
	
if (!user.hasWriteAccess()) {
	throw new ServletException ("No write access to this database");
}


	Long id = null;
	if (request.getParameter("id") != null) {
		try {
			id = new Long (request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
	}
	
	Estate estate;
	if (id != null) {
		estate = (Estate)hsession.get(Estate.class,id);
	} else {
		estate = new Estate ();
	}

	context.put ("tabId","estates");
	context.put ("estate", estate);
	
	templates.merge ("/backend/estate-edit.vm",context,out);
%>
