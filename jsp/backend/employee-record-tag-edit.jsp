<%@include file="_header.jsp"%><%

	context.put ("tabId","employeeRecords");
	
	Long tagId = new Long(request.getParameter("tag_id"));
	Tag tag = em.find(Tag.class,tagId);
	context.put ("tag",tag);
	
	templates.merge ("/backend/employee-record-tag-edit.vm",context,out);
%>
