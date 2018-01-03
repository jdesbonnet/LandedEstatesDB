<%@include file="_header.jsp"%><%
	
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long id = new Long (request.getParameter("id"));
	Estate estate = (Estate)em.find(Estate.class,id);

	context.put ("tabId","estates");
	context.put ("estate", estate);
	context.put ("pageId","./estate-edit");
	templates.merge ("/backend/master.vm",context,out);
%>
