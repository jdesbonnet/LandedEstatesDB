<%@include file="_header.jsp"%><%

	context.put ("tabId","employeeRecords");
	
	
	List<EmployeeRecord> employeeRecords = em
			.createQuery("from EmployeeRecord as er order by er.id")
			.getResultList();
	
	response.setContentType("text/plain");
	
	out.write ("TO DO");
%>
