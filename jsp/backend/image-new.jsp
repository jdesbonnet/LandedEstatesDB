<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long houseId = new Long(request.getParameter("house_id"));
	House house = (House)em.find(House.class, houseId);

	context.put ("house", house);

	context.put ("pageId","./image-new");
	templates.merge ("/backend/master.vm",context,out);
%>