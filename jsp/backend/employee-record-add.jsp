<%@include file="_header.jsp"%><%if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}

	Long houseId = new Long(request.getParameter("house_id"));
	House house = (House)em.find(House.class, houseId);
	
	context.put ("tabId","houses");
	context.put ("house", house);
	
	templates.merge ("/backend/employee-record-edit.vm",context,out);%>
