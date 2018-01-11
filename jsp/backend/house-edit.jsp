<%@include file="_header.jsp"%><%
	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long id = new Long(request.getParameter("id"));
	House house = (House)em.find(House.class, id);
	context.put ("house", house);
	context.put ("pageId","./house-edit");
	context.put ("showSideCol","true");
	templates.merge ("/backend/master.vm",context,out);
%>
