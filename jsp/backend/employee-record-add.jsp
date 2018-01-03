<%@include file="_header.jsp"%><%

	if (!user.hasWriteAccess()) {
		throw new ServletException ("No write access to this database");
	}
	
	Long estateId = new Long(request.getParameter("estate_id"));
	Estate estate = em.find(Estate.class, estateId);
	
	context.put ("tabId","employeeRecords");
	context.put ("estate", estate);
	context.put ("pageId", "./employee-record-add");
	templates.merge ("/backend/master.vm",context,out);%>
