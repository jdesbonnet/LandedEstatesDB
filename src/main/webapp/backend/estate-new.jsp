<%
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	context.put ("tabId","estates");
	context.put("pageId","./estate-new");
	templates.merge ("/backend/master.vm",context,out);
%>