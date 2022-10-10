<%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	context.put ("tabId","families");

	String referer = request.getHeader("Referer");
	if (referer != null) {
		context.put ("next",referer);
	}
	
	context.put("pageId","./family-add");
	templates.merge ("/backend/master.vm",context,out);
%>
