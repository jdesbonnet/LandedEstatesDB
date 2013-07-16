<%@include file="_header.jsp"%><%

	context.put ("tabId","employeeRecords");
	
	List<Tag> tags = em.createQuery("from Tag order by name").getResultList();
	context.put ("tags",tags);
	
	templates.merge ("/backend/employee-record-tag-list.vm",context,out);
%>
