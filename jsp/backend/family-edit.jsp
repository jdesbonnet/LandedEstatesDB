<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	context.put ("tabId","families");

	Long id = null;
	if (request.getParameter("id") != null) {
		try {
			id = new Long (request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
	}
	
	Family family;
	if (id != null) {
		family = (Family)hsession.load(Family.class,id);
	} else {
		family = new Family ();
	}

	context.put ("family", family);
	
	String referer = request.getHeader("Referer");
	if (referer != null) {
		context.put ("next",referer);
	}
	
	templates.merge ("/backend/family-edit.vm",context,out);
%>
