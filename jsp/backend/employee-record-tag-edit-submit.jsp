<%@include file="_header.jsp"%><%

	context.put ("tabId","employeeRecords");
	
	Long tagId = new Long(request.getParameter("tag_id"));
	Tag tag = em.find(Tag.class,tagId);
	
	tag.setName(request.getParameter("name"));
	response.sendRedirect("employee-record-tag-list.jsp");
%>
